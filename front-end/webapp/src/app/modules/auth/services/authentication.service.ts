import { Injectable } from '@angular/core';
import { AuthService } from '@api-rest/services/auth.service';
import { LoggedUserService } from './logged-user.service';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {

	constructor(
		private authService: AuthService,
		private loggedUserService: LoggedUserService,
	) { }

	public logout(): Observable<any> {
		this.authService.logout();
		return this.loggedUserService.load();
	}

	public login(username: string, password: string): Observable<any> {
		//console.log('auth/authentication login ' + username);
		return this.authService.login({username, password}).pipe(
			switchMap(() => this.loggedUserService.load()),
		);
	}

}
