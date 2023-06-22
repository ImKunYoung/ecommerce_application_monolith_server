package com.poc.redis.service.impl;

import com.poc.redis.domain.ShoppingCart;
import com.poc.redis.repository.ShoppingCartRepository;
import com.poc.redis.service.ShoppingCartService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShoppingCart}.
 */
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        log.debug("Request to save ShoppingCart : {}", shoppingCart);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        log.debug("Request to update ShoppingCart : {}", shoppingCart);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Optional<ShoppingCart> partialUpdate(ShoppingCart shoppingCart) {
        log.debug("Request to partially update ShoppingCart : {}", shoppingCart);

        return shoppingCartRepository
            .findById(shoppingCart.getId())
            .map(existingShoppingCart -> {
                if (shoppingCart.getPlacedDate() != null) {
                    existingShoppingCart.setPlacedDate(shoppingCart.getPlacedDate());
                }
                if (shoppingCart.getStatus() != null) {
                    existingShoppingCart.setStatus(shoppingCart.getStatus());
                }
                if (shoppingCart.getTotalPrice() != null) {
                    existingShoppingCart.setTotalPrice(shoppingCart.getTotalPrice());
                }
                if (shoppingCart.getPaymentMethod() != null) {
                    existingShoppingCart.setPaymentMethod(shoppingCart.getPaymentMethod());
                }
                if (shoppingCart.getPaymentReference() != null) {
                    existingShoppingCart.setPaymentReference(shoppingCart.getPaymentReference());
                }

                return existingShoppingCart;
            })
            .map(shoppingCartRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCart> findAll() {
        log.debug("Request to get all ShoppingCarts");
        return shoppingCartRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findOne(Long id) {
        log.debug("Request to get ShoppingCart : {}", id);
        return shoppingCartRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShoppingCart : {}", id);
        shoppingCartRepository.deleteById(id);
    }
}
