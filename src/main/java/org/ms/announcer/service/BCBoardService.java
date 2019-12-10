package org.ms.announcer.service;

import java.time.LocalDate;
import java.util.Map;

import org.ms.announcer.domain.BCBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * BCBoardService
 */
public interface BCBoardService {

    public void register(BCBoardDTO dto);
    public Map<String , Object> getAllList(Pageable page, String category, String search);
    public Page<BCBoardDTO> getTodayList(LocalDate date , Pageable page );
    
}