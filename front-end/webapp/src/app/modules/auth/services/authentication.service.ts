import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { AuthService } from '@api-rest/services/auth.service';
import { LoggedUserService } from './logged-user.service';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {

	constructor(
		private router: Router,
		private authService: AuthService,
		private loggedUserService: LoggedUserService,
	) { }

	public logout() {
		this.authService.logout();
		this.loggedUserService.reset();
		this.router.navigate(['/auth/login']);
	}

	public goHome() {
		this.router.navigate(['/auth']);
	}

	public login(username: string, password: string): Observable<any> {
		//console.log('auth/authentication login ' + username);
		return this.authService.login({username, password}).pipe(
			switchMap(() => this.loggedUserService.load()),
		);
	}

}
