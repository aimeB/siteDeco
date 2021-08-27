package com.decormoi.app.repository;

import com.decormoi.app.domain.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Event entity.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("select event from Event event where event.appartenantA.login = ?#{principal.username}")
    List<Event> findByAppartenantAIsCurrentUser();

    Optional<Event> findByIdAndAppartenantAId(long eventId, long userId);

    @Query(
        value = "select distinct event from Event event left join fetch event.agentEvenements left join fetch event.produits",
        countQuery = "select count(distinct event) from Event event"
    )
    Page<Event> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct event from Event event left join fetch event.agentEvenements left join fetch event.produits")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.agentEvenements left join fetch event.produits where event.id =:id")
    Optional<Event> findOneWithEagerRelationships(@Param("id") Long id);
}
