package com.ges.check.service.impl;

import java.util.List;
import java.util.Map;

import com.ges.check.dao.BookDao;
import com.ges.check.dao.GenreDao;
import com.ges.check.service.BatchService;
import com.ges.check.service.NaruService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private GenreDao mGenreDao;
    
    @Autowired
    private NaruService mNaruService;


    final int PageSize = 2;
    Gson gson = new Gson();

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeBatch(Map<String, Object> param) throws Exception {
        Map<String,Object> resultMap = Maps.newHashMap();

        BookList();
        return resultMap;
    }

    private Map<String,Object> BookList(){
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String,Object>> genreIndex = mGenreDao.selectGenre(null);

        Map<String,Object> genreMap = Maps.newHashMap();
        genreIndex.forEach(genre -> {

           String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);
           //System.out.println(url);
           ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

           @SuppressWarnings("all")
           Map<String,Object> result = gson.fromJson(response.getBody().toString(), Map.class);

           @SuppressWarnings("all")
           Map<String,Object> responseMap = (Map) result.get("response");
           
           @SuppressWarnings("all")
           List<Map<String,Object>> bookList = (List) responseMap.get("docs");

           List<Map<String,Object>> bookItem = Lists.newArrayList();
           for(int i =0; i < bookList.size(); i++){
               Map<String,Object> doc = bookList.get(i);
               @SuppressWarnings("all")
               Map<String,Object> book = (Map) doc.get("doc");

               bookItem.add(book);
           }
           System.out.println("bookItem =======:"+  bookItem);
           
        });

        return genreMap; 
    }
  


    private void CmsBatchProcess(String recommedType, Map<String,Object> BookResutl){

        for(String key : BookResutl.keySet()){
            @SuppressWarnings("all")            
            List<Map<String,Object>> BookResultList = (List) BookResutl.get(key);
            List<Map<String,Object>> cmsBookList = Lists.newArrayList();
        }

    }

}