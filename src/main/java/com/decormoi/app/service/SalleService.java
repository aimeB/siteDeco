package com.decormoi.app.service;

import com.decormoi.app.domain.Salle;
import com.decormoi.app.repository.SalleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Salle}.
 */
@Service
@Transactional
public class SalleService {

    private final Logger log = LoggerFactory.getLogger(SalleService.class);

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    /**
     * Save a salle.
     *
     * @param salle the entity to save.
     * @return the persisted entity.
     */
    public Salle save(Salle salle) {
        log.debug("Request to save Salle : {}", salle);
        return salleRepository.save(salle);
    }

    /**
     * Partially update a salle.
     *
     * @param salle the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Salle> partialUpdate(Salle salle) {
        log.debug("Request to partially update Salle : {}", salle);

        return salleRepository
            .findById(salle.getId())
            .map(
                existingSalle -> {
                    if (salle.getNom() != null) {
                        existingSalle.setNom(salle.getNom());
                    }

                    return existingSalle;
                }
            )
            .map(salleRepository::save);
    }

    /**
     * Get all the salles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Salle> findAll(Pageable pageable) {
        log.debug("Request to get all Salles");
        return salleRepository.findAll(pageable);
    }

    /**
     * Get one salle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Salle> findOne(Long id) {
        log.debug("Request to get Salle : {}", id);
        return salleRepository.findById(id);
    }

    /**
     * Delete the salle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Salle : {}", id);
        salleRepository.deleteById(id);
    }
}
