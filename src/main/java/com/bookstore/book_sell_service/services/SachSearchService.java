package com.bookstore.book_sell_service.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.bookstore.book_sell_service.search.SachDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SachSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     *  (Tìm kiếm gần đúng - Fuzzy Search)
     * Yêu cầu 1: "toiet" -> "toilet" (Tìm kiếm gần đúng - Fuzzy Search)
     */
    public List<SachDocument> searchFuzzy(String term) {
        // Tạo một truy vấn "match" với tùy chọn fuzziness
        Query query = co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.of(m -> m
                .field("tenSach") // Tìm trên trường tenSach
                .query(term)
                .fuzziness("AUTO") // Đây chính là chìa khóa!
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<SachDocument> hits = elasticsearchOperations.search(nativeQuery, SachDocument.class);
        return hits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /**
     * Yêu cầu 2: "oil" -> "boil", "toilet", "oil" (Gợi ý / Autocomplete)
     */
    public List<SachDocument> searchAutocomplete(String term) {
        // Dùng `match_phrase_prefix` để có hiệu quả gợi ý tốt nhất
        Query query = co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery.of(m -> m
                .field("tenSach") // Tìm trên trường tenSach đã được analyze
                .query(term)
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<SachDocument> hits = elasticsearchOperations.search(nativeQuery, SachDocument.class);
        return hits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
