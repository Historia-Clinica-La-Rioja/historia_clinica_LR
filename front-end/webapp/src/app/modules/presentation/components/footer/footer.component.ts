import { Component, OnInit } from '@angular/core';
import { ImageSrc } from '@core/utils/flavored-image-definitions';
import { FlavoredImagesService } from '@core/services/flavored-images.service';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

	public footerImages: ImageSrc[] = [];

	constructor(private flavoredImagesService: FlavoredImagesService) {
	}

	ngOnInit(): void {
		this.flavoredImagesService.getFooterImages().subscribe(footerImages => this.footerImages = footerImages);
	}

}
