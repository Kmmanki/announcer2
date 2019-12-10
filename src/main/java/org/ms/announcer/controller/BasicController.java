package org.ms.announcer.controller;

import org.ms.announcer.service.BCBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Setter;

/**
 * BaiscController
 */
@Controller
@RequestMapping("/bcboard/*")
public class BasicController {

    @Setter(onMethod_ = {@Autowired} )
    BCBoardService bcboardService;

    @GetMapping("/wishlist" )
    public void indexTest() {
        System.out.println("들어왔나요??");
    }

    @GetMapping("/totallist" )
    public void total() {
        System.out.println("들어왔나요??");
    }

    @GetMapping("/todayList" )
    public void getbclist(Model model) {
        
    }
    
}