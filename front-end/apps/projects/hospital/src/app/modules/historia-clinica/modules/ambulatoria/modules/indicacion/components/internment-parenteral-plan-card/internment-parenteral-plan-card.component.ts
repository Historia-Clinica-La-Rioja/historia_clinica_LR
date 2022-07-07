import { Component, Input, OnChanges } from '@angular/core';
import { ParenteralPlanDto } from '@api-rest/api-model';
import { PARENTERAL_PLAN, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content, ExtraInfo } from "@presentation/components/indication/indication.component";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";

@Component({
	selector: 'app-internment-parenteral-plan-card',
	templateUrl: './internment-parenteral-plan-card.component.html',
	styleUrls: ['./internment-parenteral-plan-card.component.scss']
})
export class InternmentParenteralPlanCardComponent implements OnChanges {

	PARENTERAL_PLAN = PARENTERAL_PLAN;
	indicationContent: Content[] = [];
	vias: any[] = [];
	@Input() parenteralPlans: ParenteralPlanDto[]

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }

	ngOnChanges(): void {
		this.internacionMasterdataService.getVias().subscribe(v => {
			this.vias = v;
			this.indicationContent = this.mapToIndicationContent();
		});
	}

	mapToIndicationContent(): Content[] {
		return this.parenteralPlans?.map((parenteralPlan: ParenteralPlanDto) => {
			return {
				status: {
					description: parenteralPlan.status === "INDICATED" ? 'indicacion.internment-card.sections.label.INDICATED' : 'indicacion.internment-card.sections.label.SUSPENDED',
					cssClass: parenteralPlan.status === "INDICATED" ? 'blue' : 'red'
				},
				description: parenteralPlan.snomed.pt,
				extra_info: loadExtraInfo(parenteralPlan, this.vias),
				createdBy: parenteralPlan.createdBy,
				timeElapsed: showTimeElapsed(parenteralPlan.createdOn),
			}
		});

		function loadExtraInfo(parenteralPlan: ParenteralPlanDto, vias: any[]): ExtraInfo[] {
			const extra_info = [];
			if (parenteralPlan.dosage.quantity?.value) {
				extra_info.push({
					title: 'indicacion.internment-card.sections.indication-extra-description.VOL/BOLSA',
					content: parenteralPlan.dosage.quantity.value + " ml"
				})
			}
			if (parenteralPlan.via) {
				extra_info.push({
					title: 'indicacion.internment-card.sections.indication-extra-description.VIA',
					content: vias.find(v => v.id === parenteralPlan.via)?.description
				})
			}
			return extra_info;
		}
	}
}
