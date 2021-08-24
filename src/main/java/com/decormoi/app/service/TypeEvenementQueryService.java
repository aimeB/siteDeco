package com.decormoi.app.service;

import com.decormoi.app.domain.*; // for static metamodels
import com.decormoi.app.domain.TypeEvenement;
import com.decormoi.app.repository.TypeEvenementRepository;
import com.decormoi.app.service.criteria.TypeEvenementCriteria;
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
 * Service for executing complex queries for {@link TypeEvenement} entities in the database.
 * The main input is a {@link TypeEvenementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeEvenement} or a {@link Page} of {@link TypeEvenement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeEvenementQueryService extends QueryService<TypeEvenement> {

    private final Logger log = LoggerFactory.getLogger(TypeEvenementQueryService.class);

    private final TypeEvenementRepository typeEvenementRepository;

    public TypeEvenementQueryService(TypeEvenementRepository typeEvenementRepository) {
        this.typeEvenementRepository = typeEvenementRepository;
    }

    /**
     * Return a {@link List} of {@link TypeEvenement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeEvenement> findByCriteria(TypeEvenementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TypeEvenement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeEvenement> findByCriteria(TypeEvenementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeEvenementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeEvenementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeEvenement> createSpecification(TypeEvenementCriteria criteria) {
        Specification<TypeEvenement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeEvenement_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), TypeEvenement_.nom));
            }
        }
        return specification;
    }
}
