import { Component, OnInit } from '@angular/core';
import { AuthService } from "@api-rest/services/auth.service";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

	isOAuthEnabled = false;

	isLoading : boolean;

	constructor(private readonly authService: AuthService) {}

	ngOnInit(): void {
		this.isLoading = true;
		this.authService.getOauthConfig().subscribe(config => {
			this.isOAuthEnabled = config.enabled;
			this.isLoading = false;
		});
	}

}
