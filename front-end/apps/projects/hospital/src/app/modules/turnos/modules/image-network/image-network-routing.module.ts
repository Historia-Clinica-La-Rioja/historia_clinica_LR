import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { ERole } from '@api-rest/api-model';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
import { WorklistComponent } from '@turnos/components/worklist/worklist.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: 'nueva-agenda',
				component: EquipmentDiarySetupComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] }
			},
			{
				path: 'agenda/:agendaId/editar',
				component: EquipmentDiarySetupComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] , editMode: true}
			},
			{
				path: 'imagenes/lista-trabajos',
				component: WorklistComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRATIVO_RED_DE_IMAGENES]}
			}
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class ImageNetworkRoutingModule { }
