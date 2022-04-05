package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.Sexe;
import sn.seye.gesmat.mefpai.domain.enumeration.StatutApp;

/**
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "apprenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Apprenant implements Serializable {

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
    @Column(name = "nom_complet_app", nullable = false)
    private String nomCompletApp;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "num_acte_naissance")
    private Integer numActeNaissance;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutApp statut;

    @Column(name = "situation_matrimoniale")
    private String situationMatrimoniale;

    @Column(name = "matricule_app")
    private String matriculeApp;

    @NotNull
    @Column(name = "nationalite", nullable = false)
    private String nationalite;

    @OneToOne
    @JoinColumn(unique = true)
    private DemandeMatApp demandeMatApp;

    @OneToMany(mappedBy = "apprenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "classe" }, allowSetters = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    @OneToMany(mappedBy = "apprenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude", "noteProgrammes" }, allowSetters = true)
    private Set<ReleveNote> releveNotes = new HashSet<>();

    @OneToMany(mappedBy = "apprenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude" }, allowSetters = true)
    private Set<Diplome> diplomes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Apprenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCompletApp() {
        return this.nomCompletApp;
    }

    public Apprenant nomCompletApp(String nomCompletApp) {
        this.setNomCompletApp(nomCompletApp);
        return this;
    }

    public void setNomCompletApp(String nomCompletApp) {
        this.nomCompletApp = nomCompletApp;
    }

    public String getEmail() {
        return this.email;
    }

    public Apprenant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Apprenant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Apprenant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Apprenant lieuNaissance(String lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Integer getNumActeNaissance() {
        return this.numActeNaissance;
    }

    public Apprenant numActeNaissance(Integer numActeNaissance) {
        this.setNumActeNaissance(numActeNaissance);
        return this;
    }

    public void setNumActeNaissance(Integer numActeNaissance) {
        this.numActeNaissance = numActeNaissance;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Apprenant photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Apprenant photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Apprenant sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Apprenant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public StatutApp getStatut() {
        return this.statut;
    }

    public Apprenant statut(StatutApp statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutApp statut) {
        this.statut = statut;
    }

    public String getSituationMatrimoniale() {
        return this.situationMatrimoniale;
    }

    public Apprenant situationMatrimoniale(String situationMatrimoniale) {
        this.setSituationMatrimoniale(situationMatrimoniale);
        return this;
    }

    public void setSituationMatrimoniale(String situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public String getMatriculeApp() {
        return this.matriculeApp;
    }

    public Apprenant matriculeApp(String matriculeApp) {
        this.setMatriculeApp(matriculeApp);
        return this;
    }

    public void setMatriculeApp(String matriculeApp) {
        this.matriculeApp = matriculeApp;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Apprenant nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public DemandeMatApp getDemandeMatApp() {
        return this.demandeMatApp;
    }

    public void setDemandeMatApp(DemandeMatApp demandeMatApp) {
        this.demandeMatApp = demandeMatApp;
    }

    public Apprenant demandeMatApp(DemandeMatApp demandeMatApp) {
        this.setDemandeMatApp(demandeMatApp);
        return this;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setApprenant(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setApprenant(this));
        }
        this.inscriptions = inscriptions;
    }

    public Apprenant inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Apprenant addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setApprenant(this);
        return this;
    }

    public Apprenant removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setApprenant(null);
        return this;
    }

    public Set<ReleveNote> getReleveNotes() {
        return this.releveNotes;
    }

    public void setReleveNotes(Set<ReleveNote> releveNotes) {
        if (this.releveNotes != null) {
            this.releveNotes.forEach(i -> i.setApprenant(null));
        }
        if (releveNotes != null) {
            releveNotes.forEach(i -> i.setApprenant(this));
        }
        this.releveNotes = releveNotes;
    }

    public Apprenant releveNotes(Set<ReleveNote> releveNotes) {
        this.setReleveNotes(releveNotes);
        return this;
    }

    public Apprenant addReleveNote(ReleveNote releveNote) {
        this.releveNotes.add(releveNote);
        releveNote.setApprenant(this);
        return this;
    }

    public Apprenant removeReleveNote(ReleveNote releveNote) {
        this.releveNotes.remove(releveNote);
        releveNote.setApprenant(null);
        return this;
    }

    public Set<Diplome> getDiplomes() {
        return this.diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        if (this.diplomes != null) {
            this.diplomes.forEach(i -> i.setApprenant(null));
        }
        if (diplomes != null) {
            diplomes.forEach(i -> i.setApprenant(this));
        }
        this.diplomes = diplomes;
    }

    public Apprenant diplomes(Set<Diplome> diplomes) {
        this.setDiplomes(diplomes);
        return this;
    }

    public Apprenant addDiplome(Diplome diplome) {
        this.diplomes.add(diplome);
        diplome.setApprenant(this);
        return this;
    }

    public Apprenant removeDiplome(Diplome diplome) {
        this.diplomes.remove(diplome);
        diplome.setApprenant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apprenant)) {
            return false;
        }
        return id != null && id.equals(((Apprenant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apprenant{" +
            "id=" + getId() +
            ", nomCompletApp='" + getNomCompletApp() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", numActeNaissance=" + getNumActeNaissance() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", statut='" + getStatut() + "'" +
            ", situationMatrimoniale='" + getSituationMatrimoniale() + "'" +
            ", matriculeApp='" + getMatriculeApp() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            "}";
    }
}
