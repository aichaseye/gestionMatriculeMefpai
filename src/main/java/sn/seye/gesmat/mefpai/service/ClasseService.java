package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Classe;

/**
 * Service Interface for managing {@link Classe}.
 */
public interface ClasseService {
    /**
     * Save a classe.
     *
     * @param classe the entity to save.
     * @return the persisted entity.
     */
    Classe save(Classe classe);

    /**
     * Partially updates a classe.
     *
     * @param classe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Classe> partialUpdate(Classe classe);

    /**
     * Get all the classes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Classe> findAll(Pageable pageable);

    /**
     * Get all the classes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Classe> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" classe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Classe> findOne(Long id);

    /**
     * Delete the "id" classe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
