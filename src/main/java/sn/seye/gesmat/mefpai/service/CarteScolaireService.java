package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.CarteScolaire;

/**
 * Service Interface for managing {@link CarteScolaire}.
 */
public interface CarteScolaireService {
    /**
     * Save a carteScolaire.
     *
     * @param carteScolaire the entity to save.
     * @return the persisted entity.
     */
    CarteScolaire save(CarteScolaire carteScolaire);

    /**
     * Partially updates a carteScolaire.
     *
     * @param carteScolaire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarteScolaire> partialUpdate(CarteScolaire carteScolaire);

    /**
     * Get all the carteScolaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarteScolaire> findAll(Pageable pageable);

    /**
     * Get all the carteScolaires with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarteScolaire> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" carteScolaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarteScolaire> findOne(Long id);

    /**
     * Delete the "id" carteScolaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
