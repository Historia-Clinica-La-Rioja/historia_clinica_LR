import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OauthAuthenticationService } from "../../services/oauth-authentication.service";
import { AppRoutes } from "../../../../app-routing.module";

@Component({
	selector: 'app-oauth-login',
	templateUrl: './oauth-login.component.html',
	styleUrls: ['./oauth-login.component.scss']
})
export class OauthLoginComponent implements OnInit {

	constructor(
		private readonly oauthAuthenticationService: OauthAuthenticationService,
		private router: Router,
	) { }

	private timeout = 3000;
	private isLoading = true;

	ngOnInit(): void {

		this.oauthAuthenticationService.getUserAuthenticated$().subscribe(() => {
			this.router.navigate([AppRoutes.Home]);
		});

		setTimeout(() => {
			this.isLoading = false;
			this.router.navigate([AppRoutes.Home]);
		}, this.timeout);

	}

	loading() {
		return this.isLoading;
	}

}
