package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.repositories.SachRepository;
import com.bookstore.book_sell_service.repositories.SachSearchRepository;
import com.bookstore.book_sell_service.search.SachDocument;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchSyncService implements CommandLineRunner {

    SachSearchRepository sachSearchRepository;
    SachRepository sachRepository; // JPA Repo
    ElasticsearchOperations elasticsearchOperations;

    /**
     * Đồng bộ 1 cuốn sách
     */
    public void syncSach(Long maSach) {
        Sach sach = sachRepository.findById(maSach)
                .orElse(null);

        if (sach == null) {
            log.warn("book not found with ID: {} to sync.", maSach);
            return;
        }

        SachDocument sachDocument = SachDocument.fromEntity(sach);
        sachSearchRepository.save(sachDocument);
        log.info("book synchronized {} to Elasticsearch.", sach.getTenSach());
    }

    /**
     * Xóa 1 cuốn sách khỏi index
     */
    public void deleteSachFromIndex(Long maSach) {
        sachSearchRepository.deleteById(maSach);
        log.info("deleted book with ID: {} khỏi Elasticsearch.", maSach);
    }

    /**
     * Chạy khi ứng dụng khởi động để đồng bộ toàn bộ CSDL
     * (Chỉ chạy lần đầu hoặc khi cần thiết)
     */
    @Override
    public void run(String... args) throws Exception {
        // Lấy đối tượng quản lý index
        var indexOps = elasticsearchOperations.indexOps(SachDocument.class);

        // Bước 1: Kiểm tra xem index đã tồn tại chưa
        if (indexOps.exists()) {
            log.info("Index [sach_index] existed.");

            // Bước 2: Kiểm tra xem có dữ liệu không
            if (sachSearchRepository.count() > 0) {
                log.info("Elasticsearch already has data. skip the sync.");
                return;
            }
            // Index tồn tại nhưng không có dữ liệu, tiến hành đồng bộ
            log.info("Index đã tồn tại nhưng rỗng. Bắt đầu đồng bộ...");

        } else {
            // Bước 3: Nếu index chưa tồn tại, tạo mới với mapping (và analyzer)
            log.warn("Index [sach_index] không tồn tại. Đang tạo mới...");
            try {
                // Đây là nơi file analyzer.json được áp dụng
                indexOps.createWithMapping();
                log.info("Tạo index [sach_index] thành công.");
            } catch (Exception e) {
                log.error("Không thể tạo index [sach_index]", e);
                // Dừng lại nếu không thể tạo index
                return;
            }
        }

        // Bước 4: Nếu code chạy đến đây, chúng ta cần đồng bộ
        log.info("Bắt đầu đồng bộ toàn bộ Sách từ MySQL sang Elasticsearch...");

        Iterable<Sach> allSach = sachRepository.findAll();
        var documents = StreamSupport.stream(allSach.spliterator(), false)
                .map(SachDocument::fromEntity)
                .collect(Collectors.toList());

        if (!documents.isEmpty()) {
            sachSearchRepository.saveAll(documents);
            log.info("Đã đồng bộ thành công {} cuốn sách.", documents.size());
        } else {
            log.info("Không có sách nào trong MySQL để đồng bộ.");
        }
    }
}