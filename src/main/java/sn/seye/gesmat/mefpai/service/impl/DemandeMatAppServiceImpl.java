package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.DemandeMatApp;
import sn.seye.gesmat.mefpai.repository.DemandeMatAppRepository;
import sn.seye.gesmat.mefpai.service.DemandeMatAppService;

/**
 * Service Implementation for managing {@link DemandeMatApp}.
 */
@Service
@Transactional
public class DemandeMatAppServiceImpl implements DemandeMatAppService {

    private final Logger log = LoggerFactory.getLogger(DemandeMatAppServiceImpl.class);

    private final DemandeMatAppRepository demandeMatAppRepository;

    public DemandeMatAppServiceImpl(DemandeMatAppRepository demandeMatAppRepository) {
        this.demandeMatAppRepository = demandeMatAppRepository;
    }

    @Override
    public DemandeMatApp save(DemandeMatApp demandeMatApp) {
        log.debug("Request to save DemandeMatApp : {}", demandeMatApp);
        return demandeMatAppRepository.save(demandeMatApp);
    }

    @Override
    public Optional<DemandeMatApp> partialUpdate(DemandeMatApp demandeMatApp) {
        log.debug("Request to partially update DemandeMatApp : {}", demandeMatApp);

        return demandeMatAppRepository
            .findById(demandeMatApp.getId())
            .map(existingDemandeMatApp -> {
                if (demandeMatApp.getObjet() != null) {
                    existingDemandeMatApp.setObjet(demandeMatApp.getObjet());
                }
                if (demandeMatApp.getDescription() != null) {
                    existingDemandeMatApp.setDescription(demandeMatApp.getDescription());
                }
                if (demandeMatApp.getDateDemande() != null) {
                    existingDemandeMatApp.setDateDemande(demandeMatApp.getDateDemande());
                }

                return existingDemandeMatApp;
            })
            .map(demandeMatAppRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeMatApp> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeMatApps");
        return demandeMatAppRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeMatApp> findOne(Long id) {
        log.debug("Request to get DemandeMatApp : {}", id);
        return demandeMatAppRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeMatApp : {}", id);
        demandeMatAppRepository.deleteById(id);
    }
}
