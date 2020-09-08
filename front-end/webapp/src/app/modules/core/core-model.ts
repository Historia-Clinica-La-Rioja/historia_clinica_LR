import { AppFeature, ERole } from '@api-rest/api-model';

export class MenuItem {
	text: string;
	icon: string;
	id: string;
	url: string;
	permissions?: ERole[];
	options?: any;
	featureFlag?: AppFeature;
}
