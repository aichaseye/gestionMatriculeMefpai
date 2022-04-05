package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.Personnel;

/**
 * Spring Data SQL repository for the Personnel entity.
 */
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    default Optional<Personnel> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Personnel> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Personnel> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct personnel from Personnel personnel left join fetch personnel.etablissement left join fetch personnel.inspection left join fetch personnel.poste",
        countQuery = "select count(distinct personnel) from Personnel personnel"
    )
    Page<Personnel> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct personnel from Personnel personnel left join fetch personnel.etablissement left join fetch personnel.inspection left join fetch personnel.poste"
    )
    List<Personnel> findAllWithToOneRelationships();

    @Query(
        "select personnel from Personnel personnel left join fetch personnel.etablissement left join fetch personnel.inspection left join fetch personnel.poste where personnel.id =:id"
    )
    Optional<Personnel> findOneWithToOneRelationships(@Param("id") Long id);
}
