package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personnel.
 */
@Entity
@Table(name = "personnel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.", required = true)
    @NotNull
    @Column(name = "nom_pers", nullable = false)
    private String nomPers;

    @NotNull
    @Column(name = "prenom_pers", nullable = false)
    private String prenomPers;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bons", "personnel", "classes", "commune", "inspection" }, allowSetters = true)
    private Etablissement etablissement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "personnel", "etablissements", "commune" }, allowSetters = true)
    private Inspection inspection;

    @ManyToOne
    @JsonIgnoreProperties(value = { "personnel" }, allowSetters = true)
    private Poste poste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personnel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPers() {
        return this.nomPers;
    }

    public Personnel nomPers(String nomPers) {
        this.setNomPers(nomPers);
        return this;
    }

    public void setNomPers(String nomPers) {
        this.nomPers = nomPers;
    }

    public String getPrenomPers() {
        return this.prenomPers;
    }

    public Personnel prenomPers(String prenomPers) {
        this.setPrenomPers(prenomPers);
        return this;
    }

    public void setPrenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
    }

    public String getEmail() {
        return this.email;
    }

    public Personnel email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Personnel etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    public Inspection getInspection() {
        return this.inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Personnel inspection(Inspection inspection) {
        this.setInspection(inspection);
        return this;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Personnel poste(Poste poste) {
        this.setPoste(poste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personnel)) {
            return false;
        }
        return id != null && id.equals(((Personnel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personnel{" +
            "id=" + getId() +
            ", nomPers='" + getNomPers() + "'" +
            ", prenomPers='" + getPrenomPers() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
