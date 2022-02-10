import { Component, Input } from '@angular/core';
import { Router } from "@angular/router";
import { ContextService } from "@core/services/context.service";

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_RELOCATE_PATIENT_BED_PREFIX = '/pase-cama';
const ROUTE_PATIENT_SUFIX = '/paciente/';
const ROUTE_ADMINISTRATIVE_DISCHARGE_PREFIX = '/alta';


@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent {

	@Input() internmentEpisode: InternmentEpisodeSummary;
	@Input() canLoadProbableDischargeDate: boolean;
	@Input() patientId : number;
	@Input() showDischarge: boolean;
	@Input() hasEpicrisis: boolean;
	private readonly routePrefix;

	constructor(private router: Router,
				private contextService: ContextService,
				) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	goToPaseCama(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_RELOCATE_PATIENT_BED_PREFIX]);
	}

	goToAdministrativeDischarge(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_ADMINISTRATIVE_DISCHARGE_PREFIX]);
	}
}

export interface InternmentEpisodeSummary {
	id: number;
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
