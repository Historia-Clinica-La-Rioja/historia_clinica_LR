import { Component, Input, OnInit } from '@angular/core';
import { Route } from '@angular/router';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { isActive } from '@core/guards/FeatureFlagGuard';
import { PermissionsService } from '@core/services/permissions.service';
import { isAllow } from '@core/guards/RoleGuard';

@Component({
	selector: 'app-route-menu-item',
	templateUrl: './route-menu-item.component.html',
	styleUrls: ['./route-menu-item.component.scss']
})
export class RouteMenuItemComponent implements OnInit {
	@Input() route: Route;

	featureActive: boolean;
	permissionAllow: boolean;

	constructor(
		private featureFlagService: FeatureFlagService,
		private permissionsService: PermissionsService,
	) { }

	ngOnInit(): void {
		const {
			data,
		} = this.route;
		isActive(data, this.featureFlagService).subscribe(
			isOn => this.featureActive = isOn
		);

		isAllow(data, this.permissionsService.contextAssignments$()).subscribe(
			isOn => this.permissionAllow = isOn
		);

	}

}
