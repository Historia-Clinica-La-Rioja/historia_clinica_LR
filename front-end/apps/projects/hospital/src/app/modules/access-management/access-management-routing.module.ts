import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { HomeComponent } from '@access-management/routes/home/home.component';


const MANAGER_ROLES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_REGIONAL, ERole.GESTOR_DE_ACCESO_LOCAL];

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		canActivate: [RoleGuard],
		data: { needsRoot: true, allowedRoles: MANAGER_ROLES }
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AccessManagementRoutingModule { }