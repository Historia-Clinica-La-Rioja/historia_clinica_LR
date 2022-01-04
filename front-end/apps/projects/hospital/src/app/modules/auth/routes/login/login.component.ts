import { Component } from '@angular/core';
import { AuthService } from "@api-rest/services/auth.service";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent {

	isOAuthEnabled = false;

	isLoading = true;

	constructor(private readonly authService: AuthService) {
		authService.getOauthConfig().subscribe(config => {
			this.isOAuthEnabled = config.enabled;
			this.isLoading = false;
		});
	}

}
