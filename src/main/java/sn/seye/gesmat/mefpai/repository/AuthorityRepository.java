package sn.seye.gesmat.mefpai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.seye.gesmat.mefpai.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
