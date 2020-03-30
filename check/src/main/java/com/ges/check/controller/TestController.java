package com.ges.check.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ges.check.service.BatchService;
import com.ges.check.service.BookService;
import com.ges.check.service.CheckService;
import com.ges.check.service.NaruService;
import com.google.common.collect.Maps;


import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @Autowired
    private CheckService mCheckService;

    @Autowired
    private BatchService mBatchService;
    @Autowired
    private BookService mBookService;

    @RequestMapping("/test")
    public String test() {

        final List<String> list = new ArrayList<>();
        list.add("a");
        list.add("p");
        list.add("p");
        list.add("l");
        list.add("e");

        list.forEach(x -> {
            System.out.println(x);
        });
        return "Hello World";
    }

    @RequestMapping(value = "/hello/{name}")
    String hello(@PathVariable  String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping(value = "/test/list")
    public ResponseEntity<?> allList(@RequestParam  Map<String, Object> param) throws Exception {
        try {
            return new ResponseEntity<>(mCheckService.selectList(param), HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "/test/insert")
    public ResponseEntity<?> insert(@RequestBody  Map<String, Object> param) throws Exception {

        System.out.println(param);
        try {
            return new ResponseEntity<>(mCheckService.insertBoard(param), HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // mCheckService.insertBoard(param);
        // return null;
    }

    @PutMapping(value = "/test/update")
    public ResponseEntity<?> update( Map<String, Object> param) {
        try {
            return new ResponseEntity<>(mCheckService.updateBoard(param), HttpStatus.OK);

        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/test/delete")
    public ResponseEntity<?> delete( Map<String, Object> param) {
        try {
            return new ResponseEntity<>(mCheckService.deleteBoard(param), HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "/test/bookUpdate")
    public ResponseEntity<?> bookUpdate(@RequestParam  Map<String, Object> param) throws Exception {
        System.out.println("book UPdaTE : >>>> " + param);

        try {
            return new ResponseEntity<>(mBookService.updateBook(param), HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/test/bookSelect")
    public ResponseEntity<?> bookJson(@RequestParam  Map<String, Object> param) {

        try {
            return new ResponseEntity<>(mBookService.selectBookList(param), HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/test/bookIsbn")
    public ResponseEntity<?> bookIsbn(@RequestParam  Map<String, Object> param) throws Exception {
        System.err.println("bookIsbn");
        try {
            return new ResponseEntity<>(mBookService.selectBookIsbn13(param), HttpStatus.OK);
        } catch (final Exception e) {
            // e.printStackTrace();
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/test/bookRankInsert")
    public ResponseEntity<?> bookRankInsert(@RequestParam  Map<String, Object> param) throws Exception {
        System.out.println("0000000000000000000000 book Rank Insert Controller");
        try {
            return new ResponseEntity<>(mBookService.insertRank(param), HttpStatus.OK);
            // return new ResponseEntity<>(mBatchService.executeBatch(param),HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/test/bookInsert")
    public ResponseEntity<?> bookInsert(@RequestParam  Map<String, Object> param) throws Exception {
        System.out.println("00000000000000 book insert controller");
        try {
            Map<String, Object> resultMap = Maps.newHashMap();
            resultMap.put("book", mBookService.insertBook(param));
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        } catch (final Exception e) {
            //e.printStackTrace();
           return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return null;
    }

    @GetMapping(value = "/test/bookJson")
    public ResponseEntity<?> MysqlJson(@RequestParam Map<String,Object> param) throws Exception{

        try {
            return new ResponseEntity<>(mBookService.selectJson(param), HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
        
}
        
        




