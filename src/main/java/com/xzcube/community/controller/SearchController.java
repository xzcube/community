package com.xzcube.community.controller;

import com.xzcube.community.service.ElasticsearchService;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/6/11 10:37
 *
 * 搜索功能整合了elasticsearch 但是加上去的话与前端的交互改动太大，可以用postman模拟实现
 */
//@Controller
public class SearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @PostMapping("/search")
    @ResponseBody
    public SearchHits search(@RequestParam("keyWord") String keyWord,
                         @RequestParam("page") Integer page,
                         @RequestParam("size") Integer size) throws IOException {
        SearchHits hits = elasticsearchService.searchQuestion(keyWord, page, size);
        return hits;
    }
}
