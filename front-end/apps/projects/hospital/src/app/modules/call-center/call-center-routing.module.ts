import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { HomeComponent } from '@call-center/routes/home/home.component';
import { RoleGuard } from '@core/guards/RoleGuard';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		canActivate: [RoleGuard],
		data: { needsRoot: true, allowedRoles: [ERole.GESTOR_CENTRO_LLAMADO] }
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class CallCenterRoutingModule { }
