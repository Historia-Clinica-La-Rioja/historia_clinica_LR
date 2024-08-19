import { Routes } from '@angular/router';
import { FeatureSettingsComponent } from '../../components/feature-settings/feature-settings.component';
import { RouteMenuComponent } from '@presentation/components/route-menu/route-menu.component';
import { APPEARANCE_ROUTES } from '../appearance';
import { SNOMED_CACHE_ROUTES } from '../snomed';

export const SETTINGS_ROUTES: Routes = [
	{
		path: 'features',
		component: FeatureSettingsComponent,
		data: {
			label: {key: 'configuracion.features.TITLE'},
		}
	},
	{
		path: 'appearance',
		component: RouteMenuComponent,
		data: {
			label: {key: 'configuracion.appearance.TITLE'},
		},
		children: APPEARANCE_ROUTES,
	},
	{
		path: 'snomed-cache',
		component: RouteMenuComponent,
		data: {
			label: {key: 'configuracion.snomed-cache.TITLE'},
		},
		children: SNOMED_CACHE_ROUTES,
	},
];
