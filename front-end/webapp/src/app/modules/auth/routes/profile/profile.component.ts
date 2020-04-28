import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';


@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	constructor(
		private authenticationService: AuthenticationService,
	) {
	}

	ngOnInit(): void {
	}

	logout(): void {
		this.authenticationService.logout().subscribe();
	}
}
