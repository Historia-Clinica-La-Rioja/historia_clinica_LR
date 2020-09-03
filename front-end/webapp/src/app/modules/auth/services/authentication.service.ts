import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from '@api-rest/services/auth.service';
import { LoggedUserService } from './logged-user.service';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {

	constructor(
		private router: Router,
		private authService: AuthService,
		private loggedUserService: LoggedUserService,
		private readonly dialogRef: MatDialog,
	) { }

	public logout() {
		this.authService.logout();
		this.loggedUserService.reset();
		this.closeModals();
		this.router.navigate(['/auth/login']);
	}

	public closeModals(){
		this.dialogRef.closeAll();
	}

	public goHome() {
		this.router.navigate(['/home']);
	}

	public login(username: string, password: string): Observable<any> {
		//console.log('auth/authentication login ' + username);
		return this.authService.login({username, password}).pipe(
			switchMap(() => this.loggedUserService.load()),
		);
	}

}
