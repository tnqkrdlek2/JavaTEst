package com.ges.check.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ges.check.dao.BookDao;
import com.ges.check.dao.GenreDao;
import com.ges.check.service.BatchService;
import com.ges.check.service.BatchService2;
//import com.ges.check.service.BatchService;
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
        String isbn = param.get("isbn13").toString();
        param.put("isbn13", isbn);

        
        List<Map<String,Object>> bookList = mBookDao.selectBook(param);

        //System.out.println("bookList test =================== :"+bookList);

        Map<String,Object> book = null;
        
            if(bookList.size() == 0){
                mBookDao.insertBook(book);
                
            } else {
                try {
                    book = bookList.get(0);
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

    // json 으로 List 가지고 오기
    // public Map<String, Object> BookList() throws Exception {
    //     RestTemplate restTemplate = new RestTemplate();
    //     Map<String, Object> genreMap = Maps.newHashMap();

    //     List<Map<String, Object>> genreList = mGenreDao.selectGenre(null);
       
    //         genreList.forEach(genre -> {
                // String address = mNaruService.loanItemSrch(genre.get("idx").toString(),
                // PageSize);
                // String address = mNaruService.loanItemSrch(genre.get("idx").toString(), PageSize);
                // ResponseEntity<?> response = restTemplate.exchange(address, HttpMethod.GET, null, String.class);

                // @SuppressWarnings("all") // 컴파일러가 경고 하는 내용중 제외 하고자 할때 사용 (all 모든 경고 억제)
                // Map<String, Object> result = gson.fromJson(response.getBody().toString(), Map.class);

                // @SuppressWarnings("all") // json 에서 가져온 "response 부분 MAP 넣기 최상단"
                // Map<String, Object> responseMap = (Map) result.get("response");
                // @SuppressWarnings("all") // Map 에 있는 "docs (docs": [{doc:}]) 넣기 중단 "
                // List<Map<String, Object>> bookList = (List) responseMap.get("docs");
                // List<Map<String, Object>> bookListitem = Lists.newArrayList();

                // for (int i = 0; i < bookList.size(); i++) {
                //     Map<String, Object> doc = bookList.get(i);
                //     @SuppressWarnings("all") // Map doc 가지고 온것 for 문으로 돌리기
                //     Map<String, Object> book = (Map) doc.get("doc");
                //     book.put("genre", genre.get("idx").toString());
                //     bookListitem.add(book);
                // }

                // genreMap.put("bookListitem", bookListitem);
                // mBookDao.insertRank(genreMap);
                //mBookDao.insertBook(genreMap);

            //});
        // try {
        //     CmsBatchProcess("bookListitem",genreMap);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    //     return genreMap;
    // }
    // private void CmsBatchProcess(String recommendType, Map<String,Object> BookResult){
        
             // rank 작업  
    //         System.out.println("recommedType ==== : " + recommendType);
    //         if(recommendType.equals("bookItem")) {
    //             for(String key : BookResult.keySet()){
    //                 @SuppressWarnings("all")            
    //                 List<Map<String,Object>> BookResultList = (List) BookResult.get(key);
    //                 System.out.println("key " + key + "value" + BookResult.values());
    //                 List<Map<String,Object>> cmsBookList = Lists.newArrayList();

    //                 for(int i = 0; i< BookResultList.size(); i++) {
                        
    //                     Map<String,Object> bookMap = BookResultList.get(i);
    //                     Map<String,Object> insertMap = Maps.newHashMap();

    //                     insertMap.put("book_no", bookMap.get("no"));
    //                     insertMap.put("rank",i+1);
    //                     insertMap.put("genre", key);
    //                     cmsBookList.add(insertMap);
    //                 }
    //                 Map<String,Object> insertParamMap = Maps.newHashMap();
    //                 System.out.println("cms Book List :"+ cmsBookList);
    //                 insertParamMap.put("cmsBookList", cmsBookList);
    //                 mBookDao.insertRank(insertParamMap);
    //             }
    //     }
    //}

}