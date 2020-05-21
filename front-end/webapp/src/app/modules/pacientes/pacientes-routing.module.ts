import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { SearchComponent } from './routes/search/search.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';
import { NewTemporaryPatientComponent } from './routes/new-temporary-patient/new-temporary-patient.component';
import { ProfileComponent } from "./routes/profile/profile.component";
import { RoleGuard } from '@core/guards/RoleGuard';

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
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: ['ADMINISTRATIVO'] }
	}
];

@NgModule({
	imports: [
		CoreModule,
		ReactiveFormsModule,
		FormsModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule]
})
export class PacientesRoutingModule {
}
