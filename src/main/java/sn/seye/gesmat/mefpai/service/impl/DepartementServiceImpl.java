package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Departement;
import sn.seye.gesmat.mefpai.repository.DepartementRepository;
import sn.seye.gesmat.mefpai.service.DepartementService;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    @Override
    public Departement save(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        return departementRepository.save(departement);
    }

    @Override
    public Optional<Departement> partialUpdate(Departement departement) {
        log.debug("Request to partially update Departement : {}", departement);

        return departementRepository
            .findById(departement.getId())
            .map(existingDepartement -> {
                if (departement.getNomDep() != null) {
                    existingDepartement.setNomDep(departement.getNomDep());
                }

                return existingDepartement;
            })
            .map(departementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Departement> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable);
    }

    public Page<Departement> findAllWithEagerRelationships(Pageable pageable) {
        return departementRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departement> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
    }
}
