import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { filterItems } from '@core/services/permissions.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AccountService } from '@api-rest/services/account.service';
import { RoleAssignmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';

import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';
import { mapToUserInfo } from '@api-presentation/mappers/user-person-dto.mapper';

import { LoggedUserService } from '../auth/services/logged-user.service';
import { NO_ROLES_USER_SIDEBAR_MENU, ROLES_USER_SIDEBAR_MENU } from './constants/menu';

import { HomeRoutes } from './constants/menu';
import { AppRoutes } from '../../app-routing.module';
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { MenuItemDef } from '@core/core-model';
import { ContextService } from '@core/services/context.service';
import { PwaUpdateService } from '@core/services/pwa-update.service';

export const NO_INSTITUTION: number = -1;

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	userProfileLink = ['/', AppRoutes.Home, HomeRoutes.Profile];
	menuItems$: Observable<MenuItem[]>;
	userInfo: UserInfo;
	nameSelfDeterminationFF: boolean;

	homeExtensions$: Observable<MenuItem[]>;
	constructor(
		private accountService: AccountService,
		private featureFlagService: FeatureFlagService,
		private loggedUserService: LoggedUserService,
		private readonly wcExtensionsService: WCExtensionsService,
		private contextService: ContextService,
		private readonly pwaUpdateService: PwaUpdateService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
		this.homeExtensions$ = this.wcExtensionsService.getSystemHomeMenu();
		this.contextService.setInstitutionId(NO_INSTITUTION);
	}

	ngOnInit(): void {

		this.pwaUpdateService.checkForUpdate()
		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			const menuItemDefs: MenuItemDef[] = this.userHasAnyRole(roleAssignment) ? ROLES_USER_SIDEBAR_MENU : NO_ROLES_USER_SIDEBAR_MENU;

			this.menuItems$ = this.featureFlagService.filterItems$(menuItemDefs)
				.pipe(
					map(menu => filterItems<MenuItemDef>(menu, roleAssignment.map(r => r.role))),
					map((menu: MenuItemDef[]) => menu.map(defToMenuItem)),
					switchMap(items => this.homeExtensions$.pipe(
						map((wcExtensionesMenu: MenuItem[]) => [...items, ...wcExtensionesMenu,]),
					)),
				);

		});

		this.accountService.getInfo()
			.subscribe(
				userInfo => this.userInfo = mapToUserInfo(userInfo.email, userInfo.personDto, this.nameSelfDeterminationFF, userInfo.previousLogin)
			);
	}

	private userHasAnyRole(roleAssignments: RoleAssignmentDto[]): boolean {
		return (roleAssignments.length > 0);
	}
}
