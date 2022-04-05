package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.Inscription;

/**
 * Spring Data SQL repository for the Inscription entity.
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    default Optional<Inscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inscription from Inscription inscription left join fetch inscription.apprenant left join fetch inscription.classe",
        countQuery = "select count(distinct inscription) from Inscription inscription"
    )
    Page<Inscription> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct inscription from Inscription inscription left join fetch inscription.apprenant left join fetch inscription.classe"
    )
    List<Inscription> findAllWithToOneRelationships();

    @Query(
        "select inscription from Inscription inscription left join fetch inscription.apprenant left join fetch inscription.classe where inscription.id =:id"
    )
    Optional<Inscription> findOneWithToOneRelationships(@Param("id") Long id);
}
