package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.NiveauEtude;

/**
 * Service Interface for managing {@link NiveauEtude}.
 */
public interface NiveauEtudeService {
    /**
     * Save a niveauEtude.
     *
     * @param niveauEtude the entity to save.
     * @return the persisted entity.
     */
    NiveauEtude save(NiveauEtude niveauEtude);

    /**
     * Partially updates a niveauEtude.
     *
     * @param niveauEtude the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NiveauEtude> partialUpdate(NiveauEtude niveauEtude);

    /**
     * Get all the niveauEtudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NiveauEtude> findAll(Pageable pageable);

    /**
     * Get the "id" niveauEtude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NiveauEtude> findOne(Long id);

    /**
     * Delete the "id" niveauEtude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
