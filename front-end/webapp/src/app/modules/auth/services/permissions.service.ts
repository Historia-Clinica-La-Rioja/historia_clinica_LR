import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoggedUserService } from './logged-user.service';


@Injectable({
	providedIn: 'root'
})
export class PermissionsService {

	constructor(
		private loggedUserService: LoggedUserService,
	) { }

	public filterItems$<T extends { permissions?: string[] }>(items: T[]): Observable<T[]> {
		return this.loggedUserService.assignments$.pipe(
			map(assignments => !assignments ? undefined : (assignments.length > 0? items: []))
		);
	}

}
