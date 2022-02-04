import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { InternacionPacienteComponent } from './routes/internacion-paciente/internacion-paciente.component';
import { MedicalDischargeComponent } from './components/medical-discharge/medical-discharge.component';
import { AnamnesisComponent } from './routes/anamnesis/anamnesis.component';
import { CambiarDiagnosticoPrincipalComponent } from './routes/cambiar-diagnostico-principal/cambiar-diagnostico-principal.component';
import { EpicrisisComponent } from './routes/epicrisis/epicrisis.component';
import { EvaluacionClinicaDiagnosticosComponent } from './routes/evaluacion-clinica-diagnosticos/evaluacion-clinica-diagnosticos.component';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { NewInternmentComponent } from './routes/new-internment/new-internment.component';
import { NotaEvolucionComponent } from './routes/nota-evolucion/nota-evolucion.component';
import { PatientBedRelocationComponent } from './routes/patient-bed-relocation/patient-bed-relocation.component';
import { PatientDischargeComponent } from './routes/patient-discharge/patient-discharge.component';



const routes: Routes = [
	{
		path: '',
		component: InternacionesHomeComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente',
		component: InternacionPacienteComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/anamnesis',
		component: AnamnesisComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO_ADULTO_MAYOR] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/anamnesis/:anamnesisId',
		component: AnamnesisComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO_ADULTO_MAYOR] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/nota-evolucion',
		component: NotaEvolucionComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/eval-clinica-diagnosticos/:idDiagnostico',
		component: EvaluacionClinicaDiagnosticosComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/cambiar-diag-principal',
		component: CambiarDiagnosticoPrincipalComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/epicrisis',
		component: EpicrisisComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO] }
	},
	{
		path: 'internacion/new',
		component: NewInternmentComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRATIVO] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/alta',
		canActivate: [RoleGuard],
		component: PatientDischargeComponent,
		data: { allowedRoles: [ERole.ADMINISTRATIVO] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/alta-medica',
		canActivate: [RoleGuard],
		component: MedicalDischargeComponent,
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO] }
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/pase-cama',
		canActivate: [RoleGuard],
		component: PatientBedRelocationComponent,
		data: { allowedRoles: [ERole.ADMINISTRATIVO] }
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InternacionesRoutingModule { }
