import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { SearchComponent } from './routes/search/search.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent
	},
	{
		path: 'search',
		component: SearchComponent
	},
	{	path: 'pacientes/new',
		component: NewPatientComponent
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
