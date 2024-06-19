package com.app.controller;
import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.APIResponse;
import com.app.entity.Product;
import com.app.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
	private final ProductService service;
	private final PagedResourcesAssembler<Product> pagedResource;
	
	@GetMapping("/getAll")
	public APIResponse<List<Product>> getAll(){
		List<Product> allProducts = service.getAllProducts();
		return new APIResponse<>(allProducts.size(),allProducts);
	}
	@GetMapping("/get-{field}")
	public APIResponse<List<Product>> getByField(@PathVariable String field){
		List<Product> product = service.findByField(field);
		return new APIResponse<>(product.size(),product);
	}
	@GetMapping("/pagination/{offset}/{pageSize}")
	public APIResponse<PagedModel<?>> getProduct(@PathVariable int offset, @PathVariable int pageSize){
		Page<Product> allProduct = service.findProductBySize(offset,pageSize);
		PagedModel<?> pagedModel = pagedResource.toModel(allProduct);
		return new APIResponse<>(allProduct.getSize(),pagedModel);
	}
	@GetMapping("/paginationWithSort/{offset}/{pageSize}/{field}")
	public APIResponse<PagedModel<?>> getPaginationWithSort(@PathVariable int offset,
			@PathVariable int pageSize, @PathVariable String field){
		Page<Product> product = service.findPaginationWithSort(offset,pageSize,field);
		PagedModel<?> getProduct = pagedResource.toModel(product);
		return new APIResponse<>(product.getSize(),getProduct);
	}

}
