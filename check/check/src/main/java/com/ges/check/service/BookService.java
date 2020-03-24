package com.ges.check.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
    List<Map<String,Object>> selectBookList(Map<String, Object> paramMap) throws Exception;
    Map<String,Object> insertBook(Map<String,Object> param) throws Exception;  
    Map<String,Object> selectBookIsbn13(Map<String,Object> param) throws Exception;
    Map<String,Object> updateBook(Map<String,Object> param) throws Exception;
    
    Map<String,Object> insertRank(Map<String,Object> param) throws Exception;
    //List<Map<String,Object>> selectGenre(Map<String,Object> param) throws Exception;
}