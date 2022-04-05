package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Classe.
 */
@Entity
@Table(name = "classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_classe", nullable = false)
    private String nomClasse;

    @OneToMany(mappedBy = "classe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "classe" }, allowSetters = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bons", "personnel", "classes", "commune", "inspection" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClasse() {
        return this.nomClasse;
    }

    public Classe nomClasse(String nomClasse) {
        this.setNomClasse(nomClasse);
        return this;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setClasse(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setClasse(this));
        }
        this.inscriptions = inscriptions;
    }

    public Classe inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Classe addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setClasse(this);
        return this;
    }

    public Classe removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setClasse(null);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Classe etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classe)) {
            return false;
        }
        return id != null && id.equals(((Classe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classe{" +
            "id=" + getId() +
            ", nomClasse='" + getNomClasse() + "'" +
            "}";
    }
}
