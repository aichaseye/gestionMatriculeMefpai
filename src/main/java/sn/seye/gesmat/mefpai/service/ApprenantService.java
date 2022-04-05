package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Apprenant;

/**
 * Service Interface for managing {@link Apprenant}.
 */
public interface ApprenantService {
    /**
     * Save a apprenant.
     *
     * @param apprenant the entity to save.
     * @return the persisted entity.
     */
    Apprenant save(Apprenant apprenant);

    /**
     * Partially updates a apprenant.
     *
     * @param apprenant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Apprenant> partialUpdate(Apprenant apprenant);

    /**
     * Get all the apprenants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Apprenant> findAll(Pageable pageable);

    /**
     * Get all the apprenants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Apprenant> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" apprenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Apprenant> findOne(Long id);

    /**
     * Delete the "id" apprenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
