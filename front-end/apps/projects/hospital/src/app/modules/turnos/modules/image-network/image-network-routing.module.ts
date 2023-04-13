import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { ERole } from '@api-rest/api-model';
import { WorklistComponent } from '@turnos/components/worklist/worklist.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: 'imagenes/lista-trabajos',
				component: WorklistComponent,
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
