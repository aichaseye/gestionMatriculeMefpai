package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Commune;

/**
 * Service Interface for managing {@link Commune}.
 */
public interface CommuneService {
    /**
     * Save a commune.
     *
     * @param commune the entity to save.
     * @return the persisted entity.
     */
    Commune save(Commune commune);

    /**
     * Partially updates a commune.
     *
     * @param commune the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Commune> partialUpdate(Commune commune);

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Commune> findAll(Pageable pageable);

    /**
     * Get all the communes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Commune> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" commune.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Commune> findOne(Long id);

    /**
     * Delete the "id" commune.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
