package com.decormoi.app.service;

import com.decormoi.app.domain.*; // for static metamodels
import com.decormoi.app.domain.CategorieProduit;
import com.decormoi.app.repository.CategorieProduitRepository;
import com.decormoi.app.service.criteria.CategorieProduitCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CategorieProduit} entities in the database.
 * The main input is a {@link CategorieProduitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategorieProduit} or a {@link Page} of {@link CategorieProduit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategorieProduitQueryService extends QueryService<CategorieProduit> {

    private final Logger log = LoggerFactory.getLogger(CategorieProduitQueryService.class);

    private final CategorieProduitRepository categorieProduitRepository;

    public CategorieProduitQueryService(CategorieProduitRepository categorieProduitRepository) {
        this.categorieProduitRepository = categorieProduitRepository;
    }

    /**
     * Return a {@link List} of {@link CategorieProduit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategorieProduit> findByCriteria(CategorieProduitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CategorieProduit> specification = createSpecification(criteria);
        return categorieProduitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CategorieProduit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorieProduit> findByCriteria(CategorieProduitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategorieProduit> specification = createSpecification(criteria);
        return categorieProduitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategorieProduitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CategorieProduit> specification = createSpecification(criteria);
        return categorieProduitRepository.count(specification);
    }

    /**
     * Function to convert {@link CategorieProduitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategorieProduit> createSpecification(CategorieProduitCriteria criteria) {
        Specification<CategorieProduit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategorieProduit_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), CategorieProduit_.nom));
            }
        }
        return specification;
    }
}
