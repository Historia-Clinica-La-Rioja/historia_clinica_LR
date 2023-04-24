import { Component, Input, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { isSameDay } from "date-fns";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { DiagnosesGeneralStateDto, DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto, PharmacoSummaryDto } from "@api-rest/api-model";

import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationsFacadeService } from "@historia-clinica/modules/ambulatoria/modules/indicacion/services/indications-facade.service";
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';


import { ActionsButtonService } from '../../services/actions-button.service';

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss'],
	providers: [ActionsButtonService]

})
export class InternmentIndicationsCardComponent implements OnInit {
	internmentIndication = INTERNMENT_INDICATIONS;
	actualDate: Date;
	entryDate: Date;
	professionalId: number;
	diets: DietDto[] = [];
	diagnostics: DiagnosesGeneralStateDto[] = [];
	otherIndications: OtherIndicationDto[] = [];
	othersIndicatiosType: OtherIndicationTypeDto[];
	parenteralPlan: ParenteralPlanDto[] = [];
	pharmacos: PharmacoDto[] = [];
	@Input() internmentEpisodeId: number;
	@Input() epicrisisConfirmed: boolean;
	@Input() patientId: number;

	constructor(
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly internmentIndicationService: InternmentIndicationService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly actionsButtonService: ActionsButtonService,
	) {
	}

	ngOnInit(): void {
		this.actionsButtonService.internmentEpisodeId = this.internmentEpisodeId;
		this.actionsButtonService.epicrisisConfirmed = this.epicrisisConfirmed;
		this.actionsButtonService.patientId = this.patientId;
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
		);

		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) => this.professionalId = professionalId);

		this.indicationsFacadeService.setInternmentEpisodeId(this.internmentEpisodeId);

		this.internmentIndicationService.getOtherIndicationTypes().subscribe((othersIndicationsType: OtherIndicationTypeDto[]) => this.othersIndicatiosType = othersIndicationsType);
	}

	loadActualDateAndFilter(actualDate: Date) {
		this.actionsButtonService.actualDate = actualDate;
		this.actualDate = actualDate;
		this.filterIndications();
	}

	filterIndications() {
		this.indicationsFacadeService.diets$.subscribe(d => this.diets = d.filter((diet: DietDto) => isSameDay(dateDtoToDate(diet.indicationDate), this.actualDate)));
		this.indicationsFacadeService.otherIndications$.subscribe(d => this.otherIndications = d.filter((otherIndications: OtherIndicationDto) => isSameDay(dateDtoToDate(otherIndications.indicationDate), this.actualDate)));
		this.indicationsFacadeService.parenteralPlans$.subscribe(p => this.parenteralPlan = p.filter((plan: ParenteralPlanDto) => isSameDay(dateDtoToDate(plan.indicationDate), this.actualDate)));
		this.indicationsFacadeService.pharmacos$.subscribe(p => this.pharmacos = p.filter((pharmaco: PharmacoDto) => isSameDay(dateDtoToDate(pharmaco.indicationDate), this.actualDate)));
	}

}

export interface DialogPharmacosFrequent<T> {
	openFormPharmaco: boolean;
	pharmaco: T;
}

interface ResultDialogPharmaco<T> {
	openDialogPharmacosFrequent: boolean;
	pharmaco?: T;
}

