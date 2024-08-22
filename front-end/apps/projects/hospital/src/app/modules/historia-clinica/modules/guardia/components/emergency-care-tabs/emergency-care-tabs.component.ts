import { Component, OnInit } from '@angular/core';
import { ERole } from '@api-rest/api-model';
import { LoggedUserRolesService } from '@core/services/logged-user-roles.service';
import { take } from 'rxjs';

@Component({
	selector: 'app-emergency-care-tabs',
	templateUrl: './emergency-care-tabs.component.html',
	styleUrls: ['./emergency-care-tabs.component.scss']
})
export class EmergencyCareTabsComponent implements OnInit {

	hasProffesionalRole: boolean;

	constructor(
		private readonly loggedUserRolesService: LoggedUserRolesService,
	) { }

	ngOnInit() {
		this.setRoles();
	}

	private setRoles() {
		const professionalRoles = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
		this.loggedUserRolesService.hasAnyRole(professionalRoles)
			.pipe(take(1)).subscribe(hasRole => {
				this.hasProffesionalRole = hasRole;
			});
	}
}
