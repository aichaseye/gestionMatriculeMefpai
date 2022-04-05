package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;

/**
 * Service Interface for managing {@link NoteProgramme}.
 */
public interface NoteProgrammeService {
    /**
     * Save a noteProgramme.
     *
     * @param noteProgramme the entity to save.
     * @return the persisted entity.
     */
    NoteProgramme save(NoteProgramme noteProgramme);

    /**
     * Partially updates a noteProgramme.
     *
     * @param noteProgramme the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NoteProgramme> partialUpdate(NoteProgramme noteProgramme);

    /**
     * Get all the noteProgrammes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NoteProgramme> findAll(Pageable pageable);

    /**
     * Get all the noteProgrammes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NoteProgramme> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" noteProgramme.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NoteProgramme> findOne(Long id);

    /**
     * Delete the "id" noteProgramme.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
