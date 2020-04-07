package com.ges.check.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ges.check.dao.BookDao;
import com.ges.check.dao.GenreDao;
import com.ges.check.service.BatchService;
import com.ges.check.service.BookService;
import com.ges.check.service.NaruService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(rollbackFor = Exception.class)
public class BookServiceImpl implements BookService {

    @Autowired
    private NaruService mNaruService;

    @Autowired
    private GenreDao mGenreDao;

    @Autowired
    private BookDao mBookDao;

    @Autowired
    private BatchService mBatchService;


    Gson gson = new Gson();

    final int PageSize = 2;

   

    @Override
    public Map<String, Object> insertBook(Map<String, Object> param) throws Exception {
        System.out.println("1111111111111 serviceInpl insertbOok");
        try {
            mBatchService.executeBatch(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
         return param;
    }

    @Override
    public Map<String, Object> insertRank(Map<String, Object> param) throws Exception {
        mBatchService.executeBatch(param);
        return param;
    }

    @Override
    public Map<String, Object> selectBookIsbn13(Map<String, Object> param) throws Exception {
        return mBookDao.selectBookIsbn13(param);
    }

    @Override
    public List<Map<String, Object>> selectBookList(Map<String, Object> param) throws Exception {
        List<Map<String, Object>> list = mBookDao.selectBook(param);
        return list;
    }

    @Override
    public Map<String, Object> updateBook(Map<String, Object> param) throws Exception {
        return mBookDao.updateBook(param);
    }

    @Override
    public Map<String, Object> searchIsbn(Map<String, Object> param) throws Exception {
        //System.out.println("searchIsbn"+ param);
        String isbn = param.get("isbn13").toString();
        param.put("isbn13", isbn);
        List<Map<String,Object>> bookList = mBookDao.selectBook(param);
        
        //System.out.println("bookList==========="+ bookList );

        Map<String,Object> book = Maps.newHashMap();
        if(bookList.size() == 0 ){
            mBookDao.insertBook(param);
        }else{
            try {
                //book = bookList.get(0);
                 List<Map<String,Object>> bookItem = Lists.newArrayList();
                 for(int i =0; i< bookList.size(); i++){
                    book = bookList.get(i);
                    bookItem.add(book);
                 }
                 book.put("bookItem", bookItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return book;
    }

    @Override
    public List<Map<String,Object>> selectJson(Map<String,Object> param) throws Exception{
        return null;
    }

}