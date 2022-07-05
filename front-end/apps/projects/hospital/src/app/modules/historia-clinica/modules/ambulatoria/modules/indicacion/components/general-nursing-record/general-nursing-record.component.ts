import { Component, Input } from '@angular/core';
import { EIndicationType } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { getOtherIndicationType } from '../../constants/load-information';
import { NursingRecord } from '../nursing-record/nursing-record.component';

@Component({
	selector: 'app-general-nursing-record',
	templateUrl: './general-nursing-record.component.html',
	styleUrls: ['./general-nursing-record.component.scss']
})
export class GeneralNursingRecordComponent {

	nursingRecords: NursingRecord[] = [];
	@Input()
	set nursingRecordsDto (nursingRecordsDto: any[]) {
		this.internacionMasterdataService.getOtherIndicationTypes().subscribe(othersIndicatiosTypes =>
			this.nursingRecords = toNursingRecords(nursingRecordsDto, othersIndicatiosTypes)
		);
	}

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }
}

function toNursingRecords(nursingRecordsDto: any[], othersIndicatiosTypes: OtherIndicationTypeDto[]): NursingRecord[] {
	return nursingRecordsDto.map(r => {
		return {
			matIcon: (r.indication.type === EIndicationType.OTHER_INDICATION) ? 'assignment_late' : 'local_dining',
			content: {
				status: {
					description: 'indicacion.nursing-care.status.PENDING',
					cssClass: 'red'
				},
				description: (r.indication.type === EIndicationType.OTHER_INDICATION) ? getOtherIndicationType(r, othersIndicatiosTypes) : r.indication.description,
				createdBy: "",
				timeElapsed: ""
			}
		}
	});
}