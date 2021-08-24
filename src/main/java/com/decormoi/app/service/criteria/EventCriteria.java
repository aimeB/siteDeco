package com.decormoi.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.decormoi.app.domain.Event} entity. This class is used
 * in {@link com.decormoi.app.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private InstantFilter dateEvenement;

    private DoubleFilter prix;

    private LongFilter appartenantAId;

    private LongFilter agentEvenementId;

    private LongFilter typeEvenementId;

    private LongFilter produitId;

    private LongFilter salleId;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.dateEvenement = other.dateEvenement == null ? null : other.dateEvenement.copy();
        this.prix = other.prix == null ? null : other.prix.copy();
        this.appartenantAId = other.appartenantAId == null ? null : other.appartenantAId.copy();
        this.agentEvenementId = other.agentEvenementId == null ? null : other.agentEvenementId.copy();
        this.typeEvenementId = other.typeEvenementId == null ? null : other.typeEvenementId.copy();
        this.produitId = other.produitId == null ? null : other.produitId.copy();
        this.salleId = other.salleId == null ? null : other.salleId.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public InstantFilter getDateEvenement() {
        return dateEvenement;
    }

    public InstantFilter dateEvenement() {
        if (dateEvenement == null) {
            dateEvenement = new InstantFilter();
        }
        return dateEvenement;
    }

    public void setDateEvenement(InstantFilter dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public DoubleFilter getPrix() {
        return prix;
    }

    public DoubleFilter prix() {
        if (prix == null) {
            prix = new DoubleFilter();
        }
        return prix;
    }

    public void setPrix(DoubleFilter prix) {
        this.prix = prix;
    }

    public LongFilter getAppartenantAId() {
        return appartenantAId;
    }

    public LongFilter appartenantAId() {
        if (appartenantAId == null) {
            appartenantAId = new LongFilter();
        }
        return appartenantAId;
    }

    public void setAppartenantAId(LongFilter appartenantAId) {
        this.appartenantAId = appartenantAId;
    }

    public LongFilter getAgentEvenementId() {
        return agentEvenementId;
    }

    public LongFilter agentEvenementId() {
        if (agentEvenementId == null) {
            agentEvenementId = new LongFilter();
        }
        return agentEvenementId;
    }

    public void setAgentEvenementId(LongFilter agentEvenementId) {
        this.agentEvenementId = agentEvenementId;
    }

    public LongFilter getTypeEvenementId() {
        return typeEvenementId;
    }

    public LongFilter typeEvenementId() {
        if (typeEvenementId == null) {
            typeEvenementId = new LongFilter();
        }
        return typeEvenementId;
    }

    public void setTypeEvenementId(LongFilter typeEvenementId) {
        this.typeEvenementId = typeEvenementId;
    }

    public LongFilter getProduitId() {
        return produitId;
    }

    public LongFilter produitId() {
        if (produitId == null) {
            produitId = new LongFilter();
        }
        return produitId;
    }

    public void setProduitId(LongFilter produitId) {
        this.produitId = produitId;
    }

    public LongFilter getSalleId() {
        return salleId;
    }

    public LongFilter salleId() {
        if (salleId == null) {
            salleId = new LongFilter();
        }
        return salleId;
    }

    public void setSalleId(LongFilter salleId) {
        this.salleId = salleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(dateEvenement, that.dateEvenement) &&
            Objects.equals(prix, that.prix) &&
            Objects.equals(appartenantAId, that.appartenantAId) &&
            Objects.equals(agentEvenementId, that.agentEvenementId) &&
            Objects.equals(typeEvenementId, that.typeEvenementId) &&
            Objects.equals(produitId, that.produitId) &&
            Objects.equals(salleId, that.salleId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, dateEvenement, prix, appartenantAId, agentEvenementId, typeEvenementId, produitId, salleId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (dateEvenement != null ? "dateEvenement=" + dateEvenement + ", " : "") +
            (prix != null ? "prix=" + prix + ", " : "") +
            (appartenantAId != null ? "appartenantAId=" + appartenantAId + ", " : "") +
            (agentEvenementId != null ? "agentEvenementId=" + agentEvenementId + ", " : "") +
            (typeEvenementId != null ? "typeEvenementId=" + typeEvenementId + ", " : "") +
            (produitId != null ? "produitId=" + produitId + ", " : "") +
            (salleId != null ? "salleId=" + salleId + ", " : "") +
            "}";
    }
}
