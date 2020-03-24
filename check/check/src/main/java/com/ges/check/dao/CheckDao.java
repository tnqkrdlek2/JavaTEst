package com.ges.check.dao;

import java.util.List;
import java.util.Map;


public interface CheckDao {

    List<Map<String,Object>> selectList(Map<String,Object> param) ;
    Map<String, Object> insertBoard(Map<String,Object> param) ;
    Map<String,Object> updateBoard(Map<String,Object> param);
    Map<String,Object> deleteBoard(Map<String,Object> param);
}