import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "../turnos/routes/home/home.component";
import { NewAgendaComponent } from "./routes/new-agenda/new-agenda.component";
import { ERole } from "@api-rest/api-model";
import { RoleGuard } from "@core/guards/RoleGuard";

import { mockRouters } from '@presentation/utils/mock-routers.utils';
import { MOCKS_TURNOS } from './constants/mock-routers';

const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ADMINISTRATIVO] }
			},
			{
				path: 'new-agenda',
				component: NewAgendaComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.ADMINISTRATIVO] }
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
