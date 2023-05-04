import { Component, Input, OnChanges, Output } from "@angular/core";
import { DIET, IndicationStatus, IndicationStatusScss, INDICATION_TYPE, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { DietDto, EIndicationType } from "@api-rest/api-model";
import { Content } from '@presentation/components/indication/indication.component';
import { MatDialog } from "@angular/material/dialog";
import { InternmentIndicationDetailComponent } from "../../dialogs/internment-indication-detail/internment-indication-detail.component";
import { IndicationService } from "@api-rest/services/indication.service";
import { Subject } from "rxjs";


const DIALOG_SIZE = '35%';

@Component({
	selector: 'app-internment-diet-card',
	templateUrl: './internment-diet-card.component.html',
	styleUrls: ['./internment-diet-card.component.scss']
})
export class InternmentDietCardComponent implements OnChanges {
	INDICATION = INDICATION_TYPE.DIET;
	DIET = DIET;
	indicationContent: Content[] = [];
	@Input() diets: DietDto[];
	@Output() actioned: Subject<EIndicationType> = new Subject();

	constructor(
		private readonly dialog: MatDialog,
		private readonly indicationService: IndicationService
	) { }

	ngOnChanges() {
		this.indicationContent = this.mapToIndicationContent();
	}

	action(event) {
		this.actioned.next(event)
	}

	mapToIndicationContent(): Content[] {
		return this.diets?.map((diet: DietDto) => {
			return {
				status: {
					description: IndicationStatus[diet.status],
					cssClass: IndicationStatusScss[diet.status],
					type: diet.type
				},
				id: diet.id,
				description: diet.description,
				createdBy: diet.createdBy,
				timeElapsed: showTimeElapsed(diet.createdOn),
			}
		});
	}

	openDetailDialog(content: Content): void {
		this.indicationService.getDiet(content.id)
			.subscribe(diet => {
				this.dialog.open(InternmentIndicationDetailComponent, {
					data: {
						indication: diet,
						header: this.DIET,
						status: content.status
					},
					disableClose: false,
					width: DIALOG_SIZE
				});
			});
	}
}
