import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CoreModule } from '@core/core.module';
import { RoleGuard } from '@core/guards/RoleGuard';

import { ERole } from '@api-rest/api-model';

import { HomeComponent } from './routes/home/home.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent
			},
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRATIVO, ERole.ENFERMERO] }
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
export class CamasRoutingModule {
}
