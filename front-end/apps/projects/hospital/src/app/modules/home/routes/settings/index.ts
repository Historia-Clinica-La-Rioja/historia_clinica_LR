import { Routes } from '@angular/router';
import { FeatureSettingsComponent } from '../../components/feature-settings/feature-settings.component';
import { LogoSettingsComponent } from '../../components/logo-settings/logo-settings.component';
import { RouteMenuComponent } from '@presentation/components/route-menu/route-menu.component';
import { CacheTerminologyComponent } from '../snomed/cache-terminology/cache-terminology.component';
import { CacheSynonymComponent } from '../snomed/cache-synonym/cache-synonym.component';

export const SETTINGS_ROUTES: Routes = [
	{
		path: 'features',
		component: FeatureSettingsComponent,
		data: {
			label: {key: 'configuracion.features.TITLE'},
		}
	},
	{
		path: 'logo',
		component: LogoSettingsComponent,
		data: {
			label: {key: 'configuracion.logos.TITLE'},
		}
	},
	{
		path: 'snomed-cache',
		component: RouteMenuComponent,
		data: {
			label: {key: 'configuracion.snomed-cache.TITLE'},
		},
		children: [
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
		],
	},
];
