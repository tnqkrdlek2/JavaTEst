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
    public Map<String, Object> executeBatch(final Map<String, Object> param) throws Exception {
        final Map<String, Object> resultMap = Maps.newHashMap();

        BookList();
        return resultMap;
    }

    private Map<String, Object> BookList() {
        final RestTemplate restTemplate = new RestTemplate();
        final List<Map<String, Object>> genreIndex = mGenreDao.selectGenre(null);

        Map<String, Object> genreMap = Maps.newHashMap();
        genreIndex.forEach(genre -> {
            
            final String url = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);

            final ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            @SuppressWarnings("all")
            final Map<String, Object> result = gson.fromJson(response.getBody().toString(), Map.class);

            @SuppressWarnings("all")
            final Map<String, Object> responseMap = (Map) result.get("response");

            @SuppressWarnings("all")
            final List<Map<String, Object>> bookList = (List) responseMap.get("docs");

            final List<Map<String, Object>> bookItem = Lists.newArrayList();

            for (int i = 0; i < bookList.size(); i++) {
                final Map<String, Object> doc = bookList.get(i);
                @SuppressWarnings("all")
                final Map<String, Object> book = (Map) doc.get("doc");

                book.put("genre", genre.get("idx").toString());
                bookItem.add(book);
            }
            genreMap.put("bookItem", bookItem);
            mBookDao.insertBook(genreMap);
            System.out.println("genreMap ================ : "+ genreMap);
            
        });

        try {
            CmsBatchProcess("genre", genreMap);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return genreMap;
    }

    private void CmsBatchProcess(final String recommedType, final Map<String, Object> BookResult) throws Exception {

        for (final String key : BookResult.keySet()) {
            // System.out.println(key);
            @SuppressWarnings("all")
            final List<Map<String, Object>> BookList = (List) BookResult.get(key);
            // 지금 bookItem 에서 genre = 9 번 값 만 가지고 오고 있음 ...
            // 원하는건 0~ 9 번까지 가지고 와야함

            BookList.forEach(book -> {
                if (book != null) {
                    book.put("isbn13", book.get("isbn13"));
                    try {
                        final Map<String, Object> selectbook = mBookService.searchIsbn(book);
                        // searchIsbn 에서 1~ 100 번까지 검색 후 마지막 100 번 만 return 해주고있음 ..
                        // 원하는건 1 ~ 100 번 까지 순서 대로 no 를 넣어 주는것
                        book.put("book_no", selectbook.get("no"));
                        book.put("genre", selectbook.get("genre"));
                        // System.out.println("selectbook ================= " + selectbook );

                        final Map<String, Object> deleteParamMap = Maps.newHashMap();
                        deleteParamMap.put("genre", selectbook.get("genre"));
                        mBookDao.deleteRank(deleteParamMap);

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (final String key : BookResult.keySet()) {
            @SuppressWarnings("all")
            final List<Map<String, Object>> BookResultList = (List) BookResult.get(key);
            final List<Map<String, Object>> cmsBookList = Lists.newArrayList();

            for (int i = 0; i < BookResultList.size(); i++) {
                final Map<String, Object> bookMap = BookResultList.get(i);
                final Map<String, Object> insertMap = Maps.newHashMap();
                insertMap.put("book_no", bookMap.get("book_no"));
                insertMap.put("genre", bookMap.get("genre"));
                insertMap.put("rank", bookMap.get("ranking"));
                cmsBookList.add(insertMap);
            }
            final Map<String, Object> insertParamMap = Maps.newHashMap();
            insertParamMap.put("cmsBookList", cmsBookList);
            mBookDao.insertRank(insertParamMap);
        }
    }
}