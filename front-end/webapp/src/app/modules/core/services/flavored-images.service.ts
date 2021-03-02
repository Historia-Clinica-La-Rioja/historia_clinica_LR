import { Injectable } from '@angular/core';
import { PublicService } from '@api-rest/services/public.service';
import { Observable } from 'rxjs';
import {
	FLAVORED_FOOTER_IMAGES,
	ImageSrc
} from '@core/utils/flavored-image-definitions';
import { map } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class FlavoredImagesService {

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
}
