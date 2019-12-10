package org.ms.announcer.service;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.ms.announcer.domain.BCBoardDTO;
import org.ms.announcer.repositories.BCBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * BCBoardServiceImpl
 */
@Service
@Slf4j
public class BCBoardServiceImpl implements BCBoardService {

    @Setter(onMethod_ = { @Autowired })
    private BCBoardRepository repos;

    @Override
    public void register(BCBoardDTO dto) {
        repos.save(dto);
    }

    public Page<BCBoardDTO> getTodayList( LocalDate date, Pageable page) {
        Page<BCBoardDTO> result = repos.findAllByStartdate(date, page);
        return result;
    }

    @Override
    public Map<String, Object> getAllList(Pageable page, String category, String search) {
        Page<BCBoardDTO> data =null;
        search = "%"+ search + "%";

        //카테고리구분
        if (category.equals("title")) {
            data = repos.findByTitleLike(search, page);
        }else{
            System.out.println("==============================꺄오======================================");
            data = repos.findByStartdateLike(search, page);
        }
        Map<String, Object> result  = new HashMap<>();
        int now  = data.getNumber();
        log.info(""+now);
        int total = data.getTotalPages();

        //시작페이지 설정
        // int start = now - 1 <= 0 ? 1 : (now+1)-2;  
        int start = 0;
        if(now -1 <=0 || total < 5){
            start = 1;
        }else{
            start = (now+1)-2;
        }
        result.put("start", start );

        //마지막페이지 설정
        int end =0;
        // if(total < 5 || now+2 > total ){
        //     end = total;
        // }else {
        //     end  = (now+1) +2;
        // }
        if( total < 5 || now+3 > total){
            end = total ;
        }else if(total> 5 && (now+1) <3 ){
            end = 5;
        }else{
            end = now+3;
        }
        result.put("end", end); 

        //리스트 데이터
        result.put("data", data.getContent());
        result.put("now", data.getNumber()+1);
        result.put("totalpages", total);

        result.put("category", category);
        result.put("search", search.replace("%", ""));
        return result;
    }

    

    
}