import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { RoleGuard } from '@core/guards/RoleGuard';
import { HomeComponent } from '../program-reports/routes/home/home.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: HomeComponent },
      { path: 'home', component: HomeComponent}
    ],
    canActivate: [RoleGuard, FeatureFlagGuard],
    data: {
      allowedRoles: [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE],
      featureFlag: AppFeature.HABILITAR_REPORTES_PROGRAMAS
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProgramReportsRoutingModule { }