package com.app.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public APIResponse<PagedModel<?>> pagination(@PathVariable int offset, @PathVariable int pageSize){
		Page<Product> product = service.findPagination(offset,pageSize);
		PagedModel<?> getProduct = pagedResource.toModel(product);
		return new APIResponse<>(product.getSize(),getProduct);
	}
	
	@GetMapping("/pagination/{page}")
	public APIResponse<PagedModel<?>> getProduct(@PathVariable int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize){
		Page<Product> allProduct = service.findProductBySize(page,pageSize);
		PagedModel<?> pagedModel = pagedResource.toModel(allProduct);
		
		int totalPage = allProduct.getTotalPages();
		List<Link> links = new ArrayList<>();
		links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
				.getProduct(0, pageSize))
				.withRel("first"));
		links.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
						.getProduct(page, pageSize)).withSelfRel());

		if (page < totalPage - 1) {
			links.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
							.getProduct(page + 1, pageSize))
					.withRel("next"));
		}
		if (page > 0) {
			links.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
							.getProduct(page - 1, pageSize))
					.withRel("prev"));
		}
		if (totalPage > 0) {
			links.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
							.getProduct(totalPage - 1, pageSize))
					.withRel("last"));
		}

		PagedModel<?> pagedModelWithLinks = PagedModel
				.of(pagedModel.getContent(), pagedModel.getMetadata(), links);

		return new APIResponse<>(allProduct.getSize(), pagedModelWithLinks);
	}
	@GetMapping("/paginationWithSort/{offset}/{pageSize}/{field}")
	public APIResponse<PagedModel<?>> getPaginationWithSort(@PathVariable int offset,
			@PathVariable int pageSize, @PathVariable String field){
		Page<Product> product = service.findPaginationWithSort(offset,pageSize,field);
		PagedModel<?> getProduct = pagedResource.toModel(product);
		return new APIResponse<>(product.getSize(),getProduct);
	}

}
