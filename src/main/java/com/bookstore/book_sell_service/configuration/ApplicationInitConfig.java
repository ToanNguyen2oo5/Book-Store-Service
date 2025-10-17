package com.bookstore.book_sell_service.configuration;

import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(KhachHangRepository khachHangRepository){
    return args -> {
        if(khachHangRepository.findByuserName("admin").isEmpty()){
        var roles = new HashSet<String>();
        roles.add("ADMIN");
            KhachHang khachHang = KhachHang.builder()
                    .userName("admin")
                    .matKhau(passwordEncoder.encode("admin"))
                    .roles(roles)
                    .build();
            khachHangRepository.save(khachHang);
            log.warn("admin user has been created with password: admin");
        }

    };

    }
}
