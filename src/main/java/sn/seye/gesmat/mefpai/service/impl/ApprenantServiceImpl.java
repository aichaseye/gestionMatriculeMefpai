package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Apprenant;
import sn.seye.gesmat.mefpai.repository.ApprenantRepository;
import sn.seye.gesmat.mefpai.service.ApprenantService;

/**
 * Service Implementation for managing {@link Apprenant}.
 */
@Service
@Transactional
public class ApprenantServiceImpl implements ApprenantService {

    private final Logger log = LoggerFactory.getLogger(ApprenantServiceImpl.class);

    private final ApprenantRepository apprenantRepository;

    public ApprenantServiceImpl(ApprenantRepository apprenantRepository) {
        this.apprenantRepository = apprenantRepository;
    }

    @Override
    public Apprenant save(Apprenant apprenant) {
        log.debug("Request to save Apprenant : {}", apprenant);
        return apprenantRepository.save(apprenant);
    }

    @Override
    public Optional<Apprenant> partialUpdate(Apprenant apprenant) {
        log.debug("Request to partially update Apprenant : {}", apprenant);

        return apprenantRepository
            .findById(apprenant.getId())
            .map(existingApprenant -> {
                if (apprenant.getNomCompletApp() != null) {
                    existingApprenant.setNomCompletApp(apprenant.getNomCompletApp());
                }
                if (apprenant.getEmail() != null) {
                    existingApprenant.setEmail(apprenant.getEmail());
                }
                if (apprenant.getTelephone() != null) {
                    existingApprenant.setTelephone(apprenant.getTelephone());
                }
                if (apprenant.getDateNaissance() != null) {
                    existingApprenant.setDateNaissance(apprenant.getDateNaissance());
                }
                if (apprenant.getLieuNaissance() != null) {
                    existingApprenant.setLieuNaissance(apprenant.getLieuNaissance());
                }
                if (apprenant.getNumActeNaissance() != null) {
                    existingApprenant.setNumActeNaissance(apprenant.getNumActeNaissance());
                }
                if (apprenant.getPhoto() != null) {
                    existingApprenant.setPhoto(apprenant.getPhoto());
                }
                if (apprenant.getPhotoContentType() != null) {
                    existingApprenant.setPhotoContentType(apprenant.getPhotoContentType());
                }
                if (apprenant.getSexe() != null) {
                    existingApprenant.setSexe(apprenant.getSexe());
                }
                if (apprenant.getAdresse() != null) {
                    existingApprenant.setAdresse(apprenant.getAdresse());
                }
                if (apprenant.getStatut() != null) {
                    existingApprenant.setStatut(apprenant.getStatut());
                }
                if (apprenant.getSituationMatrimoniale() != null) {
                    existingApprenant.setSituationMatrimoniale(apprenant.getSituationMatrimoniale());
                }
                if (apprenant.getMatriculeApp() != null) {
                    existingApprenant.setMatriculeApp(apprenant.getMatriculeApp());
                }
                if (apprenant.getNationalite() != null) {
                    existingApprenant.setNationalite(apprenant.getNationalite());
                }

                return existingApprenant;
            })
            .map(apprenantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Apprenant> findAll(Pageable pageable) {
        log.debug("Request to get all Apprenants");
        return apprenantRepository.findAll(pageable);
    }

    public Page<Apprenant> findAllWithEagerRelationships(Pageable pageable) {
        return apprenantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Apprenant> findOne(Long id) {
        log.debug("Request to get Apprenant : {}", id);
        return apprenantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Apprenant : {}", id);
        apprenantRepository.deleteById(id);
    }
}
