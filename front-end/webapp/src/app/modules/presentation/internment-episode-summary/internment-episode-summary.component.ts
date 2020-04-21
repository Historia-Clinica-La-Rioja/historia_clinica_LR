import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent implements OnInit {

	@Input() internmentEpisode: InternmentEpisode;

	constructor() { }

	ngOnInit(): void {
	}

}

export interface InternmentEpisode {
	room: {
		number: string;
	},
	bed: {
		number: string;
	},
	specialty: {
		name: string;
	};
	doctor: {
		firstName: string;
		lastName: string;
		license: string;
	};
	totalInternmentDays: number;
	admissionDatetime: string;
}

/*export interface InternmentEpisode {
	room: {
		number: string;
	},
	bed: {
		number: string;
	},
	floor: {
		number: string;
		description: string;
	};
	doctor: {
		firstName: string;
		lastName: string;
		license: string;
	};
	daysInterned: number;
	admissionDatetime: string;
}*/
