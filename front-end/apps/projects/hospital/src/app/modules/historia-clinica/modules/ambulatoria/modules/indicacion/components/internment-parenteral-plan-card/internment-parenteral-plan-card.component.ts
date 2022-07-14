import { Component, Input, OnChanges } from '@angular/core';
import { ParenteralPlanDto } from '@api-rest/api-model';
import { IndicationStatus, IndicationStatusScss, PARENTERAL_PLAN, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content, ExtraInfo } from "@presentation/components/indication/indication.component";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { loadExtraInfoParenteralPlan } from '../../constants/load-information';

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
					description: IndicationStatus[parenteralPlan.status],
					cssClass: IndicationStatusScss[parenteralPlan.status],
					type: parenteralPlan.type
				},
				description: parenteralPlan.snomed.pt,
				extra_info: loadExtraInfoParenteralPlan(parenteralPlan, this.vias),
				createdBy: parenteralPlan.createdBy,
				timeElapsed: showTimeElapsed(parenteralPlan.createdOn),
			}
		});
	}
}
