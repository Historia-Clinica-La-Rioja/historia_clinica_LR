import { Component, Input, OnChanges, Output } from '@angular/core';
import { IndicationStatus, IndicationStatusScss, INDICATION_TYPE, OTHER_INDICATION, OTHER_INDICATION_ID } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from '@presentation/components/indication/indication.component';
import { OtherIndicationDto } from '@api-rest/api-model';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { showFrequency } from '../../constants/load-information';
import { MatDialog } from '@angular/material/dialog';
import { InternmentIndicationDetailComponent } from '../../dialogs/internment-indication-detail/internment-indication-detail.component';
import { IndicationService } from '@api-rest/services/indication.service';
import { Subject } from 'rxjs';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { ShowTimeElapsedPipe } from '@presentation/pipes/show-time-elapsed.pipe';


const DIALOG_SIZE = '35%';
@Component({
	selector: 'app-internment-other-indication-card',
	templateUrl: './internment-other-indication-card.component.html',
	styleUrls: ['./internment-other-indication-card.component.scss']
})
export class InternmentOtherIndicationCardComponent implements OnChanges {
	INDICATION = INDICATION_TYPE.OTHER_INDICATION;

	OTHER_INDICATION = OTHER_INDICATION;

	OTHER_INDICATION_ID = OTHER_INDICATION_ID;

	indicationContent: Content[] = [];

	othersIndicatiosType: OtherIndicationTypeDto[] = [];

	@Input() otherIndications: OtherIndicationDto[];
	@Input() internmentEpisodeId: number;
	@Output() actioned = new Subject();


	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly indicationService: IndicationService
	) { }

	ngOnChanges() {
		this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
			this.othersIndicatiosType = i; this.indicationContent = this.mapToIndicationContent();
		});
	}
	action(event) {
		this.actioned.next(event)
	}


	mapToIndicationContent(): Content[] {
		return this.otherIndications?.map((otherIndication: OtherIndicationDto) => {
			const createdOn = dateTimeDtotoLocalDate(otherIndication.createdOn);
			const showTimeElapsedPipe = new ShowTimeElapsedPipe();
			return {
				status: {
					description: IndicationStatus[otherIndication.status],
					cssClass: IndicationStatusScss[otherIndication.status],
					type: otherIndication.type
				},
				id: otherIndication.id,
				description: indication(otherIndication, this.othersIndicatiosType),
				createdBy: otherIndication.createdBy,
				timeElapsed: showTimeElapsedPipe.transform(createdOn),
				extra_info: otherIndication?.dosage ? showFrequency(otherIndication.dosage) : [],
				observations: otherIndication.description
			}

		});

		function indication(otherIndication: OtherIndicationDto, othersIndicatiosType: OtherIndicationTypeDto[]): string {
			const result = othersIndicatiosType.find(i => i.id === otherIndication.otherIndicationTypeId);
			return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
		}

	}

	openDetailDialog(content: Content): void {
		this.indicationService.getOtherIndication(content.id)
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
