import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { LoggedUserService } from '../../auth/services/logged-user.service';
import { ContextService } from './context.service';
import { ERole, RoleAssignmentDto } from '@api-rest/api-model';
import { anyMatch } from '@core/utils/array.utils';

const itemHasAnyRole = (itemRoles: ERole[], userRoles: ERole[]) => itemRoles.some(role => userRoles.includes(role));

const filterRoleAssignment = (institutionId: number) =>
	(assignments: RoleAssignmentDto[]) =>
	assignments.filter(assignment => assignment.institutionId === institutionId);

const mapToRole = (assignments: RoleAssignmentDto[]) => assignments.map(assignment => assignment.role);

@Injectable({
	providedIn: 'root'
})
export class PermissionsService {

	constructor(
		private loggedUserService: LoggedUserService,
		private contextService: ContextService,
	) { }

	get contextRoleAssignments$(): Observable<RoleAssignmentDto[]> {
		return this.contextService.institutionId$.pipe(
			switchMap(() => this.loggedUserService.assignments$),
			map(filterRoleAssignment(this.contextService.institutionId)),
		);
	}

	/**
	 * Permite obtener las asignaciones del usuario en el contexto actual.
	 */
	public contextAssignments$(): Observable<ERole[]> {
		return this.contextService.institutionId$.pipe(
			switchMap(() => this.loggedUserService.assignments$),
			map(filterRoleAssignment(this.contextService.institutionId)),
			map(mapToRole)
		);
	}

	/**
	 * Permite obtener las asignaciones del usuario en una institución.
	 */
	public filterAssignments$(institutionId: number): Observable<ERole[]> {
		return this.loggedUserService.assignments$.pipe(
			map(filterRoleAssignment(institutionId)),
			map(mapToRole)
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

	public hasContextAssignments$(assignments: ERole[]): Observable<boolean> {
		return this.contextAssignments$().pipe(
			map((userRoles: ERole[]) => anyMatch<ERole>(userRoles, assignments))
		);
	}
}
