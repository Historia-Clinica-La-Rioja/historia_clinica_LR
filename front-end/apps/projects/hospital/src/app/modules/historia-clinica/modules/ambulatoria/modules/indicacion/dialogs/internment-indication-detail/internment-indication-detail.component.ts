import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from '@api-rest/api-model';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { ExtraInfo, Status, Title } from "@presentation/components/indication/indication.component"
import { getOtherIndicationType, loadExtraInfoParenteralPlan, loadExtraInfoPharmaco, showFrequency } from '../../constants/load-information';

@Component({
	selector: 'app-internment-indication-detail',
	templateUrl: './internment-indication-detail.component.html',
	styleUrls: ['./internment-indication-detail.component.scss']
})
export class InternmentIndicationDetailComponent implements OnInit {

	title: string;
	information: ExtraInfo[] = [];
	vias: any[] = [];
	othersIndicatiosType: OtherIndicationTypeDto[] = [];
	patientProvided = false;
	foodRelationId = 0;
	healthCondition: string;
	observations: string;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			indication: DietDto | OtherIndicationDto | ParenteralPlanDto | PharmacoDto,
			header: Title,
			status: Status
		},
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly healthConditionService: HealthConditionService
	) { }

	ngOnInit(): void {
		this.internacionMasterdataService.getVias().subscribe(
			v => {
				this.vias = v;
				this.loadInformation();
			});

	}

	loadInformation(): void {
		this.information = [];
		switch (this.data.indication.type) {
			case 'DIET': {
				const diet = this.data.indication as DietDto;
				this.title = diet.description;
				break;
			}
			case 'PARENTERAL_PLAN': {
				const parenteralPlan = this.data.indication as ParenteralPlanDto;
				this.title = parenteralPlan.snomed.pt;
				this.information = this.loadParenteralPlanInformation(parenteralPlan);
				break;
			}
			case 'PHARMACO': {
				const pharmaco = this.data.indication as PharmacoDto;
				this.healthConditionService.getHealthCondition(pharmaco.healthConditionId).subscribe(i => {
					this.healthCondition = i.snomed.pt;
					this.title = pharmaco.snomed.pt;
					this.information = this.loadPharmacoInformation(pharmaco);
					this.patientProvided = pharmaco.patientProvided;
					this.foodRelationId = pharmaco.foodRelationId;
					this.observations = pharmaco.note;
				});
				break;
			}
			case 'OTHER_INDICATION': {
				const otherIndication = this.data.indication as OtherIndicationDto;
				this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
					this.title = getOtherIndicationType(otherIndication, i);
					this.information = this.loadOtherIndicationInformation();
				});
				break;
			}
		}
	}

	loadPharmacoInformation(pharmaco: PharmacoDto): ExtraInfo[] {
		let information: ExtraInfo[] = [];
		information = loadExtraInfoPharmaco(pharmaco, true, this.vias);
		if (pharmaco.solvent) {
			information = information.concat([{
				title: 'indicacion.internment-card.sections.indication-extra-description.SOLVENT',
				content: pharmaco.solvent.snomed.pt
			}, {
				title: 'indicacion.internment-card.sections.indication-extra-description.SOLVENT_DOSAGE',
				content: pharmaco.solvent.dosage.quantity.value + " " + pharmaco.solvent.dosage.quantity.unit + " "
			}])
		}
		return information;
	}

	loadParenteralPlanInformation(parenteralPlan: ParenteralPlanDto): ExtraInfo[] {
		let information: ExtraInfo[] = [];
		information = loadExtraInfoParenteralPlan(parenteralPlan, this.vias);

		if (parenteralPlan.frequency) {
			information = information.concat(showFrequency(parenteralPlan.dosage));
			information = information.concat([{
				title: 'indicacion.internment-card.sections.indication-extra-description.DURATION',
				content: parenteralPlan.frequency.duration.hours + ' hs. ' + parenteralPlan.frequency.duration.minutes + ' min'
			}, {
				title: 'indicacion.internment-card.sections.indication-extra-description.FLOW',
				content: parenteralPlan.frequency.flowMlHour + ' ml/h | ' + parenteralPlan.frequency.flowDropsHour + ' hp. Gotas'
			}, {
				title: 'indicacion.internment-card.sections.indication-extra-description.VOLUMEN_DAY',
				content: parenteralPlan.frequency.dailyVolume.toString()
			}])
		}

		if (parenteralPlan.pharmacos) {
			parenteralPlan.pharmacos.forEach(pharmaco => {
				information = information.concat([{
					title: 'indicacion.internment-card.sections.indication-extra-description.PHARMACO',
					content: pharmaco.snomed.pt
				}, {
					title: 'indicacion.internment-card.sections.indication-extra-description.PHARMACO_DOSE',
					content: pharmaco.dosage.quantity.value + ' ' + pharmaco.dosage.quantity.unit
				}])
			});
		}
		return information;
	}

	loadOtherIndicationInformation(): ExtraInfo[] {
		const otherIndication = this.data.indication as OtherIndicationDto;
		let information: ExtraInfo[] = [];

		information = information.concat([{
			title: null,
			content: '"' + otherIndication.description + '"'
		}]);
		information = information.concat(showFrequency(otherIndication.dosage));
		return information;
	}

}

