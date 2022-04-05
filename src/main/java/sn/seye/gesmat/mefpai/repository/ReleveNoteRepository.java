package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.ReleveNote;

/**
 * Spring Data SQL repository for the ReleveNote entity.
 */
@Repository
public interface ReleveNoteRepository extends JpaRepository<ReleveNote, Long> {
    default Optional<ReleveNote> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReleveNote> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReleveNote> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct releveNote from ReleveNote releveNote left join fetch releveNote.apprenant left join fetch releveNote.filiereEtude left join fetch releveNote.serieEtude left join fetch releveNote.niveauEtude",
        countQuery = "select count(distinct releveNote) from ReleveNote releveNote"
    )
    Page<ReleveNote> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct releveNote from ReleveNote releveNote left join fetch releveNote.apprenant left join fetch releveNote.filiereEtude left join fetch releveNote.serieEtude left join fetch releveNote.niveauEtude"
    )
    List<ReleveNote> findAllWithToOneRelationships();

    @Query(
        "select releveNote from ReleveNote releveNote left join fetch releveNote.apprenant left join fetch releveNote.filiereEtude left join fetch releveNote.serieEtude left join fetch releveNote.niveauEtude where releveNote.id =:id"
    )
    Optional<ReleveNote> findOneWithToOneRelationships(@Param("id") Long id);
}
