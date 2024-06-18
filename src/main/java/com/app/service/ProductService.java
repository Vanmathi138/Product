package com.app.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.app.entity.Product;
import com.app.repository.ProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repo;
	
	@PostConstruct
	public void initDB() {
		Random random = new Random();
		List<Product> products = IntStream.rangeClosed(1, 30)
				.mapToObj(i -> new Product("product" + i, random.nextInt(100), random.nextInt(5000)))
				.collect(Collectors.toList());
		repo.saveAll(products);
	}

}
