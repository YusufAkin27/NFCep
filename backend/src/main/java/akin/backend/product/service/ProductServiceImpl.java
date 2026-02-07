package akin.backend.product.service;

import akin.backend.product.dto.request.CreateProductRequest;
import akin.backend.product.dto.request.UpdateProductRequest;
import akin.backend.product.dto.response.ProductResponse;
import akin.backend.product.entity.Product;
import akin.backend.product.exception.ProductNotFoundException;
import akin.backend.product.repository.ProductRepository;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    private void ensureAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.ADMIN) {
            throw new UserNotAdminException();
        }
    }

    @Override
    @Transactional
    public ProductResponse create(Long adminUserId, CreateProductRequest request, MultipartFile photo) {
        ensureAdmin(adminUserId);
        String imageUrl = photo != null && !photo.isEmpty() ? cloudinaryService.upload(photo) : null;
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .inStock(request.getInStock() != null ? request.getInStock() : true)
                .imageUrl(imageUrl)
                .price(request.getPrice())
                .build();
        product = productRepository.save(product);
        log.info("Ürün oluşturuldu: {}", product.getName());
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long adminUserId, Long productId, UpdateProductRequest request, MultipartFile photo) {
        ensureAdmin(adminUserId);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getInStock() != null) product.setInStock(request.getInStock());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (photo != null && !photo.isEmpty()) {
            product.setImageUrl(cloudinaryService.upload(photo));
        }
        product = productRepository.save(product);
        log.info("Ürün güncellendi: {}", product.getName());
        return toResponse(product);
    }

    @Override
    @Transactional
    public void delete(Long adminUserId, Long productId) {
        ensureAdmin(adminUserId);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
        log.info("Ürün silindi: {}", product.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .inStock(product.isInStock())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
