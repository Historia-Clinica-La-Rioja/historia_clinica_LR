import { Component, OnInit } from '@angular/core';
import { MenuFooter } from '@presentation/components/main-layout/main-layout.component';
import { ROLES_USER_SIDEBAR_MENU, NO_ROLES_USER_SIDEBAR_MENU } from '../portal-paciente/constants/menu';
import {RoleAssignment} from '@api-rest/api-model';
import {LoggedUserService} from '../auth/services/logged-user.service';

@Component({
	selector: 'app-portal-paciente',
	templateUrl: './portal-paciente.component.html',
	styleUrls: ['./portal-paciente.component.scss']
})
export class PortalPacienteComponent implements OnInit {

	menuItems;
	menuFooterItems: MenuFooter = {user: {}};

	constructor(
		private readonly loggedUserService: LoggedUserService,
	) {
	}

	ngOnInit(): void {
		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			if (this.userHasAnyRole(roleAssignment)) {
				this.menuItems = ROLES_USER_SIDEBAR_MENU;
			} else {
				this.menuItems = NO_ROLES_USER_SIDEBAR_MENU;
			}
		});
	}

	private userHasAnyRole(roleAssignments: RoleAssignment[]): boolean {
		return (roleAssignments.length > 0);
	}

}
