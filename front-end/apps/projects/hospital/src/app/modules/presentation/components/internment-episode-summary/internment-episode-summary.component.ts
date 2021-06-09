import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent {

	@Input() internmentEpisode: InternmentEpisodeSummary;
	@Input() canLoadProbableDischargeDate: boolean;

	constructor() { }

}

export interface InternmentEpisodeSummary {
	roomNumber: string;
	bedNumber: string;
	sectorDescription: string;
	episodeSpecialtyName: string;
	doctor: {
		firstName: string;
		lastName: string;
		license: string;
	};
	totalInternmentDays: number;
	admissionDatetime: string;
	responsibleContact?: {
		fullName: string;
		relationship: string;
		phoneNumber: string;
	};
	probableDischargeDate: string;
}
