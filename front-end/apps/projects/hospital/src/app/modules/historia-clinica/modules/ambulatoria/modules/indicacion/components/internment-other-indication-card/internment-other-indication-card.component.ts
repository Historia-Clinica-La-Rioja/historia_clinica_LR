import { Component, Input, OnChanges } from '@angular/core';
import { IndicationStatus, IndicationStatusScss, OTHER_INDICATION, OTHER_INDICATION_ID, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from '@presentation/components/indication/indication.component';
import { OtherIndicationDto } from '@api-rest/api-model';
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { showFrequency } from '../../constants/load-information';
import { MatDialog } from '@angular/material/dialog';
import { InternmentIndicationDetailComponent } from '../../dialogs/internment-indication-detail/internment-indication-detail.component';


const DIALOG_SIZE = '35%';
@Component({
	selector: 'app-internment-other-indication-card',
	templateUrl: './internment-other-indication-card.component.html',
	styleUrls: ['./internment-other-indication-card.component.scss']
})
export class InternmentOtherIndicationCardComponent implements OnChanges {

	OTHER_INDICATION = OTHER_INDICATION;

	OTHER_INDICATION_ID = OTHER_INDICATION_ID;

	indicationContent: Content[] = [];

	othersIndicatiosType: OtherIndicationTypeDto[] = [];

	@Input() otherIndications: OtherIndicationDto[];
	@Input() internmentEpisodeId: number;


	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly internmentIndicationService: InternmentIndicationService,
	) { }

	ngOnChanges() {
		this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
			this.othersIndicatiosType = i; this.indicationContent = this.mapToIndicationContent();
		});
	}


	mapToIndicationContent(): Content[] {
		return this.otherIndications?.map((otherIndication: OtherIndicationDto) => {
			return {
				status: {
					description: IndicationStatus[otherIndication.status],
					cssClass: IndicationStatusScss[otherIndication.status],
					type: otherIndication.type
				},
				id: otherIndication.id,
				description: indication(otherIndication, this.othersIndicatiosType),
				createdBy: otherIndication.createdBy,
				timeElapsed: showTimeElapsed(otherIndication.createdOn),
				extra_info: otherIndication?.dosage ? showFrequency(otherIndication.dosage) : [],
			}

		});

		function indication(otherIndication: OtherIndicationDto, othersIndicatiosType: OtherIndicationTypeDto[]): string {
			const result = othersIndicatiosType.find(i => i.id === otherIndication.otherIndicationTypeId);
			return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
		}

	}

	openDetailDialog(content: Content): void{
		this.internmentIndicationService.getInternmentEpisodeOtherIndication(this.internmentEpisodeId, content.id)
		.subscribe(otherIndication => {
			this.dialog.open(InternmentIndicationDetailComponent, {
				data: {
					indication: otherIndication,
					header: this.OTHER_INDICATION,
					status: content.status
				},
				disableClose: false,
				width: DIALOG_SIZE
			});
		});
	}
}
