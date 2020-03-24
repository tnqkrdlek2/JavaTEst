package com.ges.check.service;

import java.util.List;
import java.util.Map;


public interface CheckService {
    List<Map<String,Object>> selectList(Map<String,Object> param) throws Exception;
    Map<String,Object> insertBoard(Map<String,Object> param) throws Exception;   
    Map<String,Object> updateBoard(Map<String,Object> param) throws Exception;
    Map<String,Object> deleteBoard(Map<String,Object> param) throws Exception;

    
}