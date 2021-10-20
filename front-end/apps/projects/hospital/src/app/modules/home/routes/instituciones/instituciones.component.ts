import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AppRoutes } from '../../../../app-routing.module';
import { LoggedUserService } from '../../../auth/services/logged-user.service';

import { RoleAssignment } from '@api-rest/api-model';
import { ERole, AppFeature } from '@api-rest/api-model';

import { InstitutionService } from '@api-rest/services/institution.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-instituciones',
	templateUrl: './instituciones.component.html',
	styleUrls: ['./instituciones.component.scss']
})
export class InstitucionesComponent {
	institutions: { id: number, name: string }[] = null;
	patientPortalEnabled: boolean;
	webappInstitutionsAccess: boolean;
	backofficeAccess: boolean;
	patientPortalAccess: boolean;

	constructor(
		loggedUserService: LoggedUserService,
		institutionService: InstitutionService,
		private featureFlagService: FeatureFlagService,
		private router: Router,
	) {
		loggedUserService.assignments$.subscribe((allRoles: RoleAssignment[]) => {
			const institutionIds = allRoles
				.filter((ra) => ra.institutionId >= 0)
				.map(r => r.institutionId);

			this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTES).subscribe( habilitarReportesIsActive =>
				this.webappInstitutionsAccess = this.hasAccessToWebappInstitutions(allRoles, habilitarReportesIsActive)
			);
			this.backofficeAccess = this.hasAccessToBackoffice(allRoles);
			this.patientPortalAccess = this.hasAccessToPatientPortal(allRoles);

			institutionService.getInstitutions(institutionIds).subscribe(institutions => {
				/*const uniqueIds = uniqueItems(institutionIds);

				const webappAccess = this.hasAccessToWebapp(allRoles);

				const onlyBackoffice = !webappAccess && this.backoffice;
				const hasSingleIdWebapp = !this.backoffice && webappAccess && uniqueIds.length === 1;

				if (hasSingleIdWebapp || onlyBackoffice)
					this.ingresar({id: uniqueIds[0]}, this.backoffice)*/
				this.institutions = institutions;
			});
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE).subscribe(isOn => this.patientPortalEnabled = isOn);
	}

	ingresar(institutionDto: { id: number }, backoffice): void {
		if (backoffice) {
			window.location.href = '/backoffice/index.html';
		} else {
			this.router.navigate([AppRoutes.Institucion, institutionDto.id]);
		}
	}

	ingresarPortalPaciente(): void {
		this.router.navigate(['/paciente']);
	}

	hasAccessToBackoffice(allRoles: RoleAssignment[]) {
		return allRoles
			.filter((ra) => ra.role === ERole.ROOT ||
				ra.role === ERole.ADMINISTRADOR ||
				ra.role === ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE).length > 0;
	}

	hasAccessToWebappInstitutions(allRoles: RoleAssignment[], habilitarReportesIsActive: boolean) {
		return allRoles
			.filter((ra) => ra.role !== ERole.ROOT &&
				ra.role !== ERole.ADMINISTRADOR &&
				!(ra.role === ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE && !habilitarReportesIsActive)
			).length > 0;
	}

	hasAccessToPatientPortal(allRoles: RoleAssignment[]): boolean {
		return !(allRoles.some(ra => ra.role === ERole.ROOT));
	}
}
