package sn.seye.gesmat.mefpai.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.seye.gesmat.mefpai.domain.FiliereEtude;

/**
 * Spring Data SQL repository for the FiliereEtude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiliereEtudeRepository extends JpaRepository<FiliereEtude, Long> {}
