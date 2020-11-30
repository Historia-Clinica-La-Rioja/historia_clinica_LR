import { Component, Input, OnInit } from '@angular/core';
import { FlavoredImagesService } from "@core/services/flavored-images.service";
import { ImageSrc } from "@core/utils/flavored-image-definitions";

@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo: boolean = false;
	public logos: ImageSrc[] = []

	constructor(private flavoredImagesService: FlavoredImagesService) {
	}

	ngOnInit(): void {
		if(this.isSecondaryLogo) {
			this.flavoredImagesService.getHeaderSecondaryLogos().subscribe(logos => this.logos = logos);
		}
		else {
			this.flavoredImagesService.getLogos().subscribe(logos => this.logos = logos);
		}
	}

}
