import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { AppFeature, ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { AgendaComponent } from './routes/agenda/agenda.component';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent,
				canActivate: [RoleGuard],
				data: {
					allowedRoles: [
						ERole.ADMINISTRATIVO,
						ERole.ADMINISTRADOR_AGENDA,
						ERole.ENFERMERO,
						ERole.ESPECIALISTA_MEDICO,
						ERole.PROFESIONAL_DE_SALUD,
						ERole.ESPECIALISTA_EN_ODONTOLOGIA,
						ERole.ADMINISTRATIVO_RED_DE_IMAGENES
					]
				},
				children: [
					{
						path: 'agenda/:idAgenda',
						component: AgendaComponent
					}
				]
			},
			{
				path: 'nueva-agenda/:idProfessional',
				component: AgendaSetupComponent,
				canActivate: [RoleGuard],
				data: {allowedRoles: [ERole.ADMINISTRADOR_AGENDA]}
			},
			{
				path: 'nueva-agenda',
				component: AgendaSetupComponent,
				canActivate: [RoleGuard],
				data: {allowedRoles: [ERole.ADMINISTRADOR_AGENDA]}
			},
			{
				path: 'agenda/:agendaId/editar',
				component: AgendaSetupComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] , editMode: true}
			},
			{
				path: 'imagenes',
				loadChildren: () => import('./modules/image-network/image-network.module').then(m => m.ImageNetworkModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES }
			},
			{
				path: 'imagenes/nueva-agenda',
				component: EquipmentDiarySetupComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] }
			},
			{
				path: 'imagenes/agenda/:agendaId/editar',
				component: EquipmentDiarySetupComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] , editMode: true}
			},	
		],
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class TurnosRoutingModule {
}
