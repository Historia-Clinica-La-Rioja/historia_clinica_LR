import { Injectable } from '@angular/core';
import { PublicService } from '@api-rest/services/public.service';
import { Observable, of } from 'rxjs';
import {
	FLAVORED_FOOTER_IMAGES,
	FLAVORED_LOGOS,
	FLAVORED_SECONDARY_LOGOS,
	FlavoredImagesObj,
	ImageSrc
} from '@core/utils/flavored-image-definitions';
import { map } from 'rxjs/operators';

const getLogosPath = (flavor: string) => `assets/flavors/${flavor}/images/logos/logo_HSI.svg`;
const getHeaderSecondaryLogosPath = (flavor: string) => `assets/flavors/${flavor}/images/logos/header_secondary_logo.svg`;

@Injectable({
	providedIn: 'root'
})
export class FlavoredImagesService {

	private logos: ImageSrc[] = [];

	constructor(private readonly publicInfoService: PublicService) {
	}

	public getFooterImages():Observable<ImageSrc[]> {
		return this.publicInfoService.getInfo()
			.pipe(
				map(publicInfo => {
					return FLAVORED_FOOTER_IMAGES[publicInfo.flavor];
				}),
			);
	}

	public getLogos():Observable<ImageSrc[]> {
		return this.publicInfoService.getInfo()
			.pipe(
				map(publicInfo => {
					this.logos = FLAVORED_LOGOS[publicInfo.flavor];
					this.logos[0].location = getLogosPath(publicInfo.flavor);
					return this.logos;
				}),
			);
	}

	public getHeaderSecondaryLogos():Observable<ImageSrc[]> {
		return this.publicInfoService.getInfo()
			.pipe(
				map(publicInfo => {
					this.logos = FLAVORED_SECONDARY_LOGOS[publicInfo.flavor];
					this.logos[0].location = getHeaderSecondaryLogosPath(publicInfo.flavor);
					return this.logos;
				}),
			);
	}


}
