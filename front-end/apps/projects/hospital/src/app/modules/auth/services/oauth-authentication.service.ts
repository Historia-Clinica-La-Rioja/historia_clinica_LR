import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from "angular-oauth2-oidc";
import { Observable, ReplaySubject } from "rxjs";

@Injectable({
  	providedIn: 'root'
})
export class OauthAuthenticationService {

	private userAuthenticatedEmitter = new ReplaySubject<boolean>();
	private userAuthenticated$: Observable<boolean>;

	authConfig: AuthConfig = {
		issuer: 'http://localhost:8180/auth/realms/hsi',
		redirectUri: 'http://localhost:4200/oauth/login',
		clientId: 'hsi-fe',
		scope: 'openid profile',
		responseType: 'code',
	}

  	constructor(
  		private readonly oauthService: OAuthService,
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
		this.oauthService.configure(this.authConfig);
		this.oauthService.loadDiscoveryDocumentAndTryLogin();
	}

	private storeTokens(): void {
		localStorage.setItem('token', this.oauthService.getAccessToken());
		localStorage.setItem('refreshtoken', this.oauthService.getRefreshToken());
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
