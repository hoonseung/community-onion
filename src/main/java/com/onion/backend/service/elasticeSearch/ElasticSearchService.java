package com.onion.backend.service.elasticeSearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public Mono<String> getDocumentById(String index, String id) {
        return webClient
            .get()
            .uri("/{index}/_doc/{id}", index, id)
            .retrieve()
            .bodyToMono(String.class);
    }


    public Mono<String> indexPostDocument(String index, String id, String document) {
        return webClient
            .post()
            .uri("/{index}/_doc/{id}", index, id)
            .bodyValue(document)
            .retrieve()
            .bodyToMono(String.class);
    }


    public Mono<List<String>> articleSearchByQueryKeyword(String index, String requestQuery) {
        return webClient
            .post()
            .uri("/{index}/_search", index)
            .bodyValue(requestQuery)
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(this::extractIds);
    }


    private Mono<List<String>> extractIds(String responseBody) {
        return Mono.fromCallable(() -> {
            List<String> ids = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode hitsNode = rootNode.path("hits").path("hits");

            if (hitsNode.isArray()) {
                for (JsonNode hit : hitsNode) {
                    String id = hit.path("_id").asText();
                    ids.add(id);
                }
            }

            return ids;
        });
    }
}



