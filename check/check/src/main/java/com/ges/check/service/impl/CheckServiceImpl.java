package com.ges.check.service.impl;

import java.util.List;
import java.util.Map;

import com.ges.check.dao.CheckDao;
import com.ges.check.service.CheckService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private CheckDao mCheckDao;

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> param) throws Exception {
        System.out.println("22222>>>>> selectList Impl");
        
        List<Map<String, Object>> list = mCheckDao.selectList(param);
        return list;
    }

    @Override
    public Map<String,Object> insertBoard(Map<String,Object> param) throws Exception {
        // System.out.println("3333>>>>>> insertBoard Impl");
        // param.put("subject", "아무거나 들어가줘22222!!");
        // param.put("content", "내용 123412341234.");
        return  mCheckDao.insertBoard(param);
     
    }

    @Override
    public Map<String,Object> updateBoard(Map<String,Object> param) throws Exception{
        System.out.println("444444>>>>>> updateBoard Impl");
        param.put("subject", "변경제목입니다2222.");
        param.put("content", "변경내용입니다22222.");
        param.put("board_idx", 1);

        return mCheckDao.updateBoard(param);
    }

    @Override
    public Map<String,Object> deleteBoard(Map<String,Object> param) throws Exception{
        System.out.println("5555555>>>>>> deleteBoard Impl");
        param.put("board_idx", 86);
        return mCheckDao.deleteBoard(param);
    }
}