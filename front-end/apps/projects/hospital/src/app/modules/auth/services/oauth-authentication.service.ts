import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from "angular-oauth2-oidc";
import { Observable, ReplaySubject } from "rxjs";
import { AppRoutes } from "../../../app-routing.module";
import { AuthService } from "@api-rest/services/auth.service";

@Injectable({
  	providedIn: 'root'
})
export class OauthAuthenticationService {

	private userAuthenticatedEmitter = new ReplaySubject<boolean>();
	private userAuthenticated$: Observable<boolean>;

  	constructor(
  		private readonly oauthService: OAuthService,
		private readonly authService: AuthService
	) {
		this.configureOauthService();
		this.subscribeToTokenUpdates();

		this.userAuthenticated$ = this.userAuthenticatedEmitter.asObservable();
	}

	private subscribeToTokenUpdates() {
		this.oauthService.events.subscribe(event => {
			if (this.oauthService.hasValidAccessToken()) {
				this.storeTokens();
			}
		});
	}

	private configureOauthService() {

		this.authService.getOauthConfig().subscribe(config => {
			if (config.enabled) {
				let oAuthConfig: AuthConfig = {
					issuer: config.issuerUrl,
					redirectUri: location.origin + '/' + AppRoutes.Auth + '/oauth/login',
					clientId: config.clientId,
					scope: 'openid profile',
					responseType: 'code',
					requireHttps: false,
				}
				this.oauthService.configure(oAuthConfig);
				this.oauthService.loadDiscoveryDocumentAndTryLogin();
			}
		})
	}

	private storeTokens(): void {
		this.userAuthenticatedEmitter.next(true);
	}

	getUserAuthenticated$(): Observable<boolean> {
		return this.userAuthenticated$;
	}

	login() {
		this.oauthService.initCodeFlow();
	}

	logout() {
		this.oauthService.logOut();
	}
}
