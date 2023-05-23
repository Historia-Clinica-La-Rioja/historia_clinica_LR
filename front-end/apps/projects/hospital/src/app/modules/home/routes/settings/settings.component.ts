import { Component, OnInit } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
	snomedCacheFF$: Observable<boolean>;

	constructor(
		private featureFlagService: FeatureFlagService,
	) { }

	ngOnInit(): void {
		this.snomedCacheFF$ = this.featureFlagService.isActive(AppFeature.HABILITAR_CARGA_CACHE_EN_DESARROLLO);
	}

}
