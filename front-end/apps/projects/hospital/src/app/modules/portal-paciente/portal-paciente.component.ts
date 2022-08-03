import { Component, OnInit } from '@angular/core';
import { AccountService } from '@api-rest/services/account.service';
import { RoleAssignmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';
import { mapToUserInfo } from '@api-presentation/mappers/user-person-dto.mapper';

import { ROLES_USER_SIDEBAR_MENU, NO_ROLES_USER_SIDEBAR_MENU } from '../portal-paciente/constants/menu';
import { LoggedUserService } from '../auth/services/logged-user.service';
import { HomeRoutes } from '../home/home-routing.module';
import { AppRoutes } from '../../app-routing.module';
import {FeatureFlagService} from "@core/services/feature-flag.service";

@Component({
	selector: 'app-portal-paciente',
	templateUrl: './portal-paciente.component.html',
	styleUrls: ['./portal-paciente.component.scss']
})
export class PortalPacienteComponent implements OnInit {
	userProfileLink = ['/', AppRoutes.Home, HomeRoutes.Profile];
	menuItems: MenuItem[];
	userInfo: UserInfo;
	nameSelfDeterminationFF: boolean;

	constructor(
		private readonly loggedUserService: LoggedUserService,
		private readonly accountService: AccountService,
		private featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
			this.nameSelfDeterminationFF = isOn});
	}

	ngOnInit(): void {
		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			const menuItemDefs = this.userHasAnyRole(roleAssignment)? ROLES_USER_SIDEBAR_MENU : NO_ROLES_USER_SIDEBAR_MENU;
			this.menuItems = menuItemDefs.map(defToMenuItem);
		});
		this.accountService.getInfo()
			.subscribe(userInfo =>
				this.userInfo = mapToUserInfo(userInfo.email, userInfo.personDto, this.nameSelfDeterminationFF, userInfo.previousLogin)
			);
	}

	private userHasAnyRole(roleAssignments: RoleAssignmentDto[]): boolean {
		return (roleAssignments.length > 0);
	}

}
