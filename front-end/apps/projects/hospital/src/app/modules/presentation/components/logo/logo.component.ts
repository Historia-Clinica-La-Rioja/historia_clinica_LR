import { Component, Input, OnInit } from '@angular/core';
import { ImageSrc } from '@core/utils/image.utils';
@Component({
	selector: 'app-logo',
	templateUrl: './logo.component.html',
	styleUrls: ['./logo.component.scss']
})
export class LogoComponent implements OnInit {

	@Input() isSecondaryLogo = false;
	public img: ImageSrc = { location: '', alt: '' };

	constructor() {
	}

	ngOnInit(): void {
	
	}

}
