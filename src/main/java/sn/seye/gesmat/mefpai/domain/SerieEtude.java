package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.Serie;

/**
 * A SerieEtude.
 */
@Entity
@Table(name = "serie_etude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SerieEtude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false)
    private Serie serie;

    @OneToMany(mappedBy = "serieEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude" }, allowSetters = true)
    private Set<Diplome> diplomes = new HashSet<>();

    @OneToMany(mappedBy = "serieEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude", "noteProgrammes" }, allowSetters = true)
    private Set<ReleveNote> releveNotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SerieEtude id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return this.serie;
    }

    public SerieEtude serie(Serie serie) {
        this.setSerie(serie);
        return this;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Set<Diplome> getDiplomes() {
        return this.diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        if (this.diplomes != null) {
            this.diplomes.forEach(i -> i.setSerieEtude(null));
        }
        if (diplomes != null) {
            diplomes.forEach(i -> i.setSerieEtude(this));
        }
        this.diplomes = diplomes;
    }

    public SerieEtude diplomes(Set<Diplome> diplomes) {
        this.setDiplomes(diplomes);
        return this;
    }

    public SerieEtude addDiplome(Diplome diplome) {
        this.diplomes.add(diplome);
        diplome.setSerieEtude(this);
        return this;
    }

    public SerieEtude removeDiplome(Diplome diplome) {
        this.diplomes.remove(diplome);
        diplome.setSerieEtude(null);
        return this;
    }

    public Set<ReleveNote> getReleveNotes() {
        return this.releveNotes;
    }

    public void setReleveNotes(Set<ReleveNote> releveNotes) {
        if (this.releveNotes != null) {
            this.releveNotes.forEach(i -> i.setSerieEtude(null));
        }
        if (releveNotes != null) {
            releveNotes.forEach(i -> i.setSerieEtude(this));
        }
        this.releveNotes = releveNotes;
    }

    public SerieEtude releveNotes(Set<ReleveNote> releveNotes) {
        this.setReleveNotes(releveNotes);
        return this;
    }

    public SerieEtude addReleveNote(ReleveNote releveNote) {
        this.releveNotes.add(releveNote);
        releveNote.setSerieEtude(this);
        return this;
    }

    public SerieEtude removeReleveNote(ReleveNote releveNote) {
        this.releveNotes.remove(releveNote);
        releveNote.setSerieEtude(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SerieEtude)) {
            return false;
        }
        return id != null && id.equals(((SerieEtude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SerieEtude{" +
            "id=" + getId() +
            ", serie='" + getSerie() + "'" +
            "}";
    }
}
