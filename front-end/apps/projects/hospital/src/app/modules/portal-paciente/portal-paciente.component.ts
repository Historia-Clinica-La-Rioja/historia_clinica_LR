import { Component, OnInit } from '@angular/core';
import { MenuFooter } from '@presentation/components/main-layout/main-layout.component';
import { ROLES_USER_SIDEBAR_MENU, NO_ROLES_USER_SIDEBAR_MENU } from '../portal-paciente/constants/menu';
import { RoleAssignment } from '@api-rest/api-model';
import { LoggedUserService } from '../auth/services/logged-user.service';
import { mapToFullName } from '@api-rest/mapper/user-person-dto.mapper';
import { AccountService } from '@api-rest/services/account.service';
import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';


@Component({
	selector: 'app-portal-paciente',
	templateUrl: './portal-paciente.component.html',
	styleUrls: ['./portal-paciente.component.scss']
})
export class PortalPacienteComponent implements OnInit {

	menuItems: MenuItem[];
	menuFooterItems: MenuFooter = { user: {} };

	constructor(
		private readonly loggedUserService: LoggedUserService,
		private readonly accountService: AccountService,
	) {
	}

	ngOnInit(): void {
		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			const menuItemDefs = this.userHasAnyRole(roleAssignment)? ROLES_USER_SIDEBAR_MENU : NO_ROLES_USER_SIDEBAR_MENU;
			this.menuItems = menuItemDefs.map(defToMenuItem);
		});
		this.accountService.getInfo()
			.subscribe(userInfo => {
				this.menuFooterItems.user.userName = userInfo.email;
				this.menuFooterItems.user.fullName = mapToFullName(userInfo.personDto);
			}
			);
	}

	private userHasAnyRole(roleAssignments: RoleAssignment[]): boolean {
		return (roleAssignments.length > 0);
	}

}
