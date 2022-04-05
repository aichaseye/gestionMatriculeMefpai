package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Personnel;
import sn.seye.gesmat.mefpai.repository.PersonnelRepository;
import sn.seye.gesmat.mefpai.service.PersonnelService;

/**
 * Service Implementation for managing {@link Personnel}.
 */
@Service
@Transactional
public class PersonnelServiceImpl implements PersonnelService {

    private final Logger log = LoggerFactory.getLogger(PersonnelServiceImpl.class);

    private final PersonnelRepository personnelRepository;

    public PersonnelServiceImpl(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    @Override
    public Personnel save(Personnel personnel) {
        log.debug("Request to save Personnel : {}", personnel);
        return personnelRepository.save(personnel);
    }

    @Override
    public Optional<Personnel> partialUpdate(Personnel personnel) {
        log.debug("Request to partially update Personnel : {}", personnel);

        return personnelRepository
            .findById(personnel.getId())
            .map(existingPersonnel -> {
                if (personnel.getNomPers() != null) {
                    existingPersonnel.setNomPers(personnel.getNomPers());
                }
                if (personnel.getPrenomPers() != null) {
                    existingPersonnel.setPrenomPers(personnel.getPrenomPers());
                }
                if (personnel.getEmail() != null) {
                    existingPersonnel.setEmail(personnel.getEmail());
                }

                return existingPersonnel;
            })
            .map(personnelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personnel> findAll(Pageable pageable) {
        log.debug("Request to get all Personnel");
        return personnelRepository.findAll(pageable);
    }

    public Page<Personnel> findAllWithEagerRelationships(Pageable pageable) {
        return personnelRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personnel> findOne(Long id) {
        log.debug("Request to get Personnel : {}", id);
        return personnelRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personnel : {}", id);
        personnelRepository.deleteById(id);
    }
}
