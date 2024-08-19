import { Routes } from '@angular/router';
import { SponsorComponent } from '../appearance/sponsor/sponsor.component';
import { FaviconsComponent } from '../appearance/favicons/favicons.component';
import { DocumentImagesComponent } from '../appearance/document-images/document-images.component';

export const APPEARANCE_ROUTES: Routes = [
	{
		path: 'favicons',
		component:  FaviconsComponent,
		data: {
			label: {key: 'configuracion.logos.TITLE'},
		}
	},
	{
		path: 'sponsor',
		component:  SponsorComponent,
		data: {
			label: {key: 'configuracion.logos.SUBTITLE_1'},
		}
	},
	{
		path: 'document-images',
		component:  DocumentImagesComponent,
		data: {
			label: {key: 'configuracion.document-images.TITLE'},
		}
	},
];
