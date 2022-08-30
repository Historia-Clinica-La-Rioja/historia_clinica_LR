import { NursingRecordDto } from './../../../../../../../api-rest/api-model.d';
import { InternmentNursingRecordService } from './../../../../../../../api-rest/services/internment-nursing-record.service';
import { Component, Input, OnInit } from '@angular/core';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { NURSING_CARE } from '@historia-clinica/constants/summaries';
import { isSameDay } from 'date-fns';

@Component({
	selector: 'app-nursing-care',
	templateUrl: './nursing-care.component.html',
	styleUrls: ['./nursing-care.component.scss']
})
export class NursingCareComponent implements OnInit {

	title = NURSING_CARE;
	entryDate: Date;
	nursingRecords: NursingRecordDto[] = [];
	generalNursingRecords: NursingRecordDto[] = [];
	specificNursingRecords: NursingRecordDto[] = [];
	@Input() internmentEpisodeId: number;

	constructor(
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly internmentNursingRecordService: InternmentNursingRecordService
	) { }

	ngOnInit(): void {
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
		);
		this.internmentNursingRecordService.getInternmentNursingRecords(this.internmentEpisodeId).subscribe(nursingRecords => {
			this.nursingRecords = nursingRecords;
			this.loadActualDateAndFilter(new Date());
		});
	}

	loadActualDateAndFilter(actualDate: Date) {
		this.generalNursingRecords = [];
		this.specificNursingRecords = [];
		this.filterNursingRecords(actualDate);
	}

	private filterNursingRecords(actualDate: Date) {
		const records: any[] = this.nursingRecords.filter((r: NursingRecordDto) => isSameDay(dateDtoToDate(r.indication.indicationDate), actualDate));
		records.forEach(record => {
			if (isSpecific(record))
				this.specificNursingRecords.push(record);
			else
				this.generalNursingRecords.push(record);
		});
		function isSpecific(record: NursingRecordDto) {
			return record.scheduledAdministrationTime || record.event;
		}
	}
}