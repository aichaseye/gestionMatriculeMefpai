package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.Region;

/**
 * Spring Data SQL repository for the Region entity.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    default Optional<Region> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Region> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Region> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct region from Region region left join fetch region.pays",
        countQuery = "select count(distinct region) from Region region"
    )
    Page<Region> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct region from Region region left join fetch region.pays")
    List<Region> findAllWithToOneRelationships();

    @Query("select region from Region region left join fetch region.pays where region.id =:id")
    Optional<Region> findOneWithToOneRelationships(@Param("id") Long id);
}
