package com.ges.check.service.impl;

import java.util.List;
import java.util.Map;

import com.ges.check.dao.BookDao;
import com.ges.check.dao.GenreDao;
import com.ges.check.service.BatchService;
import com.ges.check.service.BatchService2;
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
public class BatchServiceImpl2 implements BatchService2 {

    @Autowired
    private GenreDao mGenreDao;
    @Autowired
    private BookDao mBookDao;

    @Autowired
    private BookService mBookService;

    @Autowired
    private NaruService mNaruService;

    Gson gson = new Gson();

    int PAGE_SIZE = 10;


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeBatch(Map<String, Object> param) throws Exception {
        Map<String, Object> resultMap = Maps.newHashMap();

        genreProgress();

        return resultMap;
    }

    private Map<String,Object> genreProgress() {
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String,Object>> genreIndex = mGenreDao.selectGenre(null);

        Map<String,Object> genreMap = Maps.newHashMap();

        genreIndex.forEach(genre ->{

            String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PAGE_SIZE);

            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            @SuppressWarnings("all")
            Map<String,Object> result = gson.fromJson(response.getBody().toString(), Map.class);

            @SuppressWarnings("all")
            Map<String,Object> responseMap = (Map) result.get("response");

            @SuppressWarnings("all")
            List<Map<String,Object>> bookList = (List) result.get("dosc");

            List<Map<String,Object>> bookArrList = Lists.newArrayList();
            for(int i = 0; i < bookList.size(); i++) {
                Map<String,Object> doc = bookList.get(i);
                
                @SuppressWarnings("all")
                Map<String,Object> book = (Map)doc.get("doc");
                book.put("genre",genre.get("idx").toString());    
                bookArrList.add(book);
            }
                genreMap.put("bookItem",bookArrList);            
        });
        try {
            cmsBatchProcess("genre",genreMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreMap;
    }

    private void cmsBatchProcess(String recommedType, Map<String,Object> BookResult ) throws Exception {

        for(String key: BookResult.keySet()){
            @SuppressWarnings("all")
            List<Map<String,Object>> BookResultList = (List) BookResult.get(key);
            
            BookResultList.forEach(ResultList ->{
                ResultList.put("isbn13", ResultList.get("Isbn13"));
                System.out.println("ResultListIsbn13 =============== :" + ResultList.get("Isbn13"));
                System.out.println("ResultList =============== :" + ResultList);

                try {
                    Map<String,Object> completeBook = mBookService.searchIsbn(ResultList);

                    ResultList.put("book_no", completeBook.get("no"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if(recommedType.equals("genre")){
            for(String key : BookResult.keySet()){

                @SuppressWarnings("all")
                List<Map<String,Object>> genreBookList = (List) BookResult.get(key);
                List<Map<String,Object>> cmsBookList = Lists.newArrayList();
                for(int i =0; i < genreBookList.size(); i++) {
                    Map<String,Object> genreBook = genreBookList.get(i);
                    Map<String,Object> insertParamMap = Maps.newHashMap();
                    insertParamMap.put("book_no", genreBook.get("book_no"));
                    insertParamMap.put("genre",key);
                    insertParamMap.put("rank", i+1);
                    cmsBookList.add(insertParamMap);
                }

                Map<String,Object> insertParamMap = Maps.newHashMap();
                insertParamMap.put("cmsBookList",cmsBookList);
                mBookDao.insertRank(insertParamMap);
            }
        }

    }



    
}