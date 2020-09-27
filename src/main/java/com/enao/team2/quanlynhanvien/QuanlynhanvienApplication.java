package com.enao.team2.quanlynhanvien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class QuanlynhanvienApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanlynhanvienApplication.class, args);
	}

}
