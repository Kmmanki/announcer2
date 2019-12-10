package org.ms.announcer.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ms.announcer.domain.BCBoardDTO;
import org.ms.announcer.service.BCBoardService;
import org.ms.announcer.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;

/**
 * BCBoardController
 */
// 오늘목표 : 글 받아서 DB에 등록하고 audio파일 저장확인. 미리듣기 설정(글 바꿀때마다 기존꺼 날리고).
// list 페이지에서 즉시재생시키기(1번만)

@RestController
@RequestMapping("/rbcboard/*")
@CrossOrigin
public class BCBoardController {

    @Setter(onMethod_ = { @Autowired })
    private BCBoardService service;

    // ==================== 미리듣기 ====================
    // 나중에 gender외에 옵션들 추가할것(객체로 합치든지..)
    // 등록하는거 아니면 모두 get으로 받아라.. mapping성격이 다 다르다
    @GetMapping(value = "/prelisten")
    public ResponseEntity<List<String>> preListen(BCBoardDTO dto, String gender) {
        ResponseEntity<byte[]> response = makeAudio(dto, gender);
        List<String> list = new ArrayList<>();
        list.add(FileUtil.audioSave("tmp" + dto.getTitle(), response.getBody()).replace("\\", "-"));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{uploadPath}")
    public ResponseEntity<byte[]> pathCheck(@PathVariable("uploadPath") String uploadPath) {
        File audioFile = new File(uploadPath.replace("-", "\\"));
        byte[] audioData = null;
        try {
            audioData = FileCopyUtils.copyToByteArray(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(audioData, HttpStatus.OK);
    }

    // ==================== End 미리듣기 ====================

    // 최종 저장 (return type줘야함)
    @PostMapping(value = "/register")
    public void registerBC(@RequestBody BCBoardDTO dto, @PathVariable("gender") String gender) {
        ResponseEntity<byte[]> response = makeAudio(dto, gender);
        // 잘 들어왔나 확인
        System.out.println(response.getBody());
        // 글 받아서 등록(who : service(->repository)) with UUID!!!!!!!!!
        service.register(dto);
        // list로 돌아가게, ((((((바로 재생이 시작되어야함(시간 체크해서?) 이건 list에서 하자능 )))
    }

    // get 방식으로 url dptj 원하는 페이지, 오늘 날짜를 받는다.
    @GetMapping("/todaylist/{Startdate}")
    public ResponseEntity<Page<BCBoardDTO>> list(
            @PathVariable("Startdate") String Startdate) {

        // 정렬하여 값 가져올 기준
        Pageable page = PageRequest.of(0, 300, Direction.ASC, "startdate", "starttime");
        // 문자열로 가저온 것을 split
        String[] date = Startdate.split("-");
        // 값을 얻어온다.
        Page<BCBoardDTO> result = service.getTodayList(
                LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), page);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // get 방식으로 url dptj 원하는 페이지, 오늘 날짜를 받는다.
    @GetMapping("/totallist")
    public ResponseEntity<Map<String, Object>> getTotalList(
            @PageableDefault(page = 0, direction = Direction.ASC, sort = { "startdate", "starttime" }) Pageable page,
            String category, String search   ) {
            


        Map<String, Object> result = service.getAllList(page,category,search);
        return new ResponseEntity<>(result, HttpStatus.OK);
        // return null;
    }

    // KAKAO API
    public ResponseEntity<byte[]> makeAudio(BCBoardDTO dto, String gender) {

        String kakaoUrl = "https://kakaoi-newtone-openapi.kakao.com/v1/synthesize";
        String authKey = "KakaoAK 51b790b9e32597330a3825d667ef2c35";
        String xmlString = "<speak>";
        final String womanVoice = "<voice name=\"WOMAN_READ_CALM\">";
        final String manVoice = "<voice name=\"MAN_READ_CALM\">";
        RestTemplate restTemplate = new RestTemplate();

        xmlString += gender.equals("man") ? manVoice : womanVoice;
        xmlString += dto.getContent() + "</voice></speak>";
        System.out.println(xmlString);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
        headers.add("Authorization", authKey);
        headers.add("charset", "UTF-8");
        return restTemplate.postForEntity(kakaoUrl, new HttpEntity<byte[]>(xmlString.getBytes(), headers),
                byte[].class);
    }

}