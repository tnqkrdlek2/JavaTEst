package com.ges.check.service.impl;


import java.util.List;
import java.util.Map;

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
public class BatchServiceImpl implements BatchService {

    @Autowired
    private GenreDao mGenreDao;
    @Autowired
    private BookDao mBookDao;

    @Autowired
    private BookService mBookService;

    @Autowired
    private NaruService mNaruService;

    int PageSize = 10;
    Gson gson = new Gson();

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeBatch(Map<String, Object> param) throws Exception {
         Map<String, Object> resultMap = Maps.newHashMap();

        BookList();
        return resultMap;
    }

    private Map<String, Object> BookList() {
         RestTemplate restTemplate = new RestTemplate();
         List<Map<String, Object>> genreIndex = mGenreDao.selectGenre(null);

        Map<String, Object> genreMap = Maps.newHashMap();
        genreIndex.forEach(genre -> {
            String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);

            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            @SuppressWarnings("all")
            Map<String, Object> result = gson.fromJson(response.getBody().toString(), Map.class);

            @SuppressWarnings("all")
            Map<String, Object> responseMap = (Map) result.get("response");

            @SuppressWarnings("all")
            List<Map<String, Object>> bookList = (List) responseMap.get("docs");

            List<Map<String, Object>> bookItem = Lists.newArrayList();

            List<Map<String,Object>> selectbook = mBookDao.selectBook(genreMap);

            for (int i = 0; i < bookList.size(); i++) {
                Map<String, Object> doc = bookList.get(i);
                @SuppressWarnings("all")
                Map<String, Object> book = (Map) doc.get("doc");
                book.put("genre", genre.get("idx").toString());
                bookItem.add(book);
                genreMap.put("bookItem", bookItem);
            }
            
            mBookDao.insertBook(genreMap);

            if(selectbook.size() != 0){
                List<Map<String, Object>> cmsBookList = Lists.newArrayList();
                // //mBookDao.deleteRank(); 
                for (String key : genreMap.keySet()){
                    @SuppressWarnings("all")
                    List<Map<String,Object>> list = (List) genreMap.get(key);
                    
                    list.forEach( book-> {
                        book.put("rank", book.get("ranking"));
                        
                    });
                    
                for(String item: genreMap.keySet()){
                    @SuppressWarnings("all")
                    List<Map<String,Object>> list2 =(List) genreMap.get(item);

                    list2.forEach( book2 ->{
                        
                        for (int i = 0; i < selectbook.size(); i++) {
                            if(selectbook.size() == 100){
                                Map<String, Object> bookMap = selectbook.get(i);
                                Map<String, Object> insertMap = Maps.newHashMap();
                                insertMap.put("book_no", bookMap.get("no"));
                                insertMap.put("rank", book2.get("rank"));
                                insertMap.put("genre", bookMap.get("genre"));
                                cmsBookList.add(insertMap);
                                break;
                            }
                        }
                                Map<String, Object> insertParamMap = Maps.newHashMap();
                                insertParamMap.put("cmsBookList", cmsBookList);
                                mBookDao.insertRank(insertParamMap);
                    });
                    // Map<String,Object> deleteRank = Maps.newHashMap();
                    // deleteRank.put("genre", item);
                    // mBookDao.deleteRank(deleteRank);
                    }
                }
            }
        });
        // try {
        //     CmsBatchProcess("bookItem", genreMap);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // //System.out.println("genreMap==================="+ genreMap);
        return genreMap;
    }

    // private void CmsBatchProcess(String recommedType, Map<String, Object> BookResult) throws Exception {
    //     //System.out.println("BookResult ========a=a=a=a=a=:" + BookResult);
    //     for (String key : BookResult.keySet()) {
    //         @SuppressWarnings("all")
    //         List<Map<String, Object>> BookList = (List) BookResult.get(key);
            
    //         BookList.forEach(book -> {
    //                 book.put("isbn13", book.get("isbn13"));
    //                 try {
    //                     Map<String, Object> selectbook = mBookService.searchIsbn(book);
    //                     //System.out.println("=========== bookMap ========================\n"+ book +"\n===========================" );
    //                     book.put("book_no", selectbook.get("no"));
    //                     book.put("genre", selectbook.get("genre"));
    //                 } catch (Exception e) {
    //                     e.printStackTrace();
    //                 }
                
    //         });
    //     }

    //     for (String key : BookResult.keySet()) {
    //         @SuppressWarnings("all")
    //         List<Map<String, Object>> BookResultList = (List) BookResult.get(key);
    //         List<Map<String, Object>> cmsBookList = Lists.newArrayList();

    //         for (int i = 0; i < BookResultList.size(); i++) {
    //             Map<String, Object> bookMap = BookResultList.get(i);
    //             Map<String, Object> insertMap = Maps.newHashMap();
    //             insertMap.put("book_no", bookMap.get("book_no"));
    //             insertMap.put("genre", bookMap.get("genre"));
    //             insertMap.put("rank", bookMap.get("ranking"));
    //             cmsBookList.add(insertMap);
    //         }
    //         Map<String, Object> insertParamMap = Maps.newHashMap();
    //         insertParamMap.put("cmsBookList", cmsBookList);
    //         mBookDao.insertRank(insertParamMap);
    //     }
    // }
}