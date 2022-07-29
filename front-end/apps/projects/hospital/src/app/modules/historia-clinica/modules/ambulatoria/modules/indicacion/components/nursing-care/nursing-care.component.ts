import { NursingRecordDto } from './../../../../../../../api-rest/api-model.d';
import { Component, Input, OnInit } from '@angular/core';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { NURSING_CARE } from '@historia-clinica/constants/summaries';
import { isSameDay } from 'date-fns';
import { NursingRecordFacadeService } from '../../services/nursing-record-facade.service';

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
	actualDate = new Date();
	@Input() internmentEpisodeId: number;

	constructor(
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly nursingRecordFacade: NursingRecordFacadeService
	) { }

	ngOnInit(): void {
		this.nursingRecordFacade.setInformation(this.internmentEpisodeId);
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
		);
		this.nursingRecordFacade.nursingRecords$.subscribe(nr => {
			this.nursingRecords = nr;
			this.loadActualDateAndFilter(this.actualDate);
		});
	}

	loadActualDateAndFilter(actualDate: Date) {
		this.generalNursingRecords = [];
		this.specificNursingRecords = [];
		this.actualDate = actualDate;
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