package com.ges.check.service;

import java.util.Map;

public interface BatchService {

    Map<String,Object> executeBatch(Map<String,Object> param)throws Exception;
}