import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { AgendaComponent } from './routes/agenda/agenda.component';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';

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
						ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
						ERole.ABORDAJE_VIOLENCIAS,
						ERole.GESTOR_DE_ACCESO_INSTITUCIONAL
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
