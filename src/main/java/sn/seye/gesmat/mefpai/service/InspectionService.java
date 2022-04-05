package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Inspection;

/**
 * Service Interface for managing {@link Inspection}.
 */
public interface InspectionService {
    /**
     * Save a inspection.
     *
     * @param inspection the entity to save.
     * @return the persisted entity.
     */
    Inspection save(Inspection inspection);

    /**
     * Partially updates a inspection.
     *
     * @param inspection the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inspection> partialUpdate(Inspection inspection);

    /**
     * Get all the inspections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inspection> findAll(Pageable pageable);

    /**
     * Get all the inspections with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inspection> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" inspection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inspection> findOne(Long id);

    /**
     * Delete the "id" inspection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
