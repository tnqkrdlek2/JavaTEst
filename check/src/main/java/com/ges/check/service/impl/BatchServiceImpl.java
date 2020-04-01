package com.ges.check.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ges.check.dao.BookDao;
import com.ges.check.dao.GenreDao;
import com.ges.check.service.BatchService;
import com.ges.check.service.BookService;
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
    private BookDao mBookDao;

    @Autowired
    private BookService mBookService;

    @Autowired
    private NaruService mNaruService;

    final int PageSize = 10;
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

        // 정보나루 정보 가지고오기 (가지고 오기 / insert / isbn13 비교 따로 만들기 )
        Map<String, Object> genreMap = Maps.newHashMap();
        genreIndex.forEach(genre -> {

            String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);
            // System.out.println(url);
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            @SuppressWarnings("all")
            Map<String, Object> result = gson.fromJson(response.getBody().toString(), Map.class);

            @SuppressWarnings("all")
            Map<String, Object> responseMap = (Map) result.get("response");

            @SuppressWarnings("all")
            List<Map<String, Object>> bookList = (List) responseMap.get("docs");

            List<Map<String, Object>> bookItem = Lists.newArrayList();
            for (int i = 0; i < bookList.size(); i++) {
                Map<String, Object> doc = bookList.get(i);
                @SuppressWarnings("all")
                Map<String, Object> book = (Map) doc.get("doc");

                //book = new HashMap<>();
                book.put("genre", genre.get("idx").toString());
                bookItem.add(book);
                System.out.println("genreMap ========== : "+book);
            }
            genreMap.put("bookItem", bookItem);
            mBookDao.insertBook(genreMap);
        });

        // 정보나루 값 가지고오기 end

        // DB 정보 비교 해서 rank 변경
        try {
            CmsBatchProcess("genre", genreMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genreMap;
    }

    private void CmsBatchProcess(String recommedType, Map<String, Object> BookResult) throws Exception {

        for(String key : BookResult.keySet()){
            //System.out.println(key);
            @SuppressWarnings("all")
            List<Map<String,Object>> BookList = (List) BookResult.get(key);
            
            BookList.forEach(book ->{
                if(book != null){
                    book.put("isbn13", book.get("isbn13"));
                    try {
                        Map<String,Object> selectbook = mBookService.searchIsbn(book);
                        book.put("book_no",selectbook.get("no"));
                        book.put("genre", selectbook.get("genre"));
                        //System.out.println("selectbook ================= " + selectbook );

                        Map<String,Object> deleteParamMap = Maps.newHashMap();
                        deleteParamMap.put("genre", selectbook.get("genre"));
                        mBookDao.deleteRank(deleteParamMap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
            

        for(String key: BookResult.keySet()){
            @SuppressWarnings("all")
            List<Map<String,Object>> BookResultList = (List) BookResult.get(key);
            List<Map<String,Object>> cmsBookList = Lists.newArrayList();

            for(int i = 0; i< BookResultList.size(); i++){
                Map<String,Object> bookMap = BookResultList.get(i);
                Map<String,Object> insertMap = Maps.newHashMap();
                insertMap.put("book_no",bookMap.get("book_no"));
                insertMap.put("genre",bookMap.get("genre"));
                insertMap.put("rank", bookMap.get("ranking"));
                cmsBookList.add(insertMap);
            }
            Map<String,Object> insertParamMap = Maps.newHashMap();
            insertParamMap.put("cmsBookList", cmsBookList);
            mBookDao.insertRank(insertParamMap);
        }
    }
}