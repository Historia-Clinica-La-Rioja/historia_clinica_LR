import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { RouteMenuComponent } from '@presentation/components/route-menu/route-menu.component';
import { CubeReportComponent } from './routes/cube-report/cube-report.component';
import { MonthlyReportComponent } from './routes/monthly-report/monthly-report.component';

/**
 * Routes de reportes
 */
export enum ReportesRoutes {
	MonthlyReport = 'monthly-report',
	DiabeticPatients = 'diabetes',
	HypertensivePatients = 'hypertension',
	WeeklyEpidemiologicalReport = 'epidemiological_week',
}

const routes: Routes = [
	{
		path: '',
		component: RouteMenuComponent,
		data: {
			allowedRoles: [
				ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
				ERole.PERSONAL_DE_ESTADISTICA,
				ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
				ERole.ESPECIALISTA_MEDICO,
				ERole.PROFESIONAL_DE_SALUD,
				ERole.ENFERMERO,
				ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			],
			label: { key: 'app.menu.REPORTES' },
			icon: 'description',
		},
		canActivate: [ RoleGuard ],
		children: [
			{
				path: ReportesRoutes.MonthlyReport,
				component: MonthlyReportComponent,
				data: {
					label: {key: 'reportes.monthly-report.TITLE'},
				},
			},
			{
				path: ReportesRoutes.DiabeticPatients,
				component: CubeReportComponent,
				data: {
					label: {key: 'reportes.diabetic-patients.TITLE'},
				},
			},
			{
				path: ReportesRoutes.HypertensivePatients,
				component: CubeReportComponent,
				data: {
					label: {key: 'reportes.hypertensive-patients.TITLE'},
				},
			},
			{
				path: ReportesRoutes.WeeklyEpidemiologicalReport,
				component: CubeReportComponent,
				data: {
					label: {key: 'reportes.weekly-epidemiological-report.TITLE'},
				},
			},
		],
	},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
exports: [RouterModule]
})
export class ReportesRoutingModule { }
