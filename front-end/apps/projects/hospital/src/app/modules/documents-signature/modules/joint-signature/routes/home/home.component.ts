import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { URL_DOCUMENTS_SIGNATURE } from '../../../../routes/home/home.component';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent {
	buttonBack = false;
	routePrefix: string;

	constructor(
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.featureFlagService.filterItems$([{featureFlag:[AppFeature.HABILITAR_FIRMA_DIGITAL,AppFeature.HABILITAR_FIRMA_CONJUNTA]}]).subscribe(isActive =>{
			this.buttonBack= isActive[0]?.featureFlag.length > 1;
		});
	}

	goToBackDocumentsSignature(): void {
		this.router.navigate([`${this.routePrefix}${URL_DOCUMENTS_SIGNATURE}`]);
	}
}
