package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Diplome;

/**
 * Service Interface for managing {@link Diplome}.
 */
public interface DiplomeService {
    /**
     * Save a diplome.
     *
     * @param diplome the entity to save.
     * @return the persisted entity.
     */
    Diplome save(Diplome diplome);

    /**
     * Partially updates a diplome.
     *
     * @param diplome the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Diplome> partialUpdate(Diplome diplome);

    /**
     * Get all the diplomes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Diplome> findAll(Pageable pageable);

    /**
     * Get all the diplomes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Diplome> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" diplome.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Diplome> findOne(Long id);

    /**
     * Delete the "id" diplome.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
