import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuditoriaRoutingModule } from './auditoria-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { ListCardPatientDuplicateComponent } from './components/list-card-patient-duplicate/list-card-patient-duplicate.component';
import { ControlPatientDuplicateComponent } from './routes/control-patient-duplicate/control-patient-duplicate.component';
import { PatientFusionComponent } from './routes/patient-fusion/patient-fusion.component';
import { WarningFusionComponent } from './dialogs/warning-fusion/warning-fusion.component';
import { ConfirmedFusionComponent } from './dialogs/confirmed-fusion/confirmed-fusion.component';
import { PatientProfilePopupComponent } from './dialogs/patient-profile-popup/patient-profile-popup.component';
import { EmpadronamientoComponent } from './routes/empadronamiento/empadronamiento.component';
import { PacientesModule } from '@pacientes/pacientes.module';



@NgModule({
  declarations: [
    HomeComponent,
    ListCardPatientDuplicateComponent,
    ControlPatientDuplicateComponent,
    PatientFusionComponent,
    WarningFusionComponent,
    ConfirmedFusionComponent,
    PatientProfilePopupComponent,
    EmpadronamientoComponent
  ],
  imports: [
    CommonModule,
    AuditoriaRoutingModule,
	LazyMaterialModule,
	PresentationModule,
	PacientesModule,
  ],
})
export class AuditoriaModule { }
