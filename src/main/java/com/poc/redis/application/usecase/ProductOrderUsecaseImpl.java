package com.poc.redis.application.usecase;

import com.poc.redis.domain.model.ProductOrder;
import com.poc.redis.infrastructure.repository.ProductOrderRepository;
import com.poc.redis.application.dto.ProductOrderDTO;
import com.poc.redis.application.mapper.ProductOrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductOrder}.
 */
@Slf4j
@Service
@Transactional
public class ProductOrderUsecaseImpl implements ProductOrderUsecase {

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderMapper productOrderMapper;

    public ProductOrderUsecaseImpl(ProductOrderRepository productOrderRepository, ProductOrderMapper productOrderMapper) {
        this.productOrderRepository = productOrderRepository;
        this.productOrderMapper = productOrderMapper;
    }

    @Override
    public ProductOrderDTO save(ProductOrderDTO productOrderDTO) {
        log.debug("Request to save ProductOrder : {}", productOrderDTO);
        ProductOrder productOrder = productOrderMapper.toEntity(productOrderDTO);
        productOrder = productOrderRepository.save(productOrder);
        return productOrderMapper.toDto(productOrder);
    }

    @Override
    public ProductOrderDTO update(ProductOrderDTO productOrderDTO) {
        log.debug("Request to update ProductOrder : {}", productOrderDTO);
        ProductOrder productOrder = productOrderMapper.toEntity(productOrderDTO);
        productOrder = productOrderRepository.save(productOrder);
        return productOrderMapper.toDto(productOrder);
    }

    @Override
    public Optional<ProductOrderDTO> partialUpdate(ProductOrderDTO productOrderDTO) {
        log.debug("Request to partially update ProductOrder : {}", productOrderDTO);

        return productOrderRepository
            .findById(productOrderDTO.getId())
            .map(existingProductOrder -> {
                productOrderMapper.partialUpdate(existingProductOrder, productOrderDTO);

                return existingProductOrder;
            })
            .map(productOrderRepository::save)
            .map(productOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOrderDTO> findAll() {
        log.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll().stream().map(productOrderMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productOrderRepository.findAllWithEagerRelationships(pageable).map(productOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductOrderDTO> findOne(Long id) {
        log.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findOneWithEagerRelationships(id).map(productOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
    }
}
