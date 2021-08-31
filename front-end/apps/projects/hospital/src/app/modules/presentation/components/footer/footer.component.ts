import {Component, OnInit} from '@angular/core';
import {PublicService} from '@api-rest/services/public.service';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
	applicationVersionNumber: string;

	constructor(
		private publicService: PublicService,
	) {
	}

	ngOnInit(): void {
		this.publicService.getApplicationCurrentVersion().subscribe(versionDto => this.applicationVersionNumber = versionDto.version);
	}

}
