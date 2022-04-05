package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.FiliereEtude;

/**
 * Service Interface for managing {@link FiliereEtude}.
 */
public interface FiliereEtudeService {
    /**
     * Save a filiereEtude.
     *
     * @param filiereEtude the entity to save.
     * @return the persisted entity.
     */
    FiliereEtude save(FiliereEtude filiereEtude);

    /**
     * Partially updates a filiereEtude.
     *
     * @param filiereEtude the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiliereEtude> partialUpdate(FiliereEtude filiereEtude);

    /**
     * Get all the filiereEtudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiliereEtude> findAll(Pageable pageable);

    /**
     * Get the "id" filiereEtude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiliereEtude> findOne(Long id);

    /**
     * Delete the "id" filiereEtude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
