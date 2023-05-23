import { Component, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ActivatedRoute, Router } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
import { EquipmentDiaryDto, EquipmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';


@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	routePrefix: string;

	tabActiveIndex = 0;

	ffIsOn = false;
	noPermission = false;
	hasRoleToViewTab = false;

	selectedEquipment: EquipmentDto;
	selectedDiary: EquipmentDiaryDto;

	readonly mssg = 'image-network.home.NO_PERMISSION';

	constructor(
		private readonly router: Router,
		public readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly permissionsService: PermissionsService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;

	}

	ngOnInit() {
		if (window.history.state.tab) {
			this.tabActiveIndex = window.history.state.tab;
		}

		this.selectedEquipment = window.history.state.selectedEquipment;
		this.selectedDiary = window.history.state.selectedDiary;

		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO_RED_DE_IMAGENES, ERole.ADMINISTRADOR_AGENDA]).subscribe(hasRole => {
			this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(ffIsOn => {
				this.ffIsOn = ffIsOn;
				this.noPermission = (hasRole && !ffIsOn);
				this.hasRoleToViewTab = hasRole;
			})
		})
	}

	goToNewProfessionalDiary(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda`]);
	}

	goToNewEquipmentDiary(): void {
		this.router.navigate([`${this.routePrefix}/imagenes/nueva-agenda`]);
	}

	tabChanged(tabChangeEvent: MatTabChangeEvent): void {
		this.tabActiveIndex = tabChangeEvent.index;
	}


}

export enum Tabs {
	DIAGNOSTICO_POR_IMAGEN = 3
}

