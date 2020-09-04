import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { SelectAgendaComponent } from './routes/home/routes/select-agenda/select-agenda.component';
import { AgendaComponent } from './routes/home/routes/select-agenda/routes/agenda/agenda.component';

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
					]
				},
				children: [
					{
						path: 'profesional/:idProfesional',
						component: SelectAgendaComponent,
						children: [
							{
								path: 'agenda/:idAgenda',
								component: AgendaComponent
							}
						]
					}
				]
			},
			{
				path: 'nueva-agenda',
				component: AgendaSetupComponent,
				canActivate: [RoleGuard],
				data: {allowedRoles: [ERole.ADMINISTRADOR_AGENDA]}
			},
			{
				path: 'profesional/:profesionalId/agenda/:agendaId/editar',
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
