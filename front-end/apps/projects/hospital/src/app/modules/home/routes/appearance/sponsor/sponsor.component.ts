import { Component, OnInit } from '@angular/core';
import { Asset, SponsorLogo } from '../asset.model';

@Component({
	selector: 'app-sponsor',
	templateUrl: './sponsor.component.html',
	styleUrls: ['./sponsor.component.scss']
})
export class SponsorComponent implements OnInit {
	sponsorLogo: Asset;

	constructor() { }

	ngOnInit(): void {
		this.sponsorLogo = new SponsorLogo();
	}

}
