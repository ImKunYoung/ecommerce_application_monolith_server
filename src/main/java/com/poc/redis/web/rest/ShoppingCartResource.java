package com.poc.redis.web.rest;

import com.poc.redis.application.dto.ShoppingCartDTO;
import com.poc.redis.application.usecase.ShoppingCartUsecase;
import com.poc.redis.infrastructure.repository.ShoppingCartRepository;
import com.poc.redis.web.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.poc.redis.domain.model.ShoppingCart}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ShoppingCartResource {

    private static final String ENTITY_NAME = "shoppingCart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingCartUsecase shoppingCartUsecase;

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartResource(ShoppingCartUsecase shoppingCartUsecase, ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartUsecase = shoppingCartUsecase;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * {@code POST  /shopping-carts} : Create a new shoppingCart.
     *
     * @param shoppingCartDTO the shoppingCartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingCartDTO, or with status {@code 400 (Bad Request)} if the shoppingCart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shopping-carts")
    public ResponseEntity<ShoppingCartDTO> createShoppingCart(@Valid @RequestBody ShoppingCartDTO shoppingCartDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShoppingCart : {}", shoppingCartDTO);
        if (shoppingCartDTO.getId() != null) {
            throw new BadRequestAlertException("A new shoppingCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingCartDTO result = shoppingCartUsecase.save(shoppingCartDTO);
        return ResponseEntity
            .created(new URI("/api/shopping-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-carts/:id} : Updates an existing shoppingCart.
     *
     * @param id the id of the shoppingCartDTO to save.
     * @param shoppingCartDTO the shoppingCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCartDTO,
     * or with status {@code 400 (Bad Request)} if the shoppingCartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-carts/{id}")
    public ResponseEntity<ShoppingCartDTO> updateShoppingCart(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingCartDTO shoppingCartDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShoppingCart : {}, {}", id, shoppingCartDTO);
        if (shoppingCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShoppingCartDTO result = shoppingCartUsecase.update(shoppingCartDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingCartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shopping-carts/:id} : Partial updates given fields of an existing shoppingCart, field will ignore if it is null
     *
     * @param id the id of the shoppingCartDTO to save.
     * @param shoppingCartDTO the shoppingCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCartDTO,
     * or with status {@code 400 (Bad Request)} if the shoppingCartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingCartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shopping-carts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShoppingCartDTO> partialUpdateShoppingCart(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingCartDTO shoppingCartDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShoppingCart partially : {}, {}", id, shoppingCartDTO);
        if (shoppingCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingCartDTO> result = shoppingCartUsecase.partialUpdate(shoppingCartDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingCartDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-carts} : get all the shoppingCarts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingCarts in body.
     */
    @GetMapping("/shopping-carts")
    public List<ShoppingCartDTO> getAllShoppingCarts() {
        log.debug("REST request to get all ShoppingCarts");
        return shoppingCartUsecase.findAll();
    }

    /**
     * {@code GET  /shopping-carts/:id} : get the "id" shoppingCart.
     *
     * @param id the id of the shoppingCartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-carts/{id}")
    public ResponseEntity<ShoppingCartDTO> getShoppingCart(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCart : {}", id);
        Optional<ShoppingCartDTO> shoppingCartDTO = shoppingCartUsecase.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingCartDTO);
    }

    /**
     * {@code DELETE  /shopping-carts/:id} : delete the "id" shoppingCart.
     *
     * @param id the id of the shoppingCartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shopping-carts/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCart : {}", id);
        shoppingCartUsecase.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
