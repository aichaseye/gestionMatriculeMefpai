package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;

public interface NoteProgrammeRepositoryWithBagRelationships {
    Optional<NoteProgramme> fetchBagRelationships(Optional<NoteProgramme> noteProgramme);

    List<NoteProgramme> fetchBagRelationships(List<NoteProgramme> noteProgrammes);

    Page<NoteProgramme> fetchBagRelationships(Page<NoteProgramme> noteProgrammes);
}
