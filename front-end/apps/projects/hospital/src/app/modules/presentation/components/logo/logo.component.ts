import { Component, Input, OnInit } from '@angular/core';
import { ImageSrc } from '@core/utils/image.utils';
@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo = false;
	public img: ImageSrc = { location: 'assets/logos/logo_HSI.svg', alt: 'HSI' };

	constructor() {
	}

	ngOnInit(): void {
		if (this.isSecondaryLogo) {
			this.img = {
				location: `assets/custom/sponsor-logo-512x128.png`,
				alt: 'Sponsor Logo'
			};
		}
	}

}
