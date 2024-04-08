import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Router } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { TabsLabel } from '@turnos/constants/tabs';
import { TabsService } from '@turnos/services/tabs.service';


@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

	routePrefix: string;

	noPermission = false;
	imageNetworkFF = false;

	constructor(
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly permissionsService: PermissionsService,
		readonly tabsService: TabsService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit() {
		!window.history.state.tab ? this.tabsService.setTab(TabsLabel.PROFESSIONAL) : this.tabsService.setTab(window.history.state.tab)

		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO_RED_DE_IMAGENES, ERole.ADMINISTRADOR_AGENDA]).subscribe(hasRole => {
			this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(ffIsOn => {
				this.imageNetworkFF = ffIsOn;
				this.noPermission = (hasRole && !ffIsOn);
			})
		});
	}

	ngOnDestroy(): void {
		this.tabsService.resetTabs();
	}

	goToNewProfessionalDiary(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda`]);
	}

	goToNewEquipmentDiary(): void {
		this.router.navigate([`${this.routePrefix}/imagenes/nueva-agenda`]);
	}

	tabChanged(tabChangeEvent: MatTabChangeEvent): void {
		const textLabelWithoutWhiteSpace = tabChangeEvent.tab.textLabel.trim();
		this.tabsService.setTab(textLabelWithoutWhiteSpace);
		this.router.navigate([`${this.routePrefix}`], { queryParamsHandling: 'preserve' });
	}

}
