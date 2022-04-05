package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemandeMatEtab.
 */
@Entity
@Table(name = "demande_mat_etab")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeMatEtab implements Serializable {

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

    @JsonIgnoreProperties(value = { "bons", "personnel", "classes", "commune", "inspection" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeMatEtab id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return this.objet;
    }

    public DemandeMatEtab objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDescription() {
        return this.description;
    }

    public DemandeMatEtab description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDemande() {
        return this.dateDemande;
    }

    public DemandeMatEtab dateDemande(LocalDate dateDemande) {
        this.setDateDemande(dateDemande);
        return this;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public DemandeMatEtab etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeMatEtab)) {
            return false;
        }
        return id != null && id.equals(((DemandeMatEtab) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeMatEtab{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateDemande='" + getDateDemande() + "'" +
            "}";
    }
}
