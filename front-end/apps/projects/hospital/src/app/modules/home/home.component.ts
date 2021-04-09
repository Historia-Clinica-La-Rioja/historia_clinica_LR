import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { NO_ROLES_USER_SIDEBAR_MENU, ROLES_USER_SIDEBAR_MENU } from './constants/menu';
import { PermissionsService } from '@core/services/permissions.service';
import { MenuFooter } from '@presentation/components/main-layout/main-layout.component';
import { AccountService } from '@api-rest/services/account.service';
import { mapToFullName } from '@api-rest/mapper/user-person-dto.mapper';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { map, switchMap } from 'rxjs/operators';
import { LoggedUserService } from '../auth/services/logged-user.service';
import { RoleAssignment } from '@api-rest/api-model';
import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	menuItems$: Observable<MenuItem[]>;
	menuFooterItems: MenuFooter = { user: {} };

	private readonly NO_INSTITUTION = -1;

	constructor(
		private contextService: ContextService,
		private permissionsService: PermissionsService,
		private accountService: AccountService,
		private featureFlagService: FeatureFlagService,
		private loggedUserService: LoggedUserService,
	) { }

	ngOnInit(): void {
		this.contextService.setInstitutionId(this.NO_INSTITUTION);

		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			const menuItemDefs = this.userHasAnyRole(roleAssignment)? ROLES_USER_SIDEBAR_MENU : NO_ROLES_USER_SIDEBAR_MENU;
			this.menuItems$ = this.featureFlagService.filterItems$(menuItemDefs)
				.pipe(
					switchMap(menu => this.permissionsService.filterItems$(menu)),
					map(menu => menu.map(defToMenuItem)),
				);
		});

		this.accountService.getInfo()
			.subscribe(userInfo => {
				this.menuFooterItems.user = {
					userName: userInfo.email,
					fullName: mapToFullName(userInfo.personDto)
				};
			}
			);
	}

	private userHasAnyRole(roleAssignments: RoleAssignment[]): boolean {
		return (roleAssignments.length > 0);
	}
}
