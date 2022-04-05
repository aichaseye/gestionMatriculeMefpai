package sn.seye.gesmat.mefpai.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.seye.gesmat.mefpai.domain.ReleveNote;

/**
 * Service Interface for managing {@link ReleveNote}.
 */
public interface ReleveNoteService {
    /**
     * Save a releveNote.
     *
     * @param releveNote the entity to save.
     * @return the persisted entity.
     */
    ReleveNote save(ReleveNote releveNote);

    /**
     * Partially updates a releveNote.
     *
     * @param releveNote the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReleveNote> partialUpdate(ReleveNote releveNote);

    /**
     * Get all the releveNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReleveNote> findAll(Pageable pageable);

    /**
     * Get all the releveNotes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReleveNote> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" releveNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReleveNote> findOne(Long id);

    /**
     * Delete the "id" releveNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
