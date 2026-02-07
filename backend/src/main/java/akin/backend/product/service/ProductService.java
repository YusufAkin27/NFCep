package akin.backend.product.service;

import akin.backend.product.dto.request.CreateProductRequest;
import akin.backend.product.dto.request.UpdateProductRequest;
import akin.backend.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductResponse create(Long adminUserId, CreateProductRequest request, MultipartFile photo);

    ProductResponse update(Long adminUserId, Long productId, UpdateProductRequest request, MultipartFile photo);

    void delete(Long adminUserId, Long productId);

    Page<ProductResponse> findAll(Pageable pageable);
}
