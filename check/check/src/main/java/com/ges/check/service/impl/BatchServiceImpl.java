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

    @Autowired
    private BookDao mBookDao;

    final int PageSize = 5;
    Gson gson = new Gson();

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeBatch(Map<String, Object> param) throws Exception {
        Map<String,Object> resultMap = Maps.newHashMap();

        BookList();
        return resultMap;
    }


      // json 으로 List 가지고 오기
      private Map<String,Object> BookList() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String,Object>> genreIndex = mGenreDao.selectGenre(null);

        Map<String,Object> genreMap = Maps.newHashMap();
       
        genreIndex.forEach(genre ->{

           String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);
           //System.out.println(url);
           ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

           @SuppressWarnings("all")
           Map<String,Object> result = gson.fromJson(response.getBody().toString(), Map.class);

           @SuppressWarnings("all")
           Map<String,Object> responseMap = (Map) result.get("response");

           @SuppressWarnings("all")
           List<Map<String,Object>> bookList = (List) responseMap.get("docs");
           
           List<Map<String, Object>> bookListitem = Lists.newArrayList();
            
           for (int i = 0; i < bookList.size(); i++) {
               //System.out.println("tt");
               Map<String, Object> doc = bookList.get(i);
               @SuppressWarnings("all") // Map doc 가지고 온것 for 문으로 돌리기
               Map<String, Object> book = (Map) doc.get("doc");
               bookListitem.add(book);
               
               
            }
            genreMap.put("bookItem",bookListitem);
            System.out.println("=========genreMap=========== \n  "+ genreMap + "\n=========genreMap========");
            //genreMap.put("idx",genre.get("idx").toString());
            //genreMap.put("bookListitem", bookListitem);
            // if(bookList.size() == 20){
            //     mBookDao.insertRank(insertBookMap);
            // }
            // mBookDao.insertBook(insertBookMap);

        });
        
       
        return genreMap;
    }

    // private void BatchProcess(String recommendType, Map<String,Object> BookResult ) throws Exception{

    //     for(String key : BookResult.keySet()){
    //         @SuppressWarnings("all")
    //         List<Map<String,Object>> BookList = (List) BookResult.get(key);
    //         System.out.println("BatchProcess BookList >>>>>"+ BookList);

            
    //     }
    // }

    

}