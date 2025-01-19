import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { RoleGuard } from '@core/guards/RoleGuard';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { ERole } from '@api-rest/api-model';
import { PendingChangesGuard } from '@core/guards/PendingChangesGuard';
import { EpisodeSummaryComponent } from './routes/episode-summary/episode-summary.component';
import { PrintAmbulatoriaComponent } from './routes/print-ambulatoria/print-ambulatoria.component';
import { AuditAccessGuard } from '@historia-clinica/modules/ambulatoria/guards/AuditAccess.guard';

const ALLOWED_ROLES = [
	ERole.ESPECIALISTA_MEDICO,
	ERole.PROFESIONAL_DE_SALUD,
	ERole.ENFERMERO,
	ERole.ESPECIALISTA_EN_ODONTOLOGIA,
	ERole.PERSONAL_DE_IMAGENES,
	ERole.PERSONAL_DE_LABORATORIO,
	ERole.PERSONAL_DE_FARMACIA,
	ERole.PRESCRIPTOR,
	ERole.ABORDAJE_VIOLENCIAS
];

const PRINT_ROLES = [
	ERole.PERSONAL_DE_LEGALES
];

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent,
			},
			{
				path: 'paciente/:idPaciente/print',
				component: PrintAmbulatoriaComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: PRINT_ROLES }
			},
			{
				path: 'paciente/:idPaciente',
				component: AmbulatoriaPacienteComponent,
				canDeactivate: [PendingChangesGuard],
				canActivate: [RoleGuard, AuditAccessGuard],
				data: { allowedRoles: ALLOWED_ROLES }
			},
			{
				path: 'paciente/:idPaciente/profile',
				component: PatientProfileComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: ALLOWED_ROLES }
			},
			{
				path: 'episodio/:idEpisodio',
				component: EpisodeSummaryComponent,
			}
		],
		data: { allowedRoles: [...ALLOWED_ROLES, ...PRINT_ROLES] }
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AmbulatoriaRoutingModule { }
