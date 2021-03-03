import { Component, OnInit } from '@angular/core';
import { ImageSrc } from '@core/utils/flavored-image-definitions';
import { FlavoredImagesService } from '@core/services/flavored-images.service';
import {PublicService} from '@api-rest/services/public.service';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

	public footerImages: ImageSrc[] = [];
	public applicationVersionNumber: string;

	constructor(
		private flavoredImagesService: FlavoredImagesService,
		private publicService: PublicService,
	) {
	}

	ngOnInit(): void {
		this.publicService.getApplicationCurrentVersion().subscribe(versionDto => this.applicationVersionNumber = versionDto.version);
		this.flavoredImagesService.getFooterImages().subscribe(footerImages => this.footerImages = footerImages);
	}

}
