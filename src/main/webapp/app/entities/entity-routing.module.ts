import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pays',
        data: { pageTitle: 'matriculeEfptBdApp.pays.home.title' },
        loadChildren: () => import('./pays/pays.module').then(m => m.PaysModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'matriculeEfptBdApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'departement',
        data: { pageTitle: 'matriculeEfptBdApp.departement.home.title' },
        loadChildren: () => import('./departement/departement.module').then(m => m.DepartementModule),
      },
      {
        path: 'commune',
        data: { pageTitle: 'matriculeEfptBdApp.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'inspection',
        data: { pageTitle: 'matriculeEfptBdApp.inspection.home.title' },
        loadChildren: () => import('./inspection/inspection.module').then(m => m.InspectionModule),
      },
      {
        path: 'etablissement',
        data: { pageTitle: 'matriculeEfptBdApp.etablissement.home.title' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'apprenant',
        data: { pageTitle: 'matriculeEfptBdApp.apprenant.home.title' },
        loadChildren: () => import('./apprenant/apprenant.module').then(m => m.ApprenantModule),
      },
      {
        path: 'inscription',
        data: { pageTitle: 'matriculeEfptBdApp.inscription.home.title' },
        loadChildren: () => import('./inscription/inscription.module').then(m => m.InscriptionModule),
      },
      {
        path: 'personnel',
        data: { pageTitle: 'matriculeEfptBdApp.personnel.home.title' },
        loadChildren: () => import('./personnel/personnel.module').then(m => m.PersonnelModule),
      },
      {
        path: 'poste',
        data: { pageTitle: 'matriculeEfptBdApp.poste.home.title' },
        loadChildren: () => import('./poste/poste.module').then(m => m.PosteModule),
      },
      {
        path: 'classe',
        data: { pageTitle: 'matriculeEfptBdApp.classe.home.title' },
        loadChildren: () => import('./classe/classe.module').then(m => m.ClasseModule),
      },
      {
        path: 'carte-scolaire',
        data: { pageTitle: 'matriculeEfptBdApp.carteScolaire.home.title' },
        loadChildren: () => import('./carte-scolaire/carte-scolaire.module').then(m => m.CarteScolaireModule),
      },
      {
        path: 'diplome',
        data: { pageTitle: 'matriculeEfptBdApp.diplome.home.title' },
        loadChildren: () => import('./diplome/diplome.module').then(m => m.DiplomeModule),
      },
      {
        path: 'niveau-etude',
        data: { pageTitle: 'matriculeEfptBdApp.niveauEtude.home.title' },
        loadChildren: () => import('./niveau-etude/niveau-etude.module').then(m => m.NiveauEtudeModule),
      },
      {
        path: 'serie-etude',
        data: { pageTitle: 'matriculeEfptBdApp.serieEtude.home.title' },
        loadChildren: () => import('./serie-etude/serie-etude.module').then(m => m.SerieEtudeModule),
      },
      {
        path: 'filiere-etude',
        data: { pageTitle: 'matriculeEfptBdApp.filiereEtude.home.title' },
        loadChildren: () => import('./filiere-etude/filiere-etude.module').then(m => m.FiliereEtudeModule),
      },
      {
        path: 'releve-note',
        data: { pageTitle: 'matriculeEfptBdApp.releveNote.home.title' },
        loadChildren: () => import('./releve-note/releve-note.module').then(m => m.ReleveNoteModule),
      },
      {
        path: 'note-programme',
        data: { pageTitle: 'matriculeEfptBdApp.noteProgramme.home.title' },
        loadChildren: () => import('./note-programme/note-programme.module').then(m => m.NoteProgrammeModule),
      },
      {
        path: 'demande-mat-app',
        data: { pageTitle: 'matriculeEfptBdApp.demandeMatApp.home.title' },
        loadChildren: () => import('./demande-mat-app/demande-mat-app.module').then(m => m.DemandeMatAppModule),
      },
      {
        path: 'demande-mat-etab',
        data: { pageTitle: 'matriculeEfptBdApp.demandeMatEtab.home.title' },
        loadChildren: () => import('./demande-mat-etab/demande-mat-etab.module').then(m => m.DemandeMatEtabModule),
      },
      {
        path: 'matiere',
        data: { pageTitle: 'matriculeEfptBdApp.matiere.home.title' },
        loadChildren: () => import('./matiere/matiere.module').then(m => m.MatiereModule),
      },
      {
        path: 'image',
        data: { pageTitle: 'matriculeEfptBdApp.image.home.title' },
        loadChildren: () => import('./image/image.module').then(m => m.ImageModule),
      },
      {
        path: 'bon',
        data: { pageTitle: 'matriculeEfptBdApp.bon.home.title' },
        loadChildren: () => import('./bon/bon.module').then(m => m.BonModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
