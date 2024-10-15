import { Component, Input, OnChanges, Output } from '@angular/core';
import { ParenteralPlanDto } from '@api-rest/api-model';
import { IndicationStatus, IndicationStatusScss, INDICATION_TYPE, PARENTERAL_PLAN } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from "@presentation/components/indication/indication.component";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { loadExtraInfoParenteralPlan } from '../../constants/load-information';
import { MatDialog } from '@angular/material/dialog';
import { InternmentIndicationDetailComponent } from '../../dialogs/internment-indication-detail/internment-indication-detail.component';
import { IndicationService } from '@api-rest/services/indication.service';
import { Subject } from 'rxjs';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { ShowTimeElapsedPipe } from '@presentation/pipes/show-time-elapsed.pipe';

const DIALOG_SIZE = '35%';
@Component({
	selector: 'app-internment-parenteral-plan-card',
	templateUrl: './internment-parenteral-plan-card.component.html',
	styleUrls: ['./internment-parenteral-plan-card.component.scss']
})
export class InternmentParenteralPlanCardComponent implements OnChanges {
	INDICATION = INDICATION_TYPE.PARENTERAL_PLAN;
	PARENTERAL_PLAN = PARENTERAL_PLAN;
	indicationContent: Content[] = [];
	vias: any[] = [];
	@Input() parenteralPlans: ParenteralPlanDto[];
	@Input() internmentEpisodeId: number;
	@Output() actioned = new Subject();

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly indicationService: IndicationService
	) { }

	ngOnChanges(): void {
		this.internacionMasterdataService.getVias().subscribe(v => {
			this.vias = v;
			this.indicationContent = this.mapToIndicationContent();
		});
	}

	action(event) {
		this.actioned.next(event);
	}

	mapToIndicationContent(): Content[] {
		return this.parenteralPlans?.map((parenteralPlan: ParenteralPlanDto) => {
			const createdOn = dateTimeDtotoLocalDate(parenteralPlan.createdOn);
			const showTimeElapsedPipe = new ShowTimeElapsedPipe();
			return {
				status: {
					description: IndicationStatus[parenteralPlan.status],
					cssClass: IndicationStatusScss[parenteralPlan.status],
					type: parenteralPlan.type
				},
				id: parenteralPlan.id,
				description: parenteralPlan.snomed.pt,
				extra_info: loadExtraInfoParenteralPlan(parenteralPlan, this.vias),
				createdBy: parenteralPlan.createdBy,
				timeElapsed: showTimeElapsedPipe.transform(createdOn),
			}
		});
	}

	openDetailDialog(content: Content): void {
		this.indicationService.getParenteralPlan(content.id)
			.subscribe(parenteralPlan => {
				this.dialog.open(InternmentIndicationDetailComponent, {
					data: {
						indication: parenteralPlan,
						header: this.PARENTERAL_PLAN,
						status: content.status
					},
					disableClose: false,
					width: DIALOG_SIZE
				});
			});
	}
}
