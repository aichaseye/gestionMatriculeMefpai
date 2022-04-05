package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.DemandeMatEtab;
import sn.seye.gesmat.mefpai.repository.DemandeMatEtabRepository;
import sn.seye.gesmat.mefpai.service.DemandeMatEtabService;

/**
 * Service Implementation for managing {@link DemandeMatEtab}.
 */
@Service
@Transactional
public class DemandeMatEtabServiceImpl implements DemandeMatEtabService {

    private final Logger log = LoggerFactory.getLogger(DemandeMatEtabServiceImpl.class);

    private final DemandeMatEtabRepository demandeMatEtabRepository;

    public DemandeMatEtabServiceImpl(DemandeMatEtabRepository demandeMatEtabRepository) {
        this.demandeMatEtabRepository = demandeMatEtabRepository;
    }

    @Override
    public DemandeMatEtab save(DemandeMatEtab demandeMatEtab) {
        log.debug("Request to save DemandeMatEtab : {}", demandeMatEtab);
        return demandeMatEtabRepository.save(demandeMatEtab);
    }

    @Override
    public Optional<DemandeMatEtab> partialUpdate(DemandeMatEtab demandeMatEtab) {
        log.debug("Request to partially update DemandeMatEtab : {}", demandeMatEtab);

        return demandeMatEtabRepository
            .findById(demandeMatEtab.getId())
            .map(existingDemandeMatEtab -> {
                if (demandeMatEtab.getObjet() != null) {
                    existingDemandeMatEtab.setObjet(demandeMatEtab.getObjet());
                }
                if (demandeMatEtab.getDescription() != null) {
                    existingDemandeMatEtab.setDescription(demandeMatEtab.getDescription());
                }
                if (demandeMatEtab.getDateDemande() != null) {
                    existingDemandeMatEtab.setDateDemande(demandeMatEtab.getDateDemande());
                }

                return existingDemandeMatEtab;
            })
            .map(demandeMatEtabRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeMatEtab> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeMatEtabs");
        return demandeMatEtabRepository.findAll(pageable);
    }

    public Page<DemandeMatEtab> findAllWithEagerRelationships(Pageable pageable) {
        return demandeMatEtabRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeMatEtab> findOne(Long id) {
        log.debug("Request to get DemandeMatEtab : {}", id);
        return demandeMatEtabRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeMatEtab : {}", id);
        demandeMatEtabRepository.deleteById(id);
    }
}
