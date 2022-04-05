package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.CarteScolaire;
import sn.seye.gesmat.mefpai.repository.CarteScolaireRepository;
import sn.seye.gesmat.mefpai.service.CarteScolaireService;

/**
 * Service Implementation for managing {@link CarteScolaire}.
 */
@Service
@Transactional
public class CarteScolaireServiceImpl implements CarteScolaireService {

    private final Logger log = LoggerFactory.getLogger(CarteScolaireServiceImpl.class);

    private final CarteScolaireRepository carteScolaireRepository;

    public CarteScolaireServiceImpl(CarteScolaireRepository carteScolaireRepository) {
        this.carteScolaireRepository = carteScolaireRepository;
    }

    @Override
    public CarteScolaire save(CarteScolaire carteScolaire) {
        log.debug("Request to save CarteScolaire : {}", carteScolaire);
        return carteScolaireRepository.save(carteScolaire);
    }

    @Override
    public Optional<CarteScolaire> partialUpdate(CarteScolaire carteScolaire) {
        log.debug("Request to partially update CarteScolaire : {}", carteScolaire);

        return carteScolaireRepository
            .findById(carteScolaire.getId())
            .map(existingCarteScolaire -> {
                if (carteScolaire.getLonguer() != null) {
                    existingCarteScolaire.setLonguer(carteScolaire.getLonguer());
                }
                if (carteScolaire.getLargeur() != null) {
                    existingCarteScolaire.setLargeur(carteScolaire.getLargeur());
                }
                if (carteScolaire.getDureeValidite() != null) {
                    existingCarteScolaire.setDureeValidite(carteScolaire.getDureeValidite());
                }
                if (carteScolaire.getDateCreation() != null) {
                    existingCarteScolaire.setDateCreation(carteScolaire.getDateCreation());
                }
                if (carteScolaire.getDateFin() != null) {
                    existingCarteScolaire.setDateFin(carteScolaire.getDateFin());
                }
                if (carteScolaire.getMatriculeCart() != null) {
                    existingCarteScolaire.setMatriculeCart(carteScolaire.getMatriculeCart());
                }

                return existingCarteScolaire;
            })
            .map(carteScolaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarteScolaire> findAll(Pageable pageable) {
        log.debug("Request to get all CarteScolaires");
        return carteScolaireRepository.findAll(pageable);
    }

    public Page<CarteScolaire> findAllWithEagerRelationships(Pageable pageable) {
        return carteScolaireRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarteScolaire> findOne(Long id) {
        log.debug("Request to get CarteScolaire : {}", id);
        return carteScolaireRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarteScolaire : {}", id);
        carteScolaireRepository.deleteById(id);
    }
}
