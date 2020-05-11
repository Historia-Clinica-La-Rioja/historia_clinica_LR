import { Injectable } from '@angular/core';
import { AccountService } from '@api-rest/services/account.service';
import { ReplaySubject, Observable, of } from 'rxjs';
import { RoleAssignment, PermissionsDto } from '@api-rest/api-model';
import { tap, catchError } from 'rxjs/operators';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class LoggedUserService {
	private assignmentsSource = new ReplaySubject<RoleAssignment[]>();
	private _assignments$: Observable<RoleAssignment[]>;

	constructor(
		private contextService: ContextService,
		private accountService: AccountService,
	) { }

	get assignments$(): Observable<RoleAssignment[]> {
		if (!this._assignments$) {
			this._assignments$ = this.assignmentsSource.asObservable();
			this.load().subscribe();
		}
		return this._assignments$;
	}

	public reset() {
		this.assignmentsSource.next([]);
	}

	public load(): Observable<any> {
		return this.accountService.getPermissions()
			.pipe(
				catchError(error => {
					console.log('auth/permissions load() error', error);
					return of({roleAssignments: []});
				}),
				tap((permissionsDto: PermissionsDto) => {
					console.log('auth/permissions load() next', permissionsDto);
					this.contextService.setInstitutionId(permissionsDto.roleAssignments.map(roleAssignment => roleAssignment.institutionId).find(id => id !== -1))
					this.assignmentsSource.next(permissionsDto.roleAssignments);
				}),
			);
	}

}
