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

	logout() {
		this.authService.logout();
		this.loggedUserService.reset();
		this.closeModals();
		this.router.navigate(['/auth/login'], {
			queryParams: {
				returnUrl: this.router.routerState.snapshot.url
			}
		});
	}

	closeModals() {
		this.dialogRef.closeAll();
	}

	public go(url?: string) {
		url ? this.router.navigateByUrl(url) : this.router.navigate(['/home']);
	}

	login(username: string, password: string, recaptchaResponse: string): Observable<any> {
		return this.authService.login({username, password}, recaptchaResponse).pipe(
			switchMap(() => this.loggedUserService.load()),
		);
	}

	tokenRefresh(): Observable<any>  {
		return this.authService.tokenRefresh();
	}
}
