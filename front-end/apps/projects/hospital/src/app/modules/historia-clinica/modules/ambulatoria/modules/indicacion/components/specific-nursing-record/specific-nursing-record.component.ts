import { NursingRecord } from './../nursing-record/nursing-record.component';
import { ExtraInfo } from './../../../../../../../presentation/components/indication/indication.component';
import { Component, Input, SimpleChanges, OnChanges, OnInit } from '@angular/core';
import { OtherIndicationDto, TimeDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { Content } from '@presentation/components/indication/indication.component';
import { loadExtraInfoParenteralPlan, loadExtraInfoPharmaco } from '../../constants/load-information';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { OTHER_INDICATION_ID } from '../../constants/internment-indications';
import { sortBy } from '@core/utils/array.utils';

@Component({
	selector: 'app-specific-nursing-record',
	templateUrl: './specific-nursing-record.component.html',
	styleUrls: ['./specific-nursing-record.component.scss']
})
export class SpecificNursingRecordComponent implements OnInit, OnChanges {

	nursingSections: NursingSections[] = [];
	vias: any[] = [];
	records: Content[];
	othersIndicatiosType: OtherIndicationTypeDto[];
	@Input() nursingRecords: any[];

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }

	ngOnInit() {
		this.internacionMasterdataService.getVias().subscribe(v => this.vias = v);
		this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => this.othersIndicatiosType = i);
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.nursingRecords.currentValue) {
			this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
				this.othersIndicatiosType = i;
				this.filterSections();
			});
		}
	}

	filterSections() {
		const eventSections: NursingSections[] = [];
		const scheduleSection: NursingSections[] = [];
		this.nursingRecords.forEach(record => {
			if (record.event) {
				const title = `Ante ${record.event}`;
				eventSections.push({ title, records: [this.mapNRecordToNRecord(record)] });
			}
			if (record.scheduleAdministrationTime) {
				const section = scheduleSection.find(r => r.time === record.scheduleAdministrationTime.time.hours);
				if (section)
					section.records.push(this.mapNRecordToNRecord(record));
				else {
					const administrationTime = (record.scheduleAdministrationTime.time.hours > 9) ? record.scheduleAdministrationTime.time.hours : ` 0${record.scheduleAdministrationTime.time.hours}`;
					const title = `${administrationTime} hs`;
					scheduleSection.push({ title, records: [this.mapNRecordToNRecord(record)], time: record.scheduleAdministrationTime.time.hours });
				}
			}
		});
		const sortByTime = sortBy('time');
		this.nursingSections = [...eventSections, ...sortByTime(scheduleSection)];
	}

	private mapNRecordToNRecord(record: any): NursingRecord {
		return {
			matIcon: (record.indication.type === "OTHER_INDICATION") ? 'assignment_late' : null,
			svgIcon: (record.indication.type === "PHARMACO") ? 'pharmaco' : (record.indication.type === "PARENTERAL_PLAN") ? 'parenteral_plans' : null,
			content: {
				status: {
					description: 'indicacion.nursing-care.status.PENDING',
					cssClass: 'red'
				},
				description: (record.indication.type === "OTHER_INDICATION") ? this.loadOtherIndicationType(record.indication, this.othersIndicatiosType) : record.indication.snomed.pt,
				extra_info: this.loadExtraInfo(record),
				createdBy: "",
				timeElapsed: "",
			}
		}
	}

	private loadOtherIndicationType(otherIndication: OtherIndicationDto, othersIndicatiosType: OtherIndicationTypeDto[]): string {
		const result = othersIndicatiosType.find(i => i.id === otherIndication.otherIndicationTypeId);
		return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
	}

	private loadExtraInfo(record: any): ExtraInfo[] {
		if (record.indication.type === "PARENTERAL_PLAN")
			return loadExtraInfoParenteralPlan(record.indication, this.vias);
		if (record.indication.type === "PHARMACO")
			return loadExtraInfoPharmaco(record.indication, false)
	}
}

export interface NursingSections {
	title: string;
	records: NursingRecord[];
	time?: TimeDto;
}
