import { Injectable } from '@angular/core';
import { PublicService } from '@api-rest/services/public.service';
import { Observable } from 'rxjs';
import {
	FLAVORED_FOOTER_IMAGES,
	FLAVORED_SECONDARY_LOGOS,
	ImageSrc
} from '@core/utils/flavored-image-definitions';
import { map } from 'rxjs/operators';

const getHeaderSecondaryLogosPath = (flavor: string) => `assets/flavors/${flavor}/images/logos/header_secondary_logo.svg`;

@Injectable({
	providedIn: 'root'
})
export class FlavoredImagesService {

	private logos: ImageSrc[] = [];

	constructor(private readonly publicInfoService: PublicService) {
	}

	public getFooterImages(): Observable<ImageSrc[]> {
		return this.publicInfoService.getInfo()
			.pipe(
				map(publicInfo => {
					return FLAVORED_FOOTER_IMAGES[publicInfo.flavor];
				}),
			);
	}

	public getHeaderSecondaryLogos(): Observable<ImageSrc[]> {
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
