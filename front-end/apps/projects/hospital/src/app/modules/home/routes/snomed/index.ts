import { Routes } from '@angular/router';
import { CacheTerminologyComponent } from '../snomed/cache-terminology/cache-terminology.component';
import { CacheSynonymComponent } from '../snomed/cache-synonym/cache-synonym.component';

export const SNOMED_CACHE_ROUTES: Routes = [
	{
		path: 'terminologies',
		component:  CacheTerminologyComponent,
		data: {
			label: {key: 'configuracion.snomed-cache.TERMINOLOGIES_LIST'},
		}
	},
	{
		path: 'synonyms',
		component:  CacheSynonymComponent,
		data: {
			label: {key: 'configuracion.snomed-cache.SYNONYMS_LIST'},
		}
	}
];
