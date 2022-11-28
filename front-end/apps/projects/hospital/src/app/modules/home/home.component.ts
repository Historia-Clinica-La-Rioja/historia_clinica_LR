import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { PermissionsService } from '@core/services/permissions.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AccountService } from '@api-rest/services/account.service';
import { RoleAssignmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';

import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';
import { mapToUserInfo } from '@api-presentation/mappers/user-person-dto.mapper';

import { MenuService } from '@extensions/services/menu.service';

import { LoggedUserService } from '../auth/services/logged-user.service';
import { NO_ROLES_USER_SIDEBAR_MENU, ROLES_USER_SIDEBAR_MENU } from './constants/menu';

import { HomeRoutes } from '../home/home-routing.module';
import { AppRoutes } from '../../app-routing.module';
import { Slot, SlotedInfo, WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { MenuItemDef } from '@core/core-model';

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

	private readonly NO_INSTITUTION = -1;
	homeExtensions$: Observable<any[]>;
	constructor(
		private contextService: ContextService,
		private extensionMenuService: MenuService,
		private permissionsService: PermissionsService,
		private accountService: AccountService,
		private featureFlagService: FeatureFlagService,
		private loggedUserService: LoggedUserService,
		private readonly wcExtensionsService: WCExtensionsService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
		this.homeExtensions$ = this.wcExtensionsService.getComponentsFromSlot(Slot.HOME_MENU)
			.pipe(
				map(array => {
					return array.map(this.map)
				})
			);
	}

	private map(slotedInfo: SlotedInfo): MenuItem {
		const url = slotedInfo.title.split(' ').join('-');
		return {
			label: {
				text: slotedInfo.title,
			},
			icon: 'home',
			id: url,
			url: `web-components/${slotedInfo.componentName}`,
		}
	}

	ngOnInit(): void {
		this.contextService.setInstitutionId(this.NO_INSTITUTION);

		this.loggedUserService.assignments$.subscribe(roleAssignment => {
			const menuItemDefs: MenuItemDef[] = this.userHasAnyRole(roleAssignment) ? ROLES_USER_SIDEBAR_MENU : NO_ROLES_USER_SIDEBAR_MENU;

			this.menuItems$ = this.featureFlagService.filterItems$(menuItemDefs)
				.pipe(
					switchMap(menu => this.permissionsService.filterItems$(menu)),
					map(menu => menu.map(defToMenuItem)),
					switchMap(items => this.extensionMenuService.getSystemMenuItems().pipe(
						map((extesionItems: MenuItem[]) => [...items, ...extesionItems]),
					)),
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
