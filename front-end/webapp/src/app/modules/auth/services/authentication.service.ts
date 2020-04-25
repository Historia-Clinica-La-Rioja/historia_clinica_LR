import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {

	constructor(
		private router: Router,
	) { }

	public logout() {
		this.router.navigate(['/auth/login']);
	}

	public login(username: string, password: string) {
		console.log('auth/authentication login ' + username);
		this.router.navigate(['/pacientes']);
	}

}
