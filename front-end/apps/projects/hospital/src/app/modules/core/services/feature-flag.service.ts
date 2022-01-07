import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PublicService } from '@api-rest/services/public.service';
import { map } from 'rxjs/operators';
import { AppFeature } from '@api-rest/api-model';


@Injectable({
	providedIn: 'root'
})
export class FeatureFlagService {

	constructor(
		private publicService: PublicService
	) {}

	public isActive(feature: AppFeature): Observable<boolean> {
		return this.publicService.getInfo()
			.pipe(
				map(info => info.features.some(f => f === feature))
			);
	}

	/**
	 * Permite filtrar una lista según los feature flags activos.
	 * La lista debe contener objetos que tengan el atributo featureFlag de tipo AppFeature.
	 */
	public filterItems$<T extends { featureFlag?: AppFeature }>(items: T[]): Observable<T[]> {
		return this.publicService.getInfo().pipe(map(info => {
			return items.filter(item => {
				if (item.featureFlag) {
					return info.features.some(ffActive => ffActive === item.featureFlag);
				}
				return true;
			});
		}));
	}

}
