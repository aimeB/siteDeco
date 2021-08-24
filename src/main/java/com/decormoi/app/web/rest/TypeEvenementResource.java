package com.decormoi.app.web.rest;

import com.decormoi.app.domain.TypeEvenement;
import com.decormoi.app.repository.TypeEvenementRepository;
import com.decormoi.app.service.TypeEvenementQueryService;
import com.decormoi.app.service.TypeEvenementService;
import com.decormoi.app.service.criteria.TypeEvenementCriteria;
import com.decormoi.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.decormoi.app.domain.TypeEvenement}.
 */
@RestController
@RequestMapping("/api")
public class TypeEvenementResource {

    private final Logger log = LoggerFactory.getLogger(TypeEvenementResource.class);

    private static final String ENTITY_NAME = "typeEvenement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeEvenementService typeEvenementService;

    private final TypeEvenementRepository typeEvenementRepository;

    private final TypeEvenementQueryService typeEvenementQueryService;

    public TypeEvenementResource(
        TypeEvenementService typeEvenementService,
        TypeEvenementRepository typeEvenementRepository,
        TypeEvenementQueryService typeEvenementQueryService
    ) {
        this.typeEvenementService = typeEvenementService;
        this.typeEvenementRepository = typeEvenementRepository;
        this.typeEvenementQueryService = typeEvenementQueryService;
    }

    /**
     * {@code POST  /type-evenements} : Create a new typeEvenement.
     *
     * @param typeEvenement the typeEvenement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeEvenement, or with status {@code 400 (Bad Request)} if the typeEvenement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-evenements")
    public ResponseEntity<TypeEvenement> createTypeEvenement(@Valid @RequestBody TypeEvenement typeEvenement) throws URISyntaxException {
        log.debug("REST request to save TypeEvenement : {}", typeEvenement);
        if (typeEvenement.getId() != null) {
            throw new BadRequestAlertException("A new typeEvenement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeEvenement result = typeEvenementService.save(typeEvenement);
        return ResponseEntity
            .created(new URI("/api/type-evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-evenements/:id} : Updates an existing typeEvenement.
     *
     * @param id the id of the typeEvenement to save.
     * @param typeEvenement the typeEvenement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeEvenement,
     * or with status {@code 400 (Bad Request)} if the typeEvenement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeEvenement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-evenements/{id}")
    public ResponseEntity<TypeEvenement> updateTypeEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeEvenement typeEvenement
    ) throws URISyntaxException {
        log.debug("REST request to update TypeEvenement : {}, {}", id, typeEvenement);
        if (typeEvenement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeEvenement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeEvenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeEvenement result = typeEvenementService.save(typeEvenement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeEvenement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-evenements/:id} : Partial updates given fields of an existing typeEvenement, field will ignore if it is null
     *
     * @param id the id of the typeEvenement to save.
     * @param typeEvenement the typeEvenement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeEvenement,
     * or with status {@code 400 (Bad Request)} if the typeEvenement is not valid,
     * or with status {@code 404 (Not Found)} if the typeEvenement is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeEvenement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-evenements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeEvenement> partialUpdateTypeEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeEvenement typeEvenement
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeEvenement partially : {}, {}", id, typeEvenement);
        if (typeEvenement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeEvenement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeEvenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeEvenement> result = typeEvenementService.partialUpdate(typeEvenement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeEvenement.getId().toString())
        );
    }

    /**
     * {@code GET  /type-evenements} : get all the typeEvenements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeEvenements in body.
     */
    @GetMapping("/type-evenements")
    public ResponseEntity<List<TypeEvenement>> getAllTypeEvenements(TypeEvenementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypeEvenements by criteria: {}", criteria);
        Page<TypeEvenement> page = typeEvenementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-evenements/count} : count all the typeEvenements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-evenements/count")
    public ResponseEntity<Long> countTypeEvenements(TypeEvenementCriteria criteria) {
        log.debug("REST request to count TypeEvenements by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeEvenementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-evenements/:id} : get the "id" typeEvenement.
     *
     * @param id the id of the typeEvenement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeEvenement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-evenements/{id}")
    public ResponseEntity<TypeEvenement> getTypeEvenement(@PathVariable Long id) {
        log.debug("REST request to get TypeEvenement : {}", id);
        Optional<TypeEvenement> typeEvenement = typeEvenementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeEvenement);
    }

    /**
     * {@code DELETE  /type-evenements/:id} : delete the "id" typeEvenement.
     *
     * @param id the id of the typeEvenement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-evenements/{id}")
    public ResponseEntity<Void> deleteTypeEvenement(@PathVariable Long id) {
        log.debug("REST request to delete TypeEvenement : {}", id);
        typeEvenementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
