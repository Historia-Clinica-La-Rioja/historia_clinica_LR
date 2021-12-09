import { Component } from '@angular/core';
import { OauthAuthenticationService } from "../../services/oauth-authentication.service";

@Component({
  selector: 'app-external-oauth-login',
  templateUrl: './external-oauth-login.component.html',
  styleUrls: ['./external-oauth-login.component.scss']
})
export class ExternalOAuthLoginComponent {

	constructor(
		private readonly oauthAuthenticationService: OauthAuthenticationService,
	) { }

	redirectToOAuthLogin() {
		this.oauthAuthenticationService.login();
	}

}
