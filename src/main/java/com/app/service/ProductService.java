package com.app.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entity.Product;
import com.app.repository.ProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repo;

	public List<Product> getAllProducts() {
		return repo.findAll();
	}

	public List<Product> findByField(String field) {
		return repo.findAll(Sort.by(Sort.Direction.ASC,field));
	}

	public Page<Product> findProductBySize(int offset, int pageSize) {
		return repo.findAll(PageRequest.of(offset, pageSize));
			
	}

	public Page<Product> findPaginationWithSort(int offset, int pageSize, String field) {
		return repo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
	}
	
/*	@PostConstruct
	public void initDB() {
		Random random = new Random();
		List<Product> products = IntStream.rangeClosed(1, 30)
				.mapToObj(i -> new Product("product" + i, random.nextInt(100), random.nextInt(5000)))
				.collect(Collectors.toList());
		repo.saveAll(products);
	}*/

}
