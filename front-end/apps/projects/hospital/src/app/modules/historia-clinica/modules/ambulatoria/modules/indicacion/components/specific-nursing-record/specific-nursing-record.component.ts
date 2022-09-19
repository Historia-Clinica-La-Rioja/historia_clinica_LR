import { dateDtoToDate, dateTimeDtotoLocalDate } from './../../../../../../../api-rest/mapper/date-dto.mapper';
import { NursingRecord, NursingSections } from './../nursing-record/nursing-record.component';
import { ExtraInfo } from './../../../../../../../presentation/components/indication/indication.component';
import { Component, Input } from '@angular/core';
import { NursingRecordDto } from '@api-rest/api-model';
import { ENursingRecordStatus } from '@api-rest/api-model';
import { EIndicationType } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { Content } from '@presentation/components/indication/indication.component';
import { getOtherIndicationType, loadExtraInfoParenteralPlan, loadExtraInfoPharmaco } from '../../constants/load-information';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { sortBy } from '@core/utils/array.utils';
import { IndicationMatIcon, IndicationSvgIcon, NursingRecordStatus, NursingRecordStatusScss } from '../../constants/internment-indications';

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
		return {
			id: record.id,
			matIcon: IndicationMatIcon[record.indication.type],
			svgIcon: IndicationSvgIcon[record.indication.type],
			content: {
				status: {
					description: NursingRecordStatus[record.status],
					cssClass: NursingRecordStatusScss[record.status],
					type: record.status
				},
				description: (record.indication.type === EIndicationType.OTHER_INDICATION) ? getOtherIndicationType(record.indication, otherIndicationTypes) : record.indication.snomed.pt,
				extra_info: this.loadExtraInfo(record),
				indicationDate: dateDtoToDate(record.indication.indicationDate),
				scheduledAdministrationTime: record.scheduledAdministrationTime,
				administeredBy: record.updatedBy,
				administeredTime: (record.administrationTime) ? dateTimeDtotoLocalDate(record.administrationTime) : null,
				reason: ENursingRecordStatus.REJECTED === record.status ? record.updateReason : null
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