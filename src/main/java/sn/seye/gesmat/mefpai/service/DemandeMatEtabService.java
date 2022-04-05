package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.DemandeMatEtab;

/**
 * Service Interface for managing {@link DemandeMatEtab}.
 */
public interface DemandeMatEtabService {
    /**
     * Save a demandeMatEtab.
     *
     * @param demandeMatEtab the entity to save.
     * @return the persisted entity.
     */
    DemandeMatEtab save(DemandeMatEtab demandeMatEtab);

    /**
     * Partially updates a demandeMatEtab.
     *
     * @param demandeMatEtab the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeMatEtab> partialUpdate(DemandeMatEtab demandeMatEtab);

    /**
     * Get all the demandeMatEtabs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeMatEtab> findAll(Pageable pageable);

    /**
     * Get all the demandeMatEtabs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeMatEtab> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" demandeMatEtab.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeMatEtab> findOne(Long id);

    /**
     * Delete the "id" demandeMatEtab.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
