import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent implements OnInit {

	@Input() internmentEpisode: InternmentEpisode = {
		room: {
			number: 404
		},
		bed: {
			number: 600
		},
		floor: {
			number: '5° PISO',
			description: 'Terapia intensiva'
		},
		doctor: {
			firstName: 'Tomas',
			lastName: 'Lopez',
			license: '12345',
		},
		daysInterned: 50,
		admissionDatetime: '20/06/2020 - 8:26hs',
	};

	constructor() { }

	ngOnInit(): void {
	}

}

export interface InternmentEpisode {
	room: {
		number: number;
	},
	bed: {
		number: number;
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
}
