package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.TypeInspection;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "inspection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_insp", nullable = false)
    private String nomInsp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_insp", nullable = false)
    private TypeInspection typeInsp;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "longitude")
    private Long longitude;

    @OneToMany(mappedBy = "inspection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "inspection", "poste" }, allowSetters = true)
    private Set<Personnel> personnel = new HashSet<>();

    @OneToMany(mappedBy = "inspection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bons", "personnel", "classes", "commune", "inspection" }, allowSetters = true)
    private Set<Etablissement> etablissements = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections", "etablissements", "departement" }, allowSetters = true)
    private Commune commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inspection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomInsp() {
        return this.nomInsp;
    }

    public Inspection nomInsp(String nomInsp) {
        this.setNomInsp(nomInsp);
        return this;
    }

    public void setNomInsp(String nomInsp) {
        this.nomInsp = nomInsp;
    }

    public TypeInspection getTypeInsp() {
        return this.typeInsp;
    }

    public Inspection typeInsp(TypeInspection typeInsp) {
        this.setTypeInsp(typeInsp);
        return this;
    }

    public void setTypeInsp(TypeInspection typeInsp) {
        this.typeInsp = typeInsp;
    }

    public Long getLatitude() {
        return this.latitude;
    }

    public Inspection latitude(Long latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return this.longitude;
    }

    public Inspection longitude(Long longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Set<Personnel> getPersonnel() {
        return this.personnel;
    }

    public void setPersonnel(Set<Personnel> personnel) {
        if (this.personnel != null) {
            this.personnel.forEach(i -> i.setInspection(null));
        }
        if (personnel != null) {
            personnel.forEach(i -> i.setInspection(this));
        }
        this.personnel = personnel;
    }

    public Inspection personnel(Set<Personnel> personnel) {
        this.setPersonnel(personnel);
        return this;
    }

    public Inspection addPersonnel(Personnel personnel) {
        this.personnel.add(personnel);
        personnel.setInspection(this);
        return this;
    }

    public Inspection removePersonnel(Personnel personnel) {
        this.personnel.remove(personnel);
        personnel.setInspection(null);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.setInspection(null));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.setInspection(this));
        }
        this.etablissements = etablissements;
    }

    public Inspection etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Inspection addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.setInspection(this);
        return this;
    }

    public Inspection removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.setInspection(null);
        return this;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Inspection commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inspection)) {
            return false;
        }
        return id != null && id.equals(((Inspection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inspection{" +
            "id=" + getId() +
            ", nomInsp='" + getNomInsp() + "'" +
            ", typeInsp='" + getTypeInsp() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
