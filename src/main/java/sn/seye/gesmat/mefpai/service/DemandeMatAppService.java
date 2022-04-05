package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.DemandeMatApp;

/**
 * Service Interface for managing {@link DemandeMatApp}.
 */
public interface DemandeMatAppService {
    /**
     * Save a demandeMatApp.
     *
     * @param demandeMatApp the entity to save.
     * @return the persisted entity.
     */
    DemandeMatApp save(DemandeMatApp demandeMatApp);

    /**
     * Partially updates a demandeMatApp.
     *
     * @param demandeMatApp the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeMatApp> partialUpdate(DemandeMatApp demandeMatApp);

    /**
     * Get all the demandeMatApps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeMatApp> findAll(Pageable pageable);

    /**
     * Get the "id" demandeMatApp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeMatApp> findOne(Long id);

    /**
     * Delete the "id" demandeMatApp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
