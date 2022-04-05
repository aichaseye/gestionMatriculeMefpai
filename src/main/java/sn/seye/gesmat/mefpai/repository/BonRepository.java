package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.Bon;

/**
 * Spring Data SQL repository for the Bon entity.
 */
@Repository
public interface BonRepository extends JpaRepository<Bon, Long> {
    default Optional<Bon> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Bon> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Bon> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct bon from Bon bon left join fetch bon.etablissement left join fetch bon.matiere",
        countQuery = "select count(distinct bon) from Bon bon"
    )
    Page<Bon> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct bon from Bon bon left join fetch bon.etablissement left join fetch bon.matiere")
    List<Bon> findAllWithToOneRelationships();

    @Query("select bon from Bon bon left join fetch bon.etablissement left join fetch bon.matiere where bon.id =:id")
    Optional<Bon> findOneWithToOneRelationships(@Param("id") Long id);
}
