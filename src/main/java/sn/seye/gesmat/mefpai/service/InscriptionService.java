package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Inscription;

/**
 * Service Interface for managing {@link Inscription}.
 */
public interface InscriptionService {
    /**
     * Save a inscription.
     *
     * @param inscription the entity to save.
     * @return the persisted entity.
     */
    Inscription save(Inscription inscription);

    /**
     * Partially updates a inscription.
     *
     * @param inscription the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inscription> partialUpdate(Inscription inscription);

    /**
     * Get all the inscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inscription> findAll(Pageable pageable);

    /**
     * Get all the inscriptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inscription> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" inscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inscription> findOne(Long id);

    /**
     * Delete the "id" inscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
