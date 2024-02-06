import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { AppFeature } from '@api-rest/api-model';
import { HomeComponent } from '@access-management/routes/home/home.component';
import { MANAGER_ROLES } from '../home/constants/menu';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent,
		canActivate: [RoleGuard, FeatureFlagGuard],
		data: { needsRoot: true, allowedRoles: MANAGER_ROLES, featureFlag: AppFeature.HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO }
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AccessManagementRoutingModule { }
