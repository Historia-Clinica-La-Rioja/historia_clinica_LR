import { dateTimeDtotoLocalDate } from './../../../../../../../api-rest/mapper/date-dto.mapper';
import { NursingRecord, NursingSections } from './../nursing-record/nursing-record.component';
import { ExtraInfo } from './../../../../../../../presentation/components/indication/indication.component';
import { Component, Input } from '@angular/core';
import { NursingRecordDto } from '@api-rest/api-model';
import { EIndicationType } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { Content } from '@presentation/components/indication/indication.component';
import { getOtherIndicationType, loadExtraInfoParenteralPlan, loadExtraInfoPharmaco } from '../../constants/load-information';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { sortBy } from '@core/utils/array.utils';

@Component({
	selector: 'app-specific-nursing-record',
	templateUrl: './specific-nursing-record.component.html',
	styleUrls: ['./specific-nursing-record.component.scss']
})
export class SpecificNursingRecordComponent {

	nursingSections: NursingSections[] = [];
	vias: any[] = [];
	records: Content[];
	@Input()
	set nursingRecordsDto(nursingRecordsDto: NursingRecordDto[]) {
		this.internacionMasterdataService.getVias().subscribe(v => {
			this.vias = v;
			this.internacionMasterdataService.getOtherIndicationTypes().subscribe(otherIndicationTypes => {
				this.filterSections(nursingRecordsDto, otherIndicationTypes);
			});
		});
		
	}

	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,
	) { }

	filterSections(nursingRecordsDto: any[], otherIndicationTypes: OtherIndicationTypeDto[]) {
		const eventSections: NursingSections[] = [];
		const scheduleSections: NursingSections[] = [];
		nursingRecordsDto.forEach(record => {
			const nursingRecord = this.toNursingRecord(record, otherIndicationTypes);
			if (record.event) {
				const title = `Ante ${record.event}`;
				eventSections.push({ title, records: [nursingRecord] });
			}
			else {
				const scheduleHours = dateTimeDtotoLocalDate(record.scheduledAdministrationTime).getHours();
				let section = scheduleSections.find(r => r.time === scheduleHours);
				if (!section) {
					section = this.createSection(scheduleHours);
					scheduleSections.push(section);
				}
				section.records.push(nursingRecord);
			}
		});
		const sortByTime = sortBy('time');
		this.nursingSections = [...eventSections, ...sortByTime(scheduleSections)];
	}

	private createSection(scheduleHour: number): NursingSections {
		const administrationTime = (scheduleHour > 9) ? scheduleHour : `0${scheduleHour}`;
		const title = `${administrationTime} hs`;
		return { title, records: [], time: scheduleHour }
	}

	private toNursingRecord(record: any, otherIndicationTypes: OtherIndicationTypeDto[]): NursingRecord {
		let svgIcon: string;
		let matIcon: string;
		switch (record.indication.type) {
			case EIndicationType.PHARMACO: {
				svgIcon = 'pharmaco';
				break;
			}
			case EIndicationType.PARENTERAL_PLAN: {
				svgIcon = 'parenteral_plans';
				break;
			}
			case EIndicationType.OTHER_INDICATION: {
				matIcon = 'assignment_late';
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
				description: (record.indication.type === EIndicationType.OTHER_INDICATION) ? getOtherIndicationType(record.indication, otherIndicationTypes) : record.indication.snomed.pt,
				extra_info: this.loadExtraInfo(record),
				createdBy: "",
				timeElapsed: "",
			}
		}
	}

	private loadExtraInfo(record: any): ExtraInfo[] {
		if (record.indication.type === EIndicationType.PARENTERAL_PLAN)
			return loadExtraInfoParenteralPlan(record.indication, this.vias);
		if (record.indication.type === EIndicationType.PHARMACO)
			return loadExtraInfoPharmaco(record.indication, false, this.vias)
	}
}