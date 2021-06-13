import { AppFeature, ERole } from '@api-rest/api-model';

export class MenuItemDef {
	text: string;
	icon: string;
	id: string;
	url: string;
	permissions?: ERole[];
	options?: any;
	featureFlag?: AppFeature;
}

export interface PWAAction {
	run(): void;
}
