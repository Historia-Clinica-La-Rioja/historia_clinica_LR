import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { ERole } from '@api-rest/api-model';
import { WorklistByTechnicalComponent } from './routes/worklist-by-technical/worklist-by-technical.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: 'lista-trabajos',
				component: WorklistByTechnicalComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.TECNICO]}
			}
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class ImageNetworkRoutingModule { }
