import { Component, Input } from '@angular/core';
import { EIndicationType } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { getOtherIndicationType, loadExtraInfoParenteralPlan } from '../../constants/load-information';
import { NursingRecord } from '../nursing-record/nursing-record.component';

@Component({
	selector: 'app-general-nursing-record',
	templateUrl: './general-nursing-record.component.html',
	styleUrls: ['./general-nursing-record.component.scss']
})
export class GeneralNursingRecordComponent {

	nursingRecords: NursingRecord[] = [];
	@Input()
	set nursingRecordsDto(nursingRecordsDto: any[]) {
		this.internacionMasterdataService.getVias().subscribe(v => {
			this.internacionMasterdataService.getOtherIndicationTypes().subscribe(othersIndicatiosTypes =>
				this.nursingRecords = toNursingRecords(nursingRecordsDto, othersIndicatiosTypes, v));
		});
	}

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }
}

function toNursingRecords(nursingRecordsDto: any[], othersIndicatiosTypes: OtherIndicationTypeDto[], vias:any[]): NursingRecord[] {
	return nursingRecordsDto.map(r => {
		let svgIcon: string;
		let matIcon: string;
		let description: string;
		switch (r.indication.type) {
			case EIndicationType.DIET: {
				matIcon = 'local_dining';
				description = r.indication.description;
				break;
			}
			case EIndicationType.PARENTERAL_PLAN: {
				svgIcon = 'parenteral_plans';
				description = r.indication.snomed.pt
				break;
			}
			case EIndicationType.OTHER_INDICATION: {
				matIcon = 'assignment_late';
				description = getOtherIndicationType(r.indication, othersIndicatiosTypes);
				break;
			}
		}
		return {
			matIcon,
			svgIcon,
			content: {
				status: {
					description: 'indicacion.nursing-care.status.PENDING',
					cssClass: 'red'
				},
				description,
				extra_info: (EIndicationType.PARENTERAL_PLAN === r.indication.type) ? loadExtraInfoParenteralPlan(r.indication, vias) : null,
				createdBy: "",
				timeElapsed: ""
			}
		}
	});
}