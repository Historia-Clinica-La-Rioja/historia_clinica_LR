import { Component, Input, OnChanges } from '@angular/core';
import { PharmacoDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { IndicationStatus, IndicationStatusScss, PHARMACO, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from "@presentation/components/indication/indication.component";
import { loadExtraInfoPharmaco } from '../../constants/load-information';

@Component({
	selector: 'app-internment-pharmaco-card',
	templateUrl: './internment-pharmaco-card.component.html',
	styleUrls: ['./internment-pharmaco-card.component.scss']
})
export class InternmentPharmacoCardComponent implements OnChanges {

	PHARMACO = PHARMACO;
	indicationContent: Content[] = [];
	vias: any[] = [];
	@Input() pharmacos: PharmacoDto[]
	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
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
					cssClass: IndicationStatusScss[pharmaco.status]
				},
				description: pharmaco.snomed.pt,
				extra_info: loadExtraInfoPharmaco(pharmaco, true),
				createdBy: pharmaco.createdBy,
				timeElapsed: showTimeElapsed(pharmaco.createdOn),
			}
		});
	}
}

