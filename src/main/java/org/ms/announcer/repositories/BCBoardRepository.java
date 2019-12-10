package org.ms.announcer.repositories;

import java.time.LocalDate;

import org.ms.announcer.domain.BCBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BCBoardRepository
 */
public interface BCBoardRepository extends JpaRepository<BCBoardDTO,Integer>{
    // List<BCBoardDTO> findAllByStartdate(LocalDate date , Pageable page);

    Page<BCBoardDTO> findAllByStartdate(LocalDate date, Pageable page );

    Page<BCBoardDTO> findByTitleLike(String title, Pageable page); 
    
    Page<BCBoardDTO> findByStartdateLike(String startdate, Pageable page); 
}