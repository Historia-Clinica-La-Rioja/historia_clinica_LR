import { Injectable } from '@angular/core';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { pushIfNotExists } from '@core/utils/array.utils';
import { ROLE_TABS, Tabs } from '@turnos/constants/tabs';
import { take, map } from 'rxjs';

@Injectable()
export class TabsService {

	private allUserRolesTabs: Tabs[] = [];
	selectedIndex = 0;
	tabActive = Tabs.PROFESSIONAL;

	constructor(
		private readonly permissionService: PermissionsService
	) { }

	setTab(value: string) {
		if (!this.allUserRolesTabs.length)
			this.setUserRolesTabs();
		const index = this.allUserRolesTabs.findIndex(tab => value === tab);
		this.tabActive = this.allUserRolesTabs[index];
		this.selectedIndex = index;
	}

	clearInfo() {
		this.selectedIndex = 0;
		this.tabActive = Tabs.PROFESSIONAL;
		this.allUserRolesTabs = [];
	}

	private setUserRolesTabs() {
		this.permissionService.contextAssignments$().pipe(
			take(1),
			map(roles => roles.includes(ERole.ADMINISTRATIVO) ? this.sortByRole(roles) : roles))
			.subscribe(roles => roles.forEach(
				rol => {
					const currentRolTabs: Tabs[] = ROLE_TABS[rol];
					currentRolTabs?.forEach(rolTab => this.allUserRolesTabs = pushIfNotExists(this.allUserRolesTabs, rolTab, this.equalsTabs));
				})
			);
	}

	private sortByRole(roles: ERole[]): ERole[] {
		return roles.sort(role => role === ERole.ADMINISTRATIVO ? -1 : 1)
	}

	private equalsTabs(tab1: Tabs, tab2: Tabs): boolean {
		return tab1 === tab2;
	}

}
