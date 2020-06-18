import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { JWTokenDto } from '@api-rest/api-model';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '@api-rest/services/auth.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-oauth-login',
	templateUrl: './oauth-login.component.html',
	styleUrls: ['./oauth-login.component.scss']
})
export class OauthLoginComponent implements OnInit {
	constructor(
		private readonly authService: AuthService,
		private readonly route: ActivatedRoute,
		private readonly authenticationService: AuthenticationService,
	) { }

	isLoading = true;

	ngOnInit(): void {
		const code = this.route.snapshot.queryParamMap.get('code');
		this.getOauthUrl(code).subscribe(value => {
			this.isLoading = false;
			localStorage.setItem('token', value.token);
			this.authenticationService.goHome();
		});
	}

	loading(){
		return this.isLoading;
	}

	getOauthUrl(code): Observable<JWTokenDto> {
		return this.authService.loginOauth(code);
	}

}
