import { NursingRecordStatus, NursingRecordStatusScss } from './../../constants/internment-indications';
import { Component, Input } from '@angular/core';
import { EIndicationType, ENursingRecordStatus } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { IndicationMatIcon, IndicationSvgIcon } from '../../constants/internment-indications';
import { getOtherIndicationType, loadExtraInfoParenteralPlan } from '../../constants/load-information';
import { NursingRecord } from '../nursing-record/nursing-record.component';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';

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

function toNursingRecords(nursingRecordsDto: any[], othersIndicatiosTypes: OtherIndicationTypeDto[], vias: any[]): NursingRecord[] {
	return nursingRecordsDto.map(r => {
		let description: string;
		switch (r.indication.type) {
			case EIndicationType.DIET: {
				description = r.indication.description;
				break;
			}
			case EIndicationType.PARENTERAL_PLAN: {
				description = r.indication.snomed.pt
				break;
			}
			case EIndicationType.OTHER_INDICATION: {
				description = getOtherIndicationType(r.indication, othersIndicatiosTypes);
				break;
			}
		}
		return {
			id: r.id,
			matIcon: IndicationMatIcon[r.indication.type],
			svgIcon: IndicationSvgIcon[r.indication.type],
			content: {
				status: {
					description: NursingRecordStatus[r.status],
					cssClass: NursingRecordStatusScss[r.status],
					type: r.status
				},
				description,
				extra_info: (EIndicationType.PARENTERAL_PLAN === r.indication.type) ? loadExtraInfoParenteralPlan(r.indication, vias) : null,
				indicationDate: dateDtoToDate(r.indication.indicationDate),
				scheduledAdministrationTime: r.scheduledAdministrationTime,
				administeredBy: r.updatedBy,
				administeredTime: (r.administrationTime) ? dateTimeDtotoLocalDate(r.administrationTime) : null,
				reason: ENursingRecordStatus.REJECTED === r.status ? r.updateReason : null
			}
		}
	});
}