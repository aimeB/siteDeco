package com.decormoi.app.service;

import com.decormoi.app.domain.CategorieProduit;
import com.decormoi.app.repository.CategorieProduitRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategorieProduit}.
 */
@Service
@Transactional
public class CategorieProduitService {

    private final Logger log = LoggerFactory.getLogger(CategorieProduitService.class);

    private final CategorieProduitRepository categorieProduitRepository;

    public CategorieProduitService(CategorieProduitRepository categorieProduitRepository) {
        this.categorieProduitRepository = categorieProduitRepository;
    }

    /**
     * Save a categorieProduit.
     *
     * @param categorieProduit the entity to save.
     * @return the persisted entity.
     */
    public CategorieProduit save(CategorieProduit categorieProduit) {
        log.debug("Request to save CategorieProduit : {}", categorieProduit);
        return categorieProduitRepository.save(categorieProduit);
    }

    /**
     * Partially update a categorieProduit.
     *
     * @param categorieProduit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategorieProduit> partialUpdate(CategorieProduit categorieProduit) {
        log.debug("Request to partially update CategorieProduit : {}", categorieProduit);

        return categorieProduitRepository
            .findById(categorieProduit.getId())
            .map(
                existingCategorieProduit -> {
                    if (categorieProduit.getNom() != null) {
                        existingCategorieProduit.setNom(categorieProduit.getNom());
                    }

                    return existingCategorieProduit;
                }
            )
            .map(categorieProduitRepository::save);
    }

    /**
     * Get all the categorieProduits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorieProduit> findAll(Pageable pageable) {
        log.debug("Request to get all CategorieProduits");
        return categorieProduitRepository.findAll(pageable);
    }

    /**
     * Get one categorieProduit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategorieProduit> findOne(Long id) {
        log.debug("Request to get CategorieProduit : {}", id);
        return categorieProduitRepository.findById(id);
    }

    /**
     * Delete the categorieProduit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CategorieProduit : {}", id);
        categorieProduitRepository.deleteById(id);
    }
}
