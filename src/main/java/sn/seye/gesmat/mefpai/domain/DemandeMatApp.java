package sn.seye.gesmat.mefpai.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemandeMatApp.
 */
@Entity
@Table(name = "demande_mat_app")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeMatApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "objet")
    private String objet;

    @Column(name = "description")
    private String description;

    @Column(name = "date_demande")
    private LocalDate dateDemande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeMatApp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return this.objet;
    }

    public DemandeMatApp objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDescription() {
        return this.description;
    }

    public DemandeMatApp description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDemande() {
        return this.dateDemande;
    }

    public DemandeMatApp dateDemande(LocalDate dateDemande) {
        this.setDateDemande(dateDemande);
        return this;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeMatApp)) {
            return false;
        }
        return id != null && id.equals(((DemandeMatApp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeMatApp{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateDemande='" + getDateDemande() + "'" +
            "}";
    }
}
