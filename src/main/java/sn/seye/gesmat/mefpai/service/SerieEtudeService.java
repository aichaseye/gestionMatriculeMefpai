package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.SerieEtude;

/**
 * Service Interface for managing {@link SerieEtude}.
 */
public interface SerieEtudeService {
    /**
     * Save a serieEtude.
     *
     * @param serieEtude the entity to save.
     * @return the persisted entity.
     */
    SerieEtude save(SerieEtude serieEtude);

    /**
     * Partially updates a serieEtude.
     *
     * @param serieEtude the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SerieEtude> partialUpdate(SerieEtude serieEtude);

    /**
     * Get all the serieEtudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SerieEtude> findAll(Pageable pageable);

    /**
     * Get the "id" serieEtude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SerieEtude> findOne(Long id);

    /**
     * Delete the "id" serieEtude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
