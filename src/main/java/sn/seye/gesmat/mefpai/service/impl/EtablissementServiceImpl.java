package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Etablissement;
import sn.seye.gesmat.mefpai.repository.EtablissementRepository;
import sn.seye.gesmat.mefpai.service.EtablissementService;

/**
 * Service Implementation for managing {@link Etablissement}.
 */
@Service
@Transactional
public class EtablissementServiceImpl implements EtablissementService {

    private final Logger log = LoggerFactory.getLogger(EtablissementServiceImpl.class);

    private final EtablissementRepository etablissementRepository;

    public EtablissementServiceImpl(EtablissementRepository etablissementRepository) {
        this.etablissementRepository = etablissementRepository;
    }

    @Override
    public Etablissement save(Etablissement etablissement) {
        log.debug("Request to save Etablissement : {}", etablissement);
        return etablissementRepository.save(etablissement);
    }

    @Override
    public Optional<Etablissement> partialUpdate(Etablissement etablissement) {
        log.debug("Request to partially update Etablissement : {}", etablissement);

        return etablissementRepository
            .findById(etablissement.getId())
            .map(existingEtablissement -> {
                if (etablissement.getNomEtab() != null) {
                    existingEtablissement.setNomEtab(etablissement.getNomEtab());
                }
                if (etablissement.getTypeEtab() != null) {
                    existingEtablissement.setTypeEtab(etablissement.getTypeEtab());
                }
                if (etablissement.getStatut() != null) {
                    existingEtablissement.setStatut(etablissement.getStatut());
                }
                if (etablissement.getLatitude() != null) {
                    existingEtablissement.setLatitude(etablissement.getLatitude());
                }
                if (etablissement.getLongitude() != null) {
                    existingEtablissement.setLongitude(etablissement.getLongitude());
                }
                if (etablissement.getMatriculeEtab() != null) {
                    existingEtablissement.setMatriculeEtab(etablissement.getMatriculeEtab());
                }

                return existingEtablissement;
            })
            .map(etablissementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etablissement> findAll(Pageable pageable) {
        log.debug("Request to get all Etablissements");
        return etablissementRepository.findAll(pageable);
    }

    public Page<Etablissement> findAllWithEagerRelationships(Pageable pageable) {
        return etablissementRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etablissement> findOne(Long id) {
        log.debug("Request to get Etablissement : {}", id);
        return etablissementRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etablissement : {}", id);
        etablissementRepository.deleteById(id);
    }
}
