import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';
import { InstitutionExtensionComponent } from '@extensions/routes/extension/extension.component';

import { InstitucionComponent } from './institucion.component';
import { HomeComponent } from './routes/home/home.component';
import { FeatureFlagGuard } from '@core/guards/FeatureFlagGuard';
import { InstitutionRoutedExternalComponent } from '@extensions/institution-routed-external/institution-routed-external.component';

const routes: Routes = [
	{
		path: ':id',
		component: InstitucionComponent,
		children: [
			{ path: '', component: HomeComponent },
			{
				path: 'pacientes',
				loadChildren: () => import('../pacientes/pacientes.module').then(m => m.PacientesModule),
			},
			{
				path: 'internaciones',
				loadChildren: () => import('../historia-clinica/modules/ambulatoria/modules/internacion/internaciones.module').then(m => m.InternacionesModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_MODULO_INTERNACION }
			},
			{
				path: 'ambulatoria',
				loadChildren: () => import('../historia-clinica/modules/ambulatoria/ambulatoria.module').then(m => m.AmbulatoriaModule),
			},
			{
				path: 'turnos',
				loadChildren: () => import('../turnos/turnos.module').then(m => m.TurnosModule),
			},
			{
				path: 'camas',
				loadChildren: () => import('../camas/camas.module').then(m => m.CamasModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_MODULO_CAMAS }
			},
			{
				path: 'guardia',
				loadChildren: () => import('../historia-clinica/modules/guardia/guardia.module').then(m => m.GuardiaModule),
			},
			{
				path: 'reportes',
				loadChildren: () => import('../reportes/reportes.module').then(m => m.ReportesModule),
				canActivate: [ FeatureFlagGuard ],
				data: { featureFlag: AppFeature.HABILITAR_REPORTES }
			},
			{ path: 'extension/:menuItemId', component: InstitutionExtensionComponent, data: { enableDownloadCSV: true } },
			{
				path: 'auditoria',
				loadChildren: () => import('../auditoria/auditoria.module').then(m => m.AuditoriaModule),
			},
			{
				path: 'imagenes/lista-trabajos',
				loadChildren: () => import('../image-network/image-network.module').then(m => m.ImageNetworkModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES }
			},
			{
				path: 'ordenes/lista-trabajos',
				loadChildren: () => import('../orders/orders.module').then(m => m.OrdersModule),
			},
			{ path: 'web-components/:wcId', component: InstitutionRoutedExternalComponent },
			{
				path: 'telesalud',
				loadChildren: () => import('../telemedicina/telemedicina.module').then(m => m.TelemedicinaModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_TELEMEDICINA }
			},
			{
				path: 'firma-digital/documentos',
				loadChildren: () => import('../documents-signature/modules/digital-signature/digital-signature.module').then(m => m.DigitalSignatureModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_FIRMA_DIGITAL }
			},
			{
				path: 'firma-documentos',
				loadChildren: () => import('../documents-signature/documents-signature.module').then(m => m.DocumentsSignatureModule),
			},
			{
				path: 'firma-conjunta',
				loadChildren: () => import('../documents-signature/modules/joint-signature/joint-signature.module').then(m => m.JointSignatureModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_FIRMA_CONJUNTA }
			},
			{
				path: 'areas-sanitarias',
				loadChildren: () => import('../gis/gis.module').then(m => m.GisModule),
				canActivate: [FeatureFlagGuard],
				data: { featureFlag: AppFeature.HABILITAR_AREA_RESPONSABILIDAD_SANITARIA }
			},

		],
		canActivate: [RoleGuard],
		data: {
			allowedRoles: [ERole.ADMINISTRADOR, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRATIVO,
			ERole.ENFERMERO, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ROOT, ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			ERole.ADMINISTRADOR_DE_CAMAS, ERole.PERSONAL_DE_IMAGENES, ERole.PERSONAL_DE_LABORATORIO, ERole.PERSONAL_DE_FARMACIA, ERole.PERSONAL_DE_ESTADISTICA,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, ERole.PRESCRIPTOR, ERole.AUDITOR_MPI, ERole.TECNICO, ERole.INDEXADOR,
			ERole.PERSONAL_DE_LEGALES, ERole.INFORMADOR, ERole.VIRTUAL_CONSULTATION_PROFESSIONAL, ERole.VIRTUAL_CONSULTATION_RESPONSIBLE, ERole.ABORDAJE_VIOLENCIAS]
		},

	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InstitucionRoutingModule { }
