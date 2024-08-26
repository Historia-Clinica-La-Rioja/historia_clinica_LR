import { Component, Input, OnChanges, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MasterDataDto, PharmacoDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { IndicationStatus, IndicationStatusScss, INDICATION_TYPE, PHARMACO } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from "@presentation/components/indication/indication.component";
import { loadExtraInfoPharmaco } from '../../constants/load-information';
import { InternmentIndicationDetailComponent } from '../../dialogs/internment-indication-detail/internment-indication-detail.component';
import { IndicationService } from '@api-rest/services/indication.service';
import { Subject } from 'rxjs';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { ShowTimeElapsedPipe } from '@historia-clinica/pipes/show-time-elapsed.pipe';


const DIALOG_SIZE = '35%';
@Component({
	selector: 'app-internment-pharmaco-card',
	templateUrl: './internment-pharmaco-card.component.html',
	styleUrls: ['./internment-pharmaco-card.component.scss']
})
export class InternmentPharmacoCardComponent implements OnChanges {
	INDICATION = INDICATION_TYPE.PHARMACO;
	PHARMACO = PHARMACO;
	indicationContent: Content[] = [];
	vias: MasterDataDto[] = [];
	@Input() pharmacos: PharmacoDto[];
	@Input() internmentEpisodeId: number;
	@Output() actioned = new Subject();

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly indicationService: IndicationService
	) {
		this.internacionMasterdataService.getViasPharmaco().subscribe(v => this.vias = v);
	}
	ngOnChanges(): void {
		this.indicationContent = this.mapToIndicationContent();
	}

	action(event) {
		this.actioned.next(event)
	}

	mapToIndicationContent(): Content[] {
		return this.pharmacos?.map((pharmaco: any) => {
			const createdOn = dateTimeDtotoLocalDate(pharmaco.createdOn);
			const showTimeElapsedPipe = new ShowTimeElapsedPipe();
			return {
				status: {
					description: IndicationStatus[pharmaco.status],
					cssClass: IndicationStatusScss[pharmaco.status],
					type: pharmaco.type
				},
				id: pharmaco.id,
				description: pharmaco.snomed.pt,
				extra_info: loadExtraInfoPharmaco(pharmaco, true, this.vias),
				createdBy: pharmaco.createdBy,
				timeElapsed: showTimeElapsedPipe.transform(createdOn),
				observations: pharmaco.note ? pharmaco.note.trim() : ''
			}
		});
	}

	openDetailDialog(content: Content): void {
		this.indicationService.getPharmaco(content.id)
			.subscribe(pharmaco => {
				this.dialog.open(InternmentIndicationDetailComponent, {
					data: {
						indication: pharmaco,
						header: this.PHARMACO,
						status: content.status
					},
					disableClose: false,
					width: DIALOG_SIZE
				});
			});
	}
}

