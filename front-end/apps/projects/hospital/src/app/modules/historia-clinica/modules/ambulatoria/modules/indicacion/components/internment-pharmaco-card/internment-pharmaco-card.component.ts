import { Component, Input, OnChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PharmacoDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentIndicationService } from '@api-rest/services/internment-indication.service';
import { IndicationStatus, IndicationStatusScss, PHARMACO, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from "@presentation/components/indication/indication.component";
import { loadExtraInfoPharmaco } from '../../constants/load-information';
import { InternmentIndicationDetailComponent } from '../../dialogs/internment-indication-detail/internment-indication-detail.component';


const DIALOG_SIZE = '35%';
@Component({
	selector: 'app-internment-pharmaco-card',
	templateUrl: './internment-pharmaco-card.component.html',
	styleUrls: ['./internment-pharmaco-card.component.scss']
})
export class InternmentPharmacoCardComponent implements OnChanges {

	PHARMACO = PHARMACO;
	indicationContent: Content[] = [];
	vias: any[] = [];
	@Input() pharmacos: PharmacoDto[];
	@Input() internmentEpisodeId: number;
	
	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly internmentIndicationService: InternmentIndicationService,
	) { }
	ngOnChanges(): void {
		this.internacionMasterdataService.getVias().subscribe(v => this.vias = v);
		this.indicationContent = this.mapToIndicationContent();
	}

	mapToIndicationContent(): Content[] {
		return this.pharmacos?.map((pharmaco: any) => {
			return {
				status: {
					description: IndicationStatus[pharmaco.status],
					cssClass: IndicationStatusScss[pharmaco.status],
					type: pharmaco.type
				},
				id: pharmaco.id,
				description: pharmaco.snomed.pt,
				extra_info: loadExtraInfoPharmaco(pharmaco, true),
				createdBy: pharmaco.createdBy,
				timeElapsed: showTimeElapsed(pharmaco.createdOn),
			}
		});
	}

	openDetailDialog(content: Content): void{
		this.internmentIndicationService.getInternmentEpisodePharmaco(this.internmentEpisodeId, content.id)
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

