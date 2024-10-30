package com.onion.backend.dto.elasticeSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onion.backend.dto.elasticeSearch.ElasticSearchRequestQuery.SearchQuery.QueryBoolean;
import java.util.List;

/**
 * elastic search 검색 api 요청 쿼리
 * @param query : 검색 쿼리
 * @param ids : 검색 결과 필드
 * @param size : 검색 결과 개수
 */
public record ElasticSearchRequestQuery(
    @JsonProperty("query")
    SearchQuery query,
    @JsonProperty("fields")
    List<String> ids,
    int size
) {


    public static ElasticSearchRequestQuery of(Long boardId, String keyword) {
        return new ElasticSearchRequestQuery(
            new SearchQuery(
                new QueryBoolean(
                    List.of(
                            new RootTerm(new Term(boardId)),
                            new RootMatch(new Match(keyword))
                    )
                )
            ), List.of("_id"), 10
        );
    }


    record SearchQuery(
        @JsonProperty("bool")
        QueryBoolean queryBoolean
    ) {

        record QueryBoolean(
            @JsonProperty("must")
            List<Object> musts
        ) {

        }
    }


    record RootTerm(
        @JsonProperty("term")
        Term term
    ) {}

    record RootMatch(
        @JsonProperty("match")
        Match match
    ) {}

    record Term(
        @JsonProperty("board.id")
        Long boardId
    ) {}

    record Match(
        @JsonProperty("title")
        String title
    ) {}


}

