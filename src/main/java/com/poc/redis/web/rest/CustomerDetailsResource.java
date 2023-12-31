package com.poc.redis.web.rest;

import com.poc.redis.application.dto.CustomerDetailsDTO;
import com.poc.redis.application.usecase.CustomerDetailsUsecase;
import com.poc.redis.infrastructure.repository.CustomerDetailsRepository;
import com.poc.redis.web.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.poc.redis.domain.model.CustomerDetails}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CustomerDetailsResource {

    private static final String ENTITY_NAME = "customerDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerDetailsUsecase customerDetailsUsecase;

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsResource(CustomerDetailsUsecase customerDetailsUsecase, CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsUsecase = customerDetailsUsecase;
        this.customerDetailsRepository = customerDetailsRepository;
    }

    /**
     * {@code POST  /customer-details} : Create a new customerDetails.
     *
     * @param customerDetailsDTO the customerDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDetailsDTO, or with status {@code 400 (Bad Request)} if the customerDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-details")
    public ResponseEntity<CustomerDetailsDTO> createCustomerDetails(@Valid @RequestBody CustomerDetailsDTO customerDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerDetails : {}", customerDetailsDTO);
        if (customerDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDetailsDTO result = customerDetailsUsecase.save(customerDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/customer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-details/:id} : Updates an existing customerDetails.
     *
     * @param id the id of the customerDetailsDTO to save.
     * @param customerDetailsDTO the customerDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the customerDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetailsDTO> updateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerDetailsDTO customerDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerDetails : {}, {}", id, customerDetailsDTO);
        if (customerDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDetailsDTO result = customerDetailsUsecase.update(customerDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-details/:id} : Partial updates given fields of an existing customerDetails, field will ignore if it is null
     *
     * @param id the id of the customerDetailsDTO to save.
     * @param customerDetailsDTO the customerDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the customerDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDetailsDTO> partialUpdateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerDetailsDTO customerDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerDetails partially : {}, {}", id, customerDetailsDTO);
        if (customerDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDetailsDTO> result = customerDetailsUsecase.partialUpdate(customerDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-details} : get all the customerDetails.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerDetails in body.
     */
    @GetMapping("/customer-details")
    public ResponseEntity<List<CustomerDetailsDTO>> getAllCustomerDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of CustomerDetails");
        Page<CustomerDetailsDTO> page;
        if (eagerload) {
            page = customerDetailsUsecase.findAllWithEagerRelationships(pageable);
        } else {
            page = customerDetailsUsecase.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-details/:id} : get the "id" customerDetails.
     *
     * @param id the id of the customerDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetailsDTO> getCustomerDetails(@PathVariable Long id) {
        log.debug("REST request to get CustomerDetails : {}", id);
        Optional<CustomerDetailsDTO> customerDetailsDTO = customerDetailsUsecase.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDetailsDTO);
    }

    /**
     * {@code DELETE  /customer-details/:id} : delete the "id" customerDetails.
     *
     * @param id the id of the customerDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-details/{id}")
    public ResponseEntity<Void> deleteCustomerDetails(@PathVariable Long id) {
        log.debug("REST request to delete CustomerDetails : {}", id);
        customerDetailsUsecase.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
