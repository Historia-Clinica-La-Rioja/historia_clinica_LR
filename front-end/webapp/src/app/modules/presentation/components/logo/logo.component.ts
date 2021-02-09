import { Component, Input, OnInit } from '@angular/core';
import { FlavoredImagesService } from '@core/services/flavored-images.service';
import { ImageSrc } from '@core/utils/flavored-image-definitions';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo: boolean = false;
	public logos$: Observable<ImageSrc[]>;

	constructor(private flavoredImagesService: FlavoredImagesService) {
	}

	ngOnInit(): void {
		this.logos$ = this.isSecondaryLogo ?
			this.flavoredImagesService.getHeaderSecondaryLogos() :
			this.flavoredImagesService.getLogos();
	}

}
