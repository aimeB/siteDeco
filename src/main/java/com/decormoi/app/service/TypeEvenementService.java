package com.decormoi.app.service;

import com.decormoi.app.domain.TypeEvenement;
import com.decormoi.app.repository.TypeEvenementRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeEvenement}.
 */
@Service
@Transactional
public class TypeEvenementService {

    private final Logger log = LoggerFactory.getLogger(TypeEvenementService.class);

    private final TypeEvenementRepository typeEvenementRepository;

    public TypeEvenementService(TypeEvenementRepository typeEvenementRepository) {
        this.typeEvenementRepository = typeEvenementRepository;
    }

    /**
     * Save a typeEvenement.
     *
     * @param typeEvenement the entity to save.
     * @return the persisted entity.
     */
    public TypeEvenement save(TypeEvenement typeEvenement) {
        log.debug("Request to save TypeEvenement : {}", typeEvenement);
        return typeEvenementRepository.save(typeEvenement);
    }

    /**
     * Partially update a typeEvenement.
     *
     * @param typeEvenement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeEvenement> partialUpdate(TypeEvenement typeEvenement) {
        log.debug("Request to partially update TypeEvenement : {}", typeEvenement);

        return typeEvenementRepository
            .findById(typeEvenement.getId())
            .map(
                existingTypeEvenement -> {
                    if (typeEvenement.getNom() != null) {
                        existingTypeEvenement.setNom(typeEvenement.getNom());
                    }

                    return existingTypeEvenement;
                }
            )
            .map(typeEvenementRepository::save);
    }

    /**
     * Get all the typeEvenements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeEvenement> findAll(Pageable pageable) {
        log.debug("Request to get all TypeEvenements");
        return typeEvenementRepository.findAll(pageable);
    }

    /**
     * Get one typeEvenement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeEvenement> findOne(Long id) {
        log.debug("Request to get TypeEvenement : {}", id);
        return typeEvenementRepository.findById(id);
    }

    /**
     * Delete the typeEvenement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeEvenement : {}", id);
        typeEvenementRepository.deleteById(id);
    }
}
