package com.ges.check.service.impl;

import com.ges.check.service.NaruService;
import com.ges.check.util.Constants;

import org.springframework.stereotype.Service;

@Service
public class NaruServiceImpl implements NaruService {

    @Override
    public String loanItemSrch(String kdc, int PageSize) {

        StringBuilder sb = new StringBuilder();
        sb.append(Constants.Naru.NARU_API_URL);
        sb.append("loanItemSrch?format=json");
        sb.append("&authKey=" + Constants.Naru.NARU_API_KEY);
        sb.append("&kdc=" + kdc);
        sb.append("&pageSize=" + PageSize);
        return sb.toString();
        

    }
    @Override
    public String loanItemSrch(String kdc) {

        StringBuilder sb = new StringBuilder();
        sb.append(Constants.Naru.NARU_API_URL);
        sb.append("loanItemSrch?format=json");
        sb.append("&authKey=" + Constants.Naru.NARU_API_KEY);
        sb.append("&kdc=" + kdc);
        sb.append("&pageSize=2" );
        return sb.toString();

    }
}