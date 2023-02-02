import { Component, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ActivatedRoute, Router } from '@angular/router';
import { AppFeature, ERole } from '@api-rest/api-model';
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
		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO_RED_DE_IMAGENES]).subscribe(hasRole => {
			this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(ffIsOn => {
				this.ffIsOn = ffIsOn;
				this.noPermission = (hasRole && !ffIsOn);
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

