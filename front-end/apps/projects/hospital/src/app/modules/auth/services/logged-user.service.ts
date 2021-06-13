import { Injectable } from '@angular/core';
import { AccountService } from '@api-rest/services/account.service';
import { ReplaySubject, Observable, EMPTY } from 'rxjs';
import { RoleAssignment, PermissionsDto } from '@api-rest/api-model';
import { tap, catchError } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class LoggedUserService {
	private assignmentsSource = new ReplaySubject<RoleAssignment[]>(1);
	private _assignments$: Observable<RoleAssignment[]>;

	constructor(
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
		this._assignments$ = undefined;
	}

	public load(): Observable<any> {
		return this.accountService.getPermissions()
			.pipe(
				catchError(() => {
					return EMPTY;
				}),
				tap((permissionsDto: PermissionsDto) => {
					this.assignmentsSource.next(permissionsDto.roleAssignments);
				}),
			);
	}

}
