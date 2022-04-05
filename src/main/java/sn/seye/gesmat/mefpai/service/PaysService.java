package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.Pays;

/**
 * Service Interface for managing {@link Pays}.
 */
public interface PaysService {
    /**
     * Save a pays.
     *
     * @param pays the entity to save.
     * @return the persisted entity.
     */
    Pays save(Pays pays);

    /**
     * Partially updates a pays.
     *
     * @param pays the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pays> partialUpdate(Pays pays);

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pays> findAll(Pageable pageable);

    /**
     * Get the "id" pays.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pays> findOne(Long id);

    /**
     * Delete the "id" pays.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
