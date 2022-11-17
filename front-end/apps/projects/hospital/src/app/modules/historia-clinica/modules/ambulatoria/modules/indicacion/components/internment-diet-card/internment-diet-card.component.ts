import { Component, Input, OnChanges } from "@angular/core";
import { DIET, IndicationStatus, IndicationStatusScss, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { DietDto } from "@api-rest/api-model";
import { Content } from '@presentation/components/indication/indication.component';
import { MatDialog } from "@angular/material/dialog";
import { InternmentIndicationDetailComponent } from "../../dialogs/internment-indication-detail/internment-indication-detail.component";
import { InternmentIndicationService } from "@api-rest/services/internment-indication.service";


const DIALOG_SIZE = '35%';

@Component({
	selector: 'app-internment-diet-card',
	templateUrl: './internment-diet-card.component.html',
	styleUrls: ['./internment-diet-card.component.scss']
})
export class InternmentDietCardComponent implements OnChanges {

	DIET = DIET;
	indicationContent: Content[] = [];
	@Input() diets: DietDto[];
	@Input() internmentEpisodeId: number;

	constructor(
		private readonly dialog: MatDialog,
		private readonly internmentIndicationService: InternmentIndicationService,
	) { }

	ngOnChanges() {
		this.indicationContent = this.mapToIndicationContent();
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

	openDetailDialog(content: Content): void{
		this.internmentIndicationService.getInternmentEpisodeDiet(this.internmentEpisodeId, content.id)
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
