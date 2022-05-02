import { Component, Input, OnChanges } from '@angular/core';
import { PharmacoDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { PHARMACO, showFrequencyPharmaco, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content, ExtraInfo } from "@presentation/components/indication/indication.component";

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
					description: pharmaco.status === "INDICATED" ? 'indicacion.internment-card.sections.label.INDICATED' : 'indicacion.internment-card.sections.label.SUSPENDED',
					cssClass: pharmaco.status === "INDICATED" ? 'blue' : 'red'
				},
				description: pharmaco.snomed.pt,
				extra_info: loadExtraInfo(pharmaco, this.vias),
				createdBy: pharmaco.createdBy,
				timeElapsed: showTimeElapsed(pharmaco.createdOn),
			}
		});
		function loadExtraInfo(pharmaco: any, vias: any[]): ExtraInfo[] {
			const extra_info = [];
			if (pharmaco.dosage.quantity?.value) {
				extra_info.push({
					title: 'indicacion.internment-card.sections.indication-extra-description.DOSE',
					content: pharmaco.dosage.quantity.value + " " + pharmaco.dosage.quantity.unit + " "
				})
			}

			extra_info.push({
				title: 'indicacion.internment-card.sections.indication-extra-description.VIA',
				content: pharmaco.via
			})

			return  extra_info.concat(showFrequencyPharmaco(pharmaco));
		}
	}

}
