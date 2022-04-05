package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Bon;

/**
 * Service Interface for managing {@link Bon}.
 */
public interface BonService {
    /**
     * Save a bon.
     *
     * @param bon the entity to save.
     * @return the persisted entity.
     */
    Bon save(Bon bon);

    /**
     * Partially updates a bon.
     *
     * @param bon the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bon> partialUpdate(Bon bon);

    /**
     * Get all the bons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bon> findAll(Pageable pageable);

    /**
     * Get all the bons with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bon> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bon.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bon> findOne(Long id);

    /**
     * Delete the "id" bon.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
