package com.xzcube.community.service;

import org.elasticsearch.search.SearchHits;

import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/6/11 9:48
 */
public interface ElasticsearchService {
    SearchHits searchQuestion(String keyWord, int current, int limit) throws IOException;
}
