import { Component, OnInit } from '@angular/core';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { map } from 'rxjs/operators';
import { RoleAssignment } from '@api-rest/api-model';
import { Router } from '@angular/router';
import { InstitutionService } from '../../../api-rest/services/institution.service';
import { uniqueItems } from '@core/utils/array.utils';

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
				.filter(r => r.institutionId >= 0)
				.map(r => r.institutionId);

			this.backoffice = allRoles
				.filter((ra) => ra.role === 'ROOT' ||
					ra.role === 'ADMINISTRADOR' ||
					ra.role === 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE' ).length > 0;

			institutionService.getInstitutions(institutionIds).subscribe(institutions => {
				let uniqueIds = uniqueItems(institutionIds);
				if (uniqueIds.length === 1 && allRoles.length === 1)
					this.ingresar({id: uniqueIds[0]})
				this.institutions = institutions;
			});
		});
	}

	ngOnInit(): void {
	}

	ingresar(institutionDto: { id: number }): void {
		if (institutionDto.id === -1) {
			window.location.href = '/backoffice/index.html';
		} else {
			this.router.navigate(['/institucion', institutionDto.id]);
		}
	}
}
