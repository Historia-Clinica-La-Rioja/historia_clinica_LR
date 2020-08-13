import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '../turnos/routes/home/home.component';
import { NewAgendaComponent } from './routes/new-agenda/new-agenda.component';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';

import { mockRouters } from '@presentation/utils/mock-routers.utils';
import { MOCKS_TURNOS } from './constants/mock-routers';
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
				data: {allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ADMINISTRATIVO]},
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
				component: NewAgendaComponent,
				canActivate: [RoleGuard],
				data: {allowedRoles: [ERole.ADMINISTRATIVO]}
			},
			{
				path: 'profesional/:profesionalId/agenda/:agendaId/editar',
				component: NewAgendaComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRADOR_AGENDA] , editMode: true}
			},
			...mockRouters(MOCKS_TURNOS),
		],
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class TurnosRoutingModule {
}
