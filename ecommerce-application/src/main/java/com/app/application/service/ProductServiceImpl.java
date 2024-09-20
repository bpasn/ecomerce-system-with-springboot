package com.app.application.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.application.dto.ApiResponse;
import com.app.application.dto.CategoriesDTO;
import com.app.application.dto.ProductOptionDTO;
import com.app.application.dto.ProductsDTO;
import com.app.application.helper.FileManagement;
import com.app.application.interfaces.ProductService;
import com.app.application.mapper.ProductMapper;
import com.app.application.mapper.StockMapper;
import com.app.domain.entity.ProductCategoriesEntity;
import com.app.domain.entity.ProductEntity;
import com.app.domain.entity.ProductImageEntity;
import com.app.domain.entity.ProductOptionEntity;
import com.app.domain.entity.StockEntity;
import com.app.domain.entity.StoreEntity;
import com.app.domain.usecase.ProductCategoryUseCase;
import com.app.domain.usecase.ProductImageUseCase;
import com.app.domain.usecase.ProductOptionUseCase;
import com.app.domain.usecase.ProductUseCase;
import com.app.domain.usecase.StockUseCase;
import com.app.domain.usecase.StoreUseCase;
import com.app.infrastructure.exception.BaseException;
import com.app.infrastructure.exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductEntity, ProductsDTO> implements ProductService {

    @Value("${mount-path}")
    private String mountPath;
    private final ProductUseCase productUseCase;
    private final ProductCategoryUseCase categoryUseCase;
    private final ProductOptionUseCase productOptionUseCase;
    private final ProductImageUseCase productImageUseCase;
    private final StockUseCase stockUseCase;
    private final ProductMapper productMapper;
    private final StoreUseCase storeUseCase;
    private final FileManagement fileManagement;

    public ProductServiceImpl(
            ProductUseCase productUseCase,
            ProductImageUseCase productImageUseCase,
            ProductMapper productMapper,
            StockUseCase stockUseCase,
            ProductCategoryUseCase categoryUseCase,
            ProductOptionUseCase productOptionUseCase,
            StoreUseCase storeUseCase,
            FileManagement fileManagement) {
        super(productUseCase, productMapper, ProductEntity.class);
        this.productUseCase = productUseCase;
        this.productImageUseCase = productImageUseCase;
        this.stockUseCase = stockUseCase;
        this.categoryUseCase = categoryUseCase;
        this.productOptionUseCase = productOptionUseCase;
        this.productMapper = productMapper;
        this.storeUseCase = storeUseCase;
        this.fileManagement = fileManagement;

    }

    @Override
    public ProductsDTO getByName(String name) {
        return ProductMapper.INSTANCE.toDTO(productUseCase.findByNameTH(name).orElse(null));
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public ApiResponse<ProductsDTO> createProduct(List<MultipartFile> multipart, ProductsDTO productsDTO) {
        try {
            // แปลง DTO เป็น Entity
            ProductEntity productEntity = ProductMapper.INSTANCE.toEntity(productsDTO);

            // Find store for create product to store
            StoreEntity storeEntity = storeUseCase.findById(productsDTO.getStoreId())
                    .orElseThrow(() -> new NotFoundException("Store", productsDTO.getStoreId()));

            // set store_id in product
            productEntity.setStore(storeEntity);

            if (productsDTO.getCategories() != null && !productsDTO.getCategories().isEmpty()) {
                List<ProductCategoriesEntity> pCategoriesEntities = categoryUseCase
                        .findAllById(productsDTO.getCategories().stream().map(CategoriesDTO::getId).toList());
                productEntity.setCategories(pCategoriesEntities);
            }
            if (productsDTO.getProductOptions() != null && !productsDTO.getProductOptions().isEmpty()) {
                List<ProductOptionEntity> productOptionEntities = productOptionUseCase
                        .findAllById(productsDTO.getProductOptions().stream().map(ProductOptionDTO::getId).toList());
                productEntity.setProductOptions(productOptionEntities);
            }

            // save product first
            ProductEntity savedProduct = productUseCase.save(productEntity);

            // get store from product
            StockEntity stock = productEntity.getStock();
            stock.setProduct(savedProduct);

            System.out.println(String.format("Stock : %s", stock.toString()));

            if (stock != null) {
                StockEntity savedStock = stockUseCase.save(productEntity.getStock());
                productEntity.setStock(savedStock);
            }

            // บันทึก ProductEntity
            // จัดการกับ ProductImageEntity
            if (multipart != null && !multipart.isEmpty()) {
                multipart.forEach(file -> {
                    productEntity.getProductImages().add(createProductImage(file, savedProduct));
                });
            }

            return new ApiResponse<>(ProductMapper.INSTANCE.toDTO(productEntity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<ProductsDTO> getById(String id) {
        return new ApiResponse<>(productMapper.toDTO(productUseCase.findById(id).orElse(null)));
    }

    @Override
    public ApiResponse<Page<ProductsDTO>> findAllByStoreIdWithPageable(String storeId, int page, int size) {
        Page<ProductEntity> cPage = productUseCase.findAllByStoreIdWithPageable(storeId, page, size);
        return new ApiResponse<>(cPage.map(productMapper::toDTO));
    }

    @Override
    public ApiResponse<ProductsDTO> updateProduct(String productId, List<MultipartFile> files,
            ProductsDTO productsDTO) {
        // แปลง DTO เป็น Entity
        ProductEntity productEntity = productUseCase.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));

        productEntity.setNameEN(productsDTO.getNameEN());
        productEntity.setNameTH(productsDTO.getNameTH());
        productEntity.setDescriptionTH(productsDTO.getDescriptionTH());
        productEntity.setDescriptionEN(productsDTO.getDescriptionEN());
        productEntity.setPrice(productsDTO.getPrice());

        if (!productsDTO.getCategories().isEmpty()) {
            List<ProductCategoriesEntity> pCategoriesEntities = categoryUseCase
                    .findAllById(productsDTO.getCategories().stream().map(CategoriesDTO::getId).toList());
            productEntity.setCategories(pCategoriesEntities);
        }
        if (!productsDTO.getProductOptions().isEmpty()) {
            List<ProductOptionEntity> productOptionEntities = productOptionUseCase
                    .findAllById(productsDTO.getProductOptions().stream().map(ProductOptionDTO::getId).toList());
            productEntity.setProductOptions(productOptionEntities);
        }

        productUseCase.save(productEntity);

        StockEntity stock = StockMapper.INSTANCE.toEntity(productsDTO.getStock());
        stock.setId(productEntity.getStock().getId());
        stock.setProduct(productEntity);

        stockUseCase.save(stock);

        // บันทึก ProductEntity
        // จัดการกับ ProductImageEntity
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                productEntity.getProductImages().add(createProductImage(file, productEntity));
            });
        }

        return new ApiResponse<>(productMapper.toDTO(productEntity));
    }

    public ProductImageEntity createProductImage(MultipartFile file, ProductEntity product) {
        ProductImageEntity productImageEntity = new ProductImageEntity();
        try {
            String pathFile = fileManagement.createPathFile(file, product.getId());
            productImageEntity.setProduct(product);
            productImageEntity.setSource(pathFile);
            productImageUseCase.save(productImageEntity);
            return productImageEntity;
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        ProductEntity productEntity = productUseCase.findById(id)
                .orElseThrow(() -> new NotFoundException("Product", id));

        for (ProductImageEntity productImageEntity : productEntity.getProductImages()) {
            fileManagement.removeFile(productImageEntity.getSource());
            productImageUseCase.delete(productImageEntity.getId());
        }
        stockUseCase.delete(productEntity.getStock().getId());
        productUseCase.delete(id);
    }
}
