import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RoleGuard } from '@core/guards/RoleGuard';

import { ERole } from '@api-rest/api-model';

import { HomeComponent } from './routes/home/home.component';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRATIVO, ERole.ENFERMERO, ERole.ADMINISTRADOR_DE_CAMAS] }
	}
];

@NgModule({
	imports: [
		RouterModule.forChild(routes)
	],
	exports: [RouterModule]
})
export class CamasRoutingModule {
}
