import {Component, OnInit} from '@angular/core';
import {LoggedUserService} from '../../../auth/services/logged-user.service';
import { RoleAssignment } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import {Router} from '@angular/router';
import {InstitutionService} from '@api-rest/services/institution.service';

@Component({
	selector: 'app-instituciones',
	templateUrl: './instituciones.component.html',
	styleUrls: ['./instituciones.component.scss']
})
export class InstitucionesComponent implements OnInit {
	institutions: { id: number, name: string }[] = null;
	webappInstitutionsAccess: boolean;
	backofficeAccess: boolean;
	patientPortalAccess: boolean;

	constructor(
		loggedUserService: LoggedUserService,
		institutionService: InstitutionService,
		private router: Router,
	) {
		loggedUserService.assignments$.subscribe((allRoles: RoleAssignment[]) => {
			const institutionIds = allRoles
				.filter((ra) => ra.institutionId >= 0 && ra.role !== ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE)
				.map(r => r.institutionId);

			this.webappInstitutionsAccess = this.hasAccessToWebappInstitutions(allRoles);
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
	}

	ngOnInit(): void {
	}

	ingresar(institutionDto: { id: number }, backoffice): void {
		if (backoffice) {
			window.location.href = '/backoffice/index.html';
		} else {
			this.router.navigate(['/institucion', institutionDto.id]);
		}
	}

	ingresarPortalPaciente(): void {
		this.router.navigate(['/paciente']);
	}

	hasAccessToBackoffice(allRoles: RoleAssignment[]) {
		return allRoles
			.filter((ra) => ra.role === ERole.ROOT ||
				ra.role === ERole.ADMINISTRADOR ||
				ra.role === ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE ).length > 0;
	}

	hasAccessToWebappInstitutions(allRoles: RoleAssignment[]) {
		return allRoles
			.filter((ra) => ra.role !== ERole.ROOT &&
				ra.role !== ERole.ADMINISTRADOR &&
				ra.role !== ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE ).length > 0;
	}

	hasAccessToPatientPortal(allRoles: RoleAssignment[]): boolean {
		return !(allRoles.some(ra => ra.role === ERole.ROOT));
	}
}
