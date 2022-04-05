package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;

/**
 * Spring Data SQL repository for the NoteProgramme entity.
 */
@Repository
public interface NoteProgrammeRepository extends NoteProgrammeRepositoryWithBagRelationships, JpaRepository<NoteProgramme, Long> {
    default Optional<NoteProgramme> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NoteProgramme> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NoteProgramme> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
