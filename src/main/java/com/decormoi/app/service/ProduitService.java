package com.decormoi.app.service;

import com.decormoi.app.domain.Produit;
import com.decormoi.app.repository.ProduitRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Produit}.
 */
@Service
@Transactional
public class ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitService.class);

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    /**
     * Save a produit.
     *
     * @param produit the entity to save.
     * @return the persisted entity.
     */
    public Produit save(Produit produit) {
        log.debug("Request to save Produit : {}", produit);
        return produitRepository.save(produit);
    }

    /**
     * Partially update a produit.
     *
     * @param produit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Produit> partialUpdate(Produit produit) {
        log.debug("Request to partially update Produit : {}", produit);

        return produitRepository
            .findById(produit.getId())
            .map(
                existingProduit -> {
                    if (produit.getNom() != null) {
                        existingProduit.setNom(produit.getNom());
                    }
                    if (produit.getDescription() != null) {
                        existingProduit.setDescription(produit.getDescription());
                    }
                    if (produit.getPrix() != null) {
                        existingProduit.setPrix(produit.getPrix());
                    }
                    if (produit.getImage() != null) {
                        existingProduit.setImage(produit.getImage());
                    }
                    if (produit.getImageContentType() != null) {
                        existingProduit.setImageContentType(produit.getImageContentType());
                    }

                    return existingProduit;
                }
            )
            .map(produitRepository::save);
    }

    /**
     * Get all the produits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Produit> findAll(Pageable pageable) {
        log.debug("Request to get all Produits");
        return produitRepository.findAll(pageable);
    }

    /**
     * Get one produit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Produit> findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findById(id);
    }

    /**
     * Delete the produit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        produitRepository.deleteById(id);
    }
}
