import { Component, Input, SimpleChanges, OnChanges } from '@angular/core';
import { OtherIndicationDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { OTHER_INDICATION_ID } from '../../constants/internment-indications';
import { NursingRecord } from '../nursing-record/nursing-record.component';

@Component({
	selector: 'app-general-nursing-record',
	templateUrl: './general-nursing-record.component.html',
	styleUrls: ['./general-nursing-record.component.scss']
})
export class GeneralNursingRecordComponent implements OnChanges {

	nursingSections: any[] = [];
	othersIndicatiosType: OtherIndicationTypeDto[];
	@Input() nursingRecords: any[];

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }

	ngOnChanges(changes: SimpleChanges) {
		if (changes.nursingRecords.currentValue) {
			this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
				this.othersIndicatiosType = i;
				this.nursingSections = this.mapNRecordToNSections();
			});
		}
	}

	private mapNRecordToNSections(): NursingRecord[] {
		return this.nursingRecords.map(r => {
			return {
				matIcon: (r.indication.type === "OTHER_INDICATION") ? 'assignment_late' : 'local_dining',
				content: {
					status: {
						description: 'indicacion.nursing-care.status.PENDING',
						cssClass: 'red'
					},
					description: (r.indication.type === "OTHER_INDICATION") ? loadOtherIndicationType(r, this.othersIndicatiosType) : r.indication.description,
					createdBy: "",
					timeElapsed: ""
				}
			}
		});

		function loadOtherIndicationType(otherIndication: OtherIndicationDto, othersIndicatiosType: OtherIndicationTypeDto[]): string {
			const result = othersIndicatiosType.find(i => i.id === otherIndication.otherIndicationTypeId);
			return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
		}
	}
}
