import { Component, Input } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-bar',
	templateUrl: './bar.component.html',
	styleUrls: ['./bar.component.scss']
})
export class BarComponent {
	@Input() position = 'static';

	isExchangeableTheme$: Observable<boolean>;
	constructor(
		private readonly featureFlagService: FeatureFlagService
	) {
		this.isExchangeableTheme$ = this.featureFlagService.isActive(AppFeature.HABILITAR_INTERCAMBIO_TEMAS_EN_DESARROLLO);
	}
}
