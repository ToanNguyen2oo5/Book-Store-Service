package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.search.SachDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SachSearchRepository extends ElasticsearchRepository<SachDocument,Long> {
}
