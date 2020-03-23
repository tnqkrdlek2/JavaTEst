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

    final int PageSize = 40;

    @Override
    public Map<String, Object> insertBook(Map<String, Object> param) throws Exception {
        System.out.println("1111111111111 serviceInpl insertbOok");
        // Map<String,Object> book = mBatchService.executeBatch(param);
        Map<String, Object> book = BookList();
        try {
            mBookDao.insertBook(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Map<String, Object> insertRank(Map<String, Object> param) throws Exception {
        System.out.println("insertRank");

        return null;
    }

    @Override
    public Map<String, Object> selectBookIsbn13(Map<String, Object> param) throws Exception {
        // System.out.println("isbn13 :"+ param);
        return mBookDao.selectBookIsbn13(param);
    }

    @Override
    public List<Map<String, Object>> selectBookList(Map<String, Object> param) throws Exception {

        Map<String, Object> bookMap = mBatchService.executeBatch(param);

        List<Map<String, Object>> list = mBookDao.selectBook(bookMap);
        // System.out.println("select Book List : >> "+list);
        return list;
    }

    @Override
    public Map<String, Object> updateBook(Map<String, Object> param) throws Exception {
        return mBookDao.updateBook(param);
    }

    // json 으로 List 가지고 오기
    public Map<String, Object> BookList() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> genreMap = Maps.newHashMap();

        List<Map<String, Object>> genreList = mGenreDao.selectGenre(null);
        try {
            genreList.forEach(genre -> {
                // String address = mNaruService.loanItemSrch(genre.get("idx").toString(),
                // PageSize);
                String address = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);
                ResponseEntity<?> response = restTemplate.exchange(address, HttpMethod.GET, null, String.class);

                @SuppressWarnings("all") // 컴파일러가 경고 하는 내용중 제외 하고자 할때 사용 (all 모든 경고 억제)
                Map<String, Object> result = gson.fromJson(response.getBody().toString(), Map.class);

                @SuppressWarnings("all") // json 에서 가져온 "response 부분 MAP 넣기 최상단"
                Map<String, Object> responseMap = (Map) result.get("response");
                @SuppressWarnings("all") // Map 에 있는 "docs (docs": [{doc:}]) 넣기 중단 "
                List<Map<String, Object>> bookList = (List) responseMap.get("docs");
                List<Map<String, Object>> bookListitem = Lists.newArrayList();

                for (int i = 0; i < bookList.size(); i++) {
                    Map<String, Object> doc = bookList.get(i);
                    @SuppressWarnings("all") // Map doc 가지고 온것 for 문으로 돌리기
                    Map<String, Object> book = (Map) doc.get("doc");
                    book.put("genre", genre.get("idx").toString());
                    bookListitem.add(book);
                }

                genreMap.put("bookListitem", bookListitem);
                //mBookDao.insertRank(genreMap);
                mBookDao.insertBook(genreMap);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // API 호출은 정상적으로 했지만, API 서버 유지보수나 시스템 오류로 인한 에러가 발생하였을 경우
        return genreMap;

    }

}