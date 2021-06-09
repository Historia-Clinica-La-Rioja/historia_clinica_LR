import { Component, Input, OnInit } from '@angular/core';
import { ImageSrc } from '@core/utils/image.utils';
@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo = false;
	public img: ImageSrc = { location: 'assets/custom/app_logo.svg', alt: 'logos.app_logo' };

	constructor() {
	}

	ngOnInit(): void {
		if (this.isSecondaryLogo) {
			this.img = {
				location: `assets/custom/sponsor-logo-512x128.png`,
				alt: 'logos.sponsor_logo'
			};
		}
	}

}
