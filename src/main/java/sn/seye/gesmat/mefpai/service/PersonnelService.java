package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Personnel;

/**
 * Service Interface for managing {@link Personnel}.
 */
public interface PersonnelService {
    /**
     * Save a personnel.
     *
     * @param personnel the entity to save.
     * @return the persisted entity.
     */
    Personnel save(Personnel personnel);

    /**
     * Partially updates a personnel.
     *
     * @param personnel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personnel> partialUpdate(Personnel personnel);

    /**
     * Get all the personnel.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personnel> findAll(Pageable pageable);

    /**
     * Get all the personnel with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personnel> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" personnel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personnel> findOne(Long id);

    /**
     * Delete the "id" personnel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
