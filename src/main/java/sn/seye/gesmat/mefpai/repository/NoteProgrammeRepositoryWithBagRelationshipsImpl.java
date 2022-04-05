package sn.seye.gesmat.mefpai.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NoteProgrammeRepositoryWithBagRelationshipsImpl implements NoteProgrammeRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<NoteProgramme> fetchBagRelationships(Optional<NoteProgramme> noteProgramme) {
        return noteProgramme.map(this::fetchReleveNotes);
    }

    @Override
    public Page<NoteProgramme> fetchBagRelationships(Page<NoteProgramme> noteProgrammes) {
        return new PageImpl<>(
            fetchBagRelationships(noteProgrammes.getContent()),
            noteProgrammes.getPageable(),
            noteProgrammes.getTotalElements()
        );
    }

    @Override
    public List<NoteProgramme> fetchBagRelationships(List<NoteProgramme> noteProgrammes) {
        return Optional.of(noteProgrammes).map(this::fetchReleveNotes).get();
    }

    NoteProgramme fetchReleveNotes(NoteProgramme result) {
        return entityManager
            .createQuery(
                "select noteProgramme from NoteProgramme noteProgramme left join fetch noteProgramme.releveNotes where noteProgramme is :noteProgramme",
                NoteProgramme.class
            )
            .setParameter("noteProgramme", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<NoteProgramme> fetchReleveNotes(List<NoteProgramme> noteProgrammes) {
        return entityManager
            .createQuery(
                "select distinct noteProgramme from NoteProgramme noteProgramme left join fetch noteProgramme.releveNotes where noteProgramme in :noteProgrammes",
                NoteProgramme.class
            )
            .setParameter("noteProgrammes", noteProgrammes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
