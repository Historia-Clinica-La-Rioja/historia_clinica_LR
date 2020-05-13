import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RoleAssignment, InstitutionDto } from '@api-rest/api-model';

import { AuthenticationService } from '../../../auth/services/authentication.service';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { map, mergeMap } from 'rxjs/operators';
import { InstitutionService } from '@api-rest/services/institution.service';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	roleAssignments$: Observable<{ label: string, institution?: {name: string} }[]>;

	constructor(
		institutionService: InstitutionService,
		loggedUserService: LoggedUserService,
		private authenticationService: AuthenticationService,
	) {
		this.roleAssignments$ = loggedUserService.assignments$.pipe(
			mergeMap((roleAssignments: RoleAssignment[]) =>
				institutionService.getInstitutions(roleAssignments.map(roleAssignment => roleAssignment.institutionId))
					.pipe(
						map((institutions: InstitutionDto[]) => roleAssignments.map(roleAssignment =>({
							label: `auth.roles.names.${roleAssignment.role}`,
							institution: institutions.find(institution => institution.id === roleAssignment.institutionId),
						})))
					),
			),
		);
	}

	ngOnInit(): void {
	}

	logout(): void {
		this.authenticationService.logout();
	}
}
