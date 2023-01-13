import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { ERole } from '@api-rest/api-model';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';

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
		]
	}	
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class ImageNetworkRoutingModule { }
