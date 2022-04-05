package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarteScolaire.
 */
@Entity
@Table(name = "carte_scolaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarteScolaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "longuer", nullable = false)
    private Integer longuer;

    @NotNull
    @Column(name = "largeur", nullable = false)
    private Integer largeur;

    @Column(name = "duree_validite")
    private Integer dureeValidite;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "matricule_cart")
    private String matriculeCart;

    @JsonIgnoreProperties(value = { "demandeMatApp", "inscriptions", "releveNotes", "diplomes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Apprenant apprenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarteScolaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLonguer() {
        return this.longuer;
    }

    public CarteScolaire longuer(Integer longuer) {
        this.setLonguer(longuer);
        return this;
    }

    public void setLonguer(Integer longuer) {
        this.longuer = longuer;
    }

    public Integer getLargeur() {
        return this.largeur;
    }

    public CarteScolaire largeur(Integer largeur) {
        this.setLargeur(largeur);
        return this;
    }

    public void setLargeur(Integer largeur) {
        this.largeur = largeur;
    }

    public Integer getDureeValidite() {
        return this.dureeValidite;
    }

    public CarteScolaire dureeValidite(Integer dureeValidite) {
        this.setDureeValidite(dureeValidite);
        return this;
    }

    public void setDureeValidite(Integer dureeValidite) {
        this.dureeValidite = dureeValidite;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public CarteScolaire dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public CarteScolaire dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getMatriculeCart() {
        return this.matriculeCart;
    }

    public CarteScolaire matriculeCart(String matriculeCart) {
        this.setMatriculeCart(matriculeCart);
        return this;
    }

    public void setMatriculeCart(String matriculeCart) {
        this.matriculeCart = matriculeCart;
    }

    public Apprenant getApprenant() {
        return this.apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public CarteScolaire apprenant(Apprenant apprenant) {
        this.setApprenant(apprenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarteScolaire)) {
            return false;
        }
        return id != null && id.equals(((CarteScolaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteScolaire{" +
            "id=" + getId() +
            ", longuer=" + getLonguer() +
            ", largeur=" + getLargeur() +
            ", dureeValidite=" + getDureeValidite() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", matriculeCart='" + getMatriculeCart() + "'" +
            "}";
    }
}
