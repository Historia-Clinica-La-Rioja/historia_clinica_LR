import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { RoleAssignmentDto, InstitutionDto } from '@api-rest/api-model';

import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { map, mergeMap } from 'rxjs/operators';
import { InstitutionService } from '@api-rest/services/institution.service';
import {Router} from "@angular/router";

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
	roleAssignments$: Observable<{ label: string, institution?: {name: string} }[]>;

	constructor(
		institutionService: InstitutionService,
		loggedUserService: LoggedUserService,
		private router: Router,
	) {
		this.roleAssignments$ = loggedUserService.assignments$.pipe(
			mergeMap((roleAssignments: RoleAssignmentDto[]) =>
				institutionService.getInstitutions(roleAssignments.map(roleAssignment => roleAssignment.institutionId))
					.pipe(
						map((institutions: InstitutionDto[]) => roleAssignments.map(roleAssignment => ({
							label: `auth.roles.names.${roleAssignment.role}`,
							institution: institutions.find(institution => institution.id === roleAssignment.institutionId),
						})))
					),
			),
		);
	}

	updatePassword(): void {
		this.router.navigate(['home/update-password']);
	}

}
