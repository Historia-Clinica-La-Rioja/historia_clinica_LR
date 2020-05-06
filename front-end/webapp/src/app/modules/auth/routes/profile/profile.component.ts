import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RoleAssignment } from '@api-rest/api-model';

import { AuthenticationService } from '../../services/authentication.service';
import { LoggedUserService } from '../../services/logged-user.service';
import { map } from 'rxjs/operators';

const getInstitucion = (institutionId: number): {name: string} =>
	(institutionId !== -1) ? {name: `De la institución ${institutionId}`} : undefined;

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	roleAssignments$: Observable<{ label: string, institution?: {name: string} }[]>;

	constructor(
		loggedUserService: LoggedUserService,
		private authenticationService: AuthenticationService,
	) {
		this.roleAssignments$ = loggedUserService.assignments$.pipe(
			map((roleAssignments: RoleAssignment[]) => roleAssignments.map(roleAssignment =>({
				label: `auth.roles.names.${roleAssignment.role}`,
				institution: getInstitucion(roleAssignment.institutionId),
			}))),
		);
	}

	ngOnInit(): void {
	}

	logout(): void {
		this.authenticationService.logout();
	}
}
