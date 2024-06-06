import { Injectable } from '@angular/core';
import { AppFeature, ERole } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { TabsLabel, ALL_TABS, FF_TABS } from '@turnos/constants/tabs';
import { combineLatest, take } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class TabsService {

	availableTabs: TabsLabel[] = [];
	selectedIndex = 0;
	tabActive = TabsLabel.PROFESSIONAL;

	constructor(
		private readonly permissionService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.getPermissionsAndSetAvailableTabs();
	}

	setTab(value: string) {
		if (!this.availableTabs.length)
			this.getPermissionsAndSetAvailableTabs();
		const index = this.availableTabs.findIndex(tab => value === tab);
		this.tabActive = this.availableTabs[index];
		this.selectedIndex = index;
	}

	resetTabs() {
		this.availableTabs = [];
		this.tabActive = TabsLabel.PROFESSIONAL;
		this.selectedIndex = 0;
	}

	getPermissionsAndSetAvailableTabs() {
		const userLoggedPermissions$ = this.permissionService.contextAssignments$();
		const activeFeatureFlag$ = this.featureFlagService.filterItems$(FF_TABS);

		combineLatest([userLoggedPermissions$, activeFeatureFlag$]).pipe(take(1)).subscribe(([userLoggedPermission, activeFF]) => {
			this.setAvailableTabs(userLoggedPermission, activeFF.flatMap(active => active.featureFlag));
		});
	}

	setAvailableTabs(userLoggedPermissions: ERole[], activeFeatureFlags: AppFeature[]) {
		const allTabs = ALL_TABS;

		const matchRole = (tab: Tabs) => tab.rules.roles.some(role => userLoggedPermissions.includes(role));
		const matchFeatureFlag = (tab: Tabs) => !tab.rules.featureFlag || activeFeatureFlags.some(activeFeatureFlag => activeFeatureFlag === tab.rules.featureFlag);

		this.availableTabs = allTabs
			.filter(tab => matchRole(tab) && matchFeatureFlag(tab))
			.map(tab => tab.label);
	}
}

export interface Tabs {
	label: TabsLabel;
	rules: TabsRules;
}

export interface TabsRules {
	roles: ERole[];
	featureFlag?: AppFeature;
}