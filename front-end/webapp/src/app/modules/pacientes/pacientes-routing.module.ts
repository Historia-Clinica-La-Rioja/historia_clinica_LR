import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { PacientesLayoutComponent } from './pacientes-layout/pacientes-layout.component';

const routes: Routes = [
	{
		path: '',
		component: PacientesLayoutComponent,
		children: [
			{ path: '', component: HomeComponent }
		]
	}
];

@NgModule({
	imports: [
		CoreModule,
		RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PacientesRoutingModule {
}
