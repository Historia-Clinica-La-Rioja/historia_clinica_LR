import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { LoggedUserService } from '../../auth/services/logged-user.service';
import { ContextService } from './context.service';
import { ERole, RoleAssignment } from '@api-rest/api-model';

const itemHasAnyRole = (itemRoles: ERole[], userRoles: ERole[]) => itemRoles.some(role => userRoles.includes(role));

@Injectable({
	providedIn: 'root'
})
export class PermissionsService {

	constructor(
		private loggedUserService: LoggedUserService,
		private contextService: ContextService,
	) { }

	/**
	 * Permite obtener las asignaciones del usuario en el contexto actual.
	 */
	public contextAssignments$(): Observable<ERole[]> {
		return this.contextService.institutionId$.pipe(
			switchMap(() => this.loggedUserService.assignments$),
			map(assignments => this._mapAssignments(assignments, this.contextService.institutionId))
		);
	}

	/**
	 * Permite obtener las asignaciones del usuario en una institución.
	 */
	public filterAssignments$(institutionId: number): Observable<ERole[]> {
		return this.loggedUserService.assignments$.pipe(
			map(assignments => this._mapAssignments(assignments, institutionId))
		);
	}

	/**
	 * Permite filtrar una lista según las asignaciones del usuario en el contexto actual.
	 * La lista debe contener objetos que tengan el atributo permissions de tipo ERole[].
	 */
	public filterItems$<T extends { permissions?: ERole[] }>(items: T[]): Observable<T[]> {
		return this.contextAssignments$().pipe(
			map(contextAssignments => items.filter(item => item.permissions ? itemHasAnyRole(item.permissions, contextAssignments) : true)),
		);
	}

	private _mapAssignments(assignments: RoleAssignment[], institutionId: number): ERole[] {
		return assignments
			.filter(assignment => assignment.institutionId === institutionId)
			.map(assignment => assignment.role);
	}

}
