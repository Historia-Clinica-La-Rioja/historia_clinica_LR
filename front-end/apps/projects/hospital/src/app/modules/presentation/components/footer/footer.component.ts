import {Component, OnInit} from '@angular/core';
import {PublicService} from '@api-rest/services/public.service';
import {ImageSrc} from '@core/utils/image.utils';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

	public applicationVersionNumber: string;
	public BASE_URL = 'assets/custom/footer/';
	footerImages: ImageSrc[] = [
		{
			location: this.BASE_URL + 'footer_left.png',
			alt: 'footer_left'
		},
		{
			location: this.BASE_URL + 'footer_center.png',
			alt: 'footer_center'
		},
		{
			location: this.BASE_URL + 'footer_right	.png',
			alt: 'footer_right'
		}
	]
	constructor(
		private publicService: PublicService,
	) {
	}

	ngOnInit(): void {
		this.publicService.getApplicationCurrentVersion().subscribe(versionDto => this.applicationVersionNumber = versionDto.version);
	}

}
