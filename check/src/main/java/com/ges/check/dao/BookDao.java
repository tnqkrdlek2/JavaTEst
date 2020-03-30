package com.ges.check.dao;

import java.util.List;
import java.util.Map;

public interface BookDao{

    List<Map<String,Object>> selectBook(Map<String,Object> param);
    int insertBook(Map<String,Object> param);
    Map<String,Object> selectBookIsbn13(Map<String,Object> param);
    Map<String,Object> updateBook(Map<String,Object> param);
    int deleteBook(Map<String,Object> param);

    int insertRank(Map<String,Object> param);
    int deleteRank(Map<String,Object> param);
    List<Map<String,Object>> selectGenre(Map<String,Object> param);
    List<Map<String,Object>> selectJson(Map<String,Object> param);
}