import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { AgendaComponent } from './routes/agenda/agenda.component';

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
						ERole.ESPECIALISTA_EN_ODONTOLOGIA
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
		],
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class TurnosRoutingModule {
}
