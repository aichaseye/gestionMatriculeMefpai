package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.CarteScolaire;

/**
 * Spring Data SQL repository for the CarteScolaire entity.
 */
@Repository
public interface CarteScolaireRepository extends JpaRepository<CarteScolaire, Long> {
    default Optional<CarteScolaire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CarteScolaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CarteScolaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct carteScolaire from CarteScolaire carteScolaire left join fetch carteScolaire.apprenant",
        countQuery = "select count(distinct carteScolaire) from CarteScolaire carteScolaire"
    )
    Page<CarteScolaire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct carteScolaire from CarteScolaire carteScolaire left join fetch carteScolaire.apprenant")
    List<CarteScolaire> findAllWithToOneRelationships();

    @Query("select carteScolaire from CarteScolaire carteScolaire left join fetch carteScolaire.apprenant where carteScolaire.id =:id")
    Optional<CarteScolaire> findOneWithToOneRelationships(@Param("id") Long id);
}
