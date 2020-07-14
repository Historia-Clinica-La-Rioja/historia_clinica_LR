import {Component, OnInit} from '@angular/core';
import {LoggedUserService} from '../../../auth/services/logged-user.service';
import {RoleAssignment} from '@api-rest/api-model';
import {Router} from '@angular/router';
import {InstitutionService} from '@api-rest/services/institution.service';
import {uniqueItems} from '@core/utils/array.utils';

@Component({
	selector: 'app-instituciones',
	templateUrl: './instituciones.component.html',
	styleUrls: ['./instituciones.component.scss']
})
export class InstitucionesComponent implements OnInit {
	institutions: { id: number, name: string }[] = null;
	backoffice: boolean;

	constructor(
		loggedUserService: LoggedUserService,
		institutionService: InstitutionService,
		private router: Router,
	) {
		loggedUserService.assignments$.subscribe((allRoles: RoleAssignment[]) => {
			const institutionIds = allRoles
				.filter((ra) => ra.institutionId >= 0 && ra.role !== 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')
				.map(r => r.institutionId);

			this.backoffice = this.hasAccessToBackoffice(allRoles);

			institutionService.getInstitutions(institutionIds).subscribe(institutions => {
				let uniqueIds = uniqueItems(institutionIds);

				const webappAccess = this.hasAccessToWebapp(allRoles);

				const onlyBackoffice = !webappAccess && this.backoffice;
				const hasSingleIdWebapp = !this.backoffice && webappAccess && uniqueIds.length === 1;

				if (hasSingleIdWebapp || onlyBackoffice)
					this.ingresar({id: uniqueIds[0]}, this.backoffice)
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

	hasAccessToBackoffice(allRoles: RoleAssignment[]){
		return allRoles
			.filter((ra) => ra.role === 'ROOT' ||
				ra.role === 'ADMINISTRADOR' ||
				ra.role === 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE' ).length > 0;
	}

	hasAccessToWebapp(allRoles: RoleAssignment[]){
		return allRoles
			.filter((ra) => ra.role !== 'ROOT' &&
				ra.role !== 'ADMINISTRADOR' &&
				ra.role !== 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE' ).length > 0;
	}
}
