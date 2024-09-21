package com.app.infrastructure.usecase;

import org.springframework.stereotype.Service;
import java.util.List;
import com.app.domain.models.ProductOption;
import com.app.domain.usecase.ProductOptionUseCase;
import com.app.infrastructure.entity.ProductOptionEntity;
import com.app.infrastructure.repositories.ProductOptionJpaRepository;

@Service
public class ProductOptionUseCaseImpl extends BaseUseCaseImpl<ProductOptionEntity,ProductOption> implements ProductOptionUseCase {

    private final ProductOptionJpaRepository productOptionJpaRepository;
    

    public ProductOptionUseCaseImpl(ProductOptionJpaRepository productOptionJpaRepository) {
        super(productOptionJpaRepository);
        this.productOptionJpaRepository = productOptionJpaRepository;
    }

    @Override
    public ProductOption getByOptionName(String optionName) {
       return productOptionJpaRepository.findByOptionName(optionName);
    }

    @Override
    public List<ProductOption> findAllByStoreId(String storeId) {
        return productOptionJpaRepository.findAllByStoreId(storeId);
    }
    
}
