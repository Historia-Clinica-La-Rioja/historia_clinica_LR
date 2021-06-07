import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './routes/home/home.component';
import {ERole} from '@api-rest/api-model';
import {RoleGuard} from '@core/guards/RoleGuard';


const routes: Routes = [
	{
		path: '',
		children: [
			{ path: '', component: HomeComponent },
			{ path: 'home', component: HomeComponent }
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE] }
	},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportesRoutingModule { }
