package sn.seye.gesmat.mefpai.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.DemandeMatApp;

/**
 * Spring Data SQL repository for the DemandeMatApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeMatAppRepository extends JpaRepository<DemandeMatApp, Long> {}
