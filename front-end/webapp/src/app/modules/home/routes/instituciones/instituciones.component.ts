import { Component, OnInit } from '@angular/core';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { map } from 'rxjs/operators';
import { RoleAssignment } from '@api-rest/api-model';
import { Router } from '@angular/router';
import { InstitutionService } from '../../../api-rest/services/institution.service';

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
		loggedUserService.assignments$.pipe(
			map(
				(roleAssignments: RoleAssignment[]) =>
					roleAssignments.map(roleAssignment => roleAssignment.institutionId)
			),
		).subscribe((allIds: number[]) => {

			const institutionIds = allIds.filter((x) => x >= 0);
			institutionService.getInstitutions(institutionIds).subscribe(institutions => {
				this.backoffice = allIds.filter((x) => x === -1).length > 0;
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
