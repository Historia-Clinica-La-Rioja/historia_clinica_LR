import { Component, Input, OnInit } from '@angular/core';
import { FlavoredImagesService } from '@core/services/flavored-images.service';
import { ImageSrc } from '@core/utils/flavored-image-definitions';
@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo = false;
	public img: ImageSrc = { location: 'assets/logos/logo_HSI.svg', alt: 'HSI' };

	constructor(private flavoredImagesService: FlavoredImagesService) {
	}

	ngOnInit(): void {
		if (this.isSecondaryLogo) {
			this.flavoredImagesService.getHeaderSecondaryLogos().subscribe(
				images => this.img = images[0]
			);
		}
	}

}
