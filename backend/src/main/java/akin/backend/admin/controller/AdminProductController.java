package akin.backend.admin.controller;

import akin.backend.product.dto.request.CreateProductRequest;
import akin.backend.product.dto.request.UpdateProductRequest;
import akin.backend.product.dto.response.ProductResponse;
import akin.backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductResponse> create(
            Authentication auth,
            @RequestPart("product") @Valid CreateProductRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(productService.create(adminId, request, photo));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponse> update(
            Authentication auth,
            @PathVariable Long id,
            @RequestPart("product") @Valid UpdateProductRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        Long adminId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(productService.update(adminId, id, request, photo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        Long adminId = (Long) auth.getPrincipal();
        productService.delete(adminId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }
}
