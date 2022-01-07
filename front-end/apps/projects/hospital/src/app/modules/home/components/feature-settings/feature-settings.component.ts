import { Component } from '@angular/core';
import { UIPageDto } from '@extensions/extensions-model';
import { FeaturesService } from '@extensions/services/features.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-feature-settings',
	templateUrl: './feature-settings.component.html',
	styleUrls: ['./feature-settings.component.scss']
})
export class FeatureSettingsComponent {
	page$: Observable<UIPageDto>

	constructor(
		featuresService: FeaturesService,
	) {
		this.page$ = featuresService.status();
	}

}
