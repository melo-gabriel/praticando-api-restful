package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import jakarta.validation.Valid;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@GetMapping(value = "/produtos")
	public ResponseEntity<List<ProductModel>> getAllProduct() {
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
	}

	@GetMapping(value = "/produtos/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") Long id) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("produto nao encontrado");
		}
		return ResponseEntity.status(HttpStatus.OK).body(productO.get());
	}

	@PostMapping("/produtos")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
	}

	@PutMapping("/produtos/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") Long id,
			@RequestBody @Valid ProductRecordDto productRecordDto) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("produto nao encontrado");
		}
		var productModel = productO.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
	}

	@DeleteMapping("/produtos/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") Long id) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("produto nao encontrado");
		}
		productRepository.delete(productO.get());
		return ResponseEntity.status(HttpStatus.OK).body("produto deletado com sucesso");
	}
}
