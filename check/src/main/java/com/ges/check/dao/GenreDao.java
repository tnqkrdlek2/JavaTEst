package com.ges.check.dao;

import java.util.List;
import java.util.Map;

public interface GenreDao {

    List<Map<String,Object>> selectGenre(Map<String,Object> param);
}