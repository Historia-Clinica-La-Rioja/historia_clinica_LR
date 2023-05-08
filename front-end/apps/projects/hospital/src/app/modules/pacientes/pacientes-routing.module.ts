import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RoleGuard } from '@core/guards/RoleGuard';

import { ERole } from '@api-rest/api-model';

import { EditPatientComponent } from './routes/edit-patient/edit-patient.component';
import { HomeComponent } from './routes/home/home.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';
import { NewTemporaryPatientComponent } from './routes/new-temporary-patient/new-temporary-patient.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SearchComponent } from './routes/search/search.component';
import { EmergencyCareTemporaryPatientProfile } from './routes/emergency-care-temporary-patient-profile/emergency-care-temporary-patient-profile';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent
			},
			{
				path: 'search',
				component: SearchComponent
			},
			{
				path: 'new',
				component: NewPatientComponent
			},
			{
				path: 'temporary',
				component: NewTemporaryPatientComponent
			},
			{
				path: 'profile/:id',
				component: ProfileComponent
			},
			{
				path: 'temporal-guardia/profile/:id',
				component: EmergencyCareTemporaryPatientProfile
			},
			{
				path: 'edit',
				component: EditPatientComponent
			},
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRATIVO_RED_DE_IMAGENES, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, ERole.PRESCRIPTOR, ERole.AUDITOR_MPI] }
	}
];

@NgModule({
	imports: [
		ReactiveFormsModule,
		FormsModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule]
})
export class PacientesRoutingModule {
}
