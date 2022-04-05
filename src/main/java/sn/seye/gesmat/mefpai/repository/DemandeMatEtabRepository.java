package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.DemandeMatEtab;

/**
 * Spring Data SQL repository for the DemandeMatEtab entity.
 */
@Repository
public interface DemandeMatEtabRepository extends JpaRepository<DemandeMatEtab, Long> {
    default Optional<DemandeMatEtab> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DemandeMatEtab> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DemandeMatEtab> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct demandeMatEtab from DemandeMatEtab demandeMatEtab left join fetch demandeMatEtab.etablissement",
        countQuery = "select count(distinct demandeMatEtab) from DemandeMatEtab demandeMatEtab"
    )
    Page<DemandeMatEtab> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct demandeMatEtab from DemandeMatEtab demandeMatEtab left join fetch demandeMatEtab.etablissement")
    List<DemandeMatEtab> findAllWithToOneRelationships();

    @Query(
        "select demandeMatEtab from DemandeMatEtab demandeMatEtab left join fetch demandeMatEtab.etablissement where demandeMatEtab.id =:id"
    )
    Optional<DemandeMatEtab> findOneWithToOneRelationships(@Param("id") Long id);
}
