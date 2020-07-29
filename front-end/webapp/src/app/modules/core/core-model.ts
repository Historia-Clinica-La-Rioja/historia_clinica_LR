import { AppFeature, ERole } from '@api-rest/api-model';

export class MenuItem {
	text: string;
	icon: string;
	url: string;
	permissions?: ERole[];
	options?: any;
	featureFlag?: AppFeature;
}
