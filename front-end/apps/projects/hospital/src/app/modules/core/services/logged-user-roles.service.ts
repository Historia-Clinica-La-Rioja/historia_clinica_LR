import { Injectable } from '@angular/core';
import { ERole } from '@api-rest/api-model';
import { map, Observable } from 'rxjs';
import { PermissionsService } from './permissions.service';

@Injectable({
	providedIn: 'root'
})
export class LoggedUserRolesService {

	constructor(
		private readonly permissionsService: PermissionsService,
	) { }

	hasAnyRole(rolesToCheck: ERole[]): Observable<boolean> {
		return this.permissionsService.contextAssignments$().pipe(
			map((userRoles: ERole[]) =>
				rolesToCheck.some(role => userRoles.includes(role))
			)
		);
	}
}
