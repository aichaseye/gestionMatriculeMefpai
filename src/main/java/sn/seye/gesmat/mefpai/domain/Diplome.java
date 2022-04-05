package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.NomDiplome;

/**
 * A Diplome.
 */
@Entity
@Table(name = "diplome")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Diplome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom", nullable = false)
    private NomDiplome nom;

    @NotNull
    @Column(name = "matricule_diplome", nullable = false)
    private String matriculeDiplome;

    @Column(name = "annee")
    private LocalDate annee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandeMatApp", "inscriptions", "releveNotes", "diplomes" }, allowSetters = true)
    private Apprenant apprenant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "diplomes", "releveNotes" }, allowSetters = true)
    private FiliereEtude filiereEtude;

    @ManyToOne
    @JsonIgnoreProperties(value = { "diplomes", "releveNotes" }, allowSetters = true)
    private SerieEtude serieEtude;

    @ManyToOne
    @JsonIgnoreProperties(value = { "diplomes", "releveNotes" }, allowSetters = true)
    private NiveauEtude niveauEtude;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Diplome id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomDiplome getNom() {
        return this.nom;
    }

    public Diplome nom(NomDiplome nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(NomDiplome nom) {
        this.nom = nom;
    }

    public String getMatriculeDiplome() {
        return this.matriculeDiplome;
    }

    public Diplome matriculeDiplome(String matriculeDiplome) {
        this.setMatriculeDiplome(matriculeDiplome);
        return this;
    }

    public void setMatriculeDiplome(String matriculeDiplome) {
        this.matriculeDiplome = matriculeDiplome;
    }

    public LocalDate getAnnee() {
        return this.annee;
    }

    public Diplome annee(LocalDate annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public Apprenant getApprenant() {
        return this.apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Diplome apprenant(Apprenant apprenant) {
        this.setApprenant(apprenant);
        return this;
    }

    public FiliereEtude getFiliereEtude() {
        return this.filiereEtude;
    }

    public void setFiliereEtude(FiliereEtude filiereEtude) {
        this.filiereEtude = filiereEtude;
    }

    public Diplome filiereEtude(FiliereEtude filiereEtude) {
        this.setFiliereEtude(filiereEtude);
        return this;
    }

    public SerieEtude getSerieEtude() {
        return this.serieEtude;
    }

    public void setSerieEtude(SerieEtude serieEtude) {
        this.serieEtude = serieEtude;
    }

    public Diplome serieEtude(SerieEtude serieEtude) {
        this.setSerieEtude(serieEtude);
        return this;
    }

    public NiveauEtude getNiveauEtude() {
        return this.niveauEtude;
    }

    public void setNiveauEtude(NiveauEtude niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public Diplome niveauEtude(NiveauEtude niveauEtude) {
        this.setNiveauEtude(niveauEtude);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diplome)) {
            return false;
        }
        return id != null && id.equals(((Diplome) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Diplome{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", matriculeDiplome='" + getMatriculeDiplome() + "'" +
            ", annee='" + getAnnee() + "'" +
            "}";
    }
}
