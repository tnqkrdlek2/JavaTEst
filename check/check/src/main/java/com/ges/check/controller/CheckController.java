package com.ges.check.controller;

import java.util.Map;

import com.ges.check.service.CheckService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CheckController {

    @Autowired
    private CheckService mCheckService;

    @GetMapping(value = "/list")
    public  String getSelectList(Map<String, Object> param) {
        // get 에서 왜 예외 처리 하는지 내일 알아볼것 
        //ResponseEntity 는 status field를 가지기 때문에 상태코드는 필수적으로 리턴필요
        //ResponseEntity는 전체 HTTP 응답을 나타냄 http 통신 상태 적을필요있음
        System.out.println("00000 >>>>>>List");
           // return new ResponseEntity<>(mCheckService.selectList(param), HttpStatus.OK);
        try {
            return mCheckService.selectList(param).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return null;
    }

    @RequestMapping(value = "/list1")
    public String selectList(Map<String,Object> param){
        try {
            return mCheckService.selectList(param).toString();
        } catch (Exception e) {
           
            e.printStackTrace();
        }
        return null;
    }
        
    
    
}