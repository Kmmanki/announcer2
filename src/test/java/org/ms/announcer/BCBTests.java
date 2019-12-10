package org.ms.announcer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.ms.announcer.domain.BCBoardDTO;
import org.ms.announcer.service.BCBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * BCBTests
 */
@SpringBootTest
public class BCBTests {

    @Autowired
    BCBoardService service;
    @Test
    public void Insert() {
        IntStream.range(1, 101).forEach(i ->{
            
            BCBoardDTO dto = new BCBoardDTO();
            dto.setTitle("title"+i);
            dto.setContent("content"+i);
            dto.setAudioName("audioName"+i);

            // LocalDate sdate = LocalDate.of(2019, (i%11==0 ? 1 : i%11), ((i*3)%30)==0? 1: i*3%30);
            LocalDate sdate = LocalDate.of(2019,12,10);
            dto.setStartdate(sdate);

            LocalTime stime = LocalTime.of((i*3)%23 == 0 ? 1: (i*3)%23 , (i*7)%59 ==0  ? 1: (i*7)%59  );
            // LocalTime stime = LocalTime.of(11 , (i)%59 == 0 ? 1: (i)%59  );
            dto.setStarttime(stime);
            dto.setAudioName("C:-AudioStorage-tmp-b133321369da4ac9b191c502625979e5_tmpasdada.mp3");
            service.register(dto);

        });
        
    }
    
}