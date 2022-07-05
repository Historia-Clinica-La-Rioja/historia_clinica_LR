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
	nursingRecords: any[] = [];
	generalNursingRecords: any[] = [];
	specificNursingRecords: any[] = [];
	@Input() internmentEpisodeId: number;

	constructor(
		private readonly internmentEpisode: InternmentEpisodeService,
	) { }

	ngOnInit(): void {
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
		);
		//lamar al BE para cargar el nursingRecords
	}

	loadActualDateAndFilter(actualDate: Date) {
		this.generalNursingRecords = [];
		this.specificNursingRecords = [];
		this.filterNursingRecords(actualDate);
	}

	private filterNursingRecords(actualDate: Date) {
		const records: any[] = this.nursingRecords.filter(r => isSameDay(dateDtoToDate(r.indication.indicationDate), actualDate));
		records.forEach(record => {
			if (isSpecific(record))
				this.specificNursingRecords.push(record);
			else
				this.generalNursingRecords.push(record);
		});
		function isSpecific(record: any) {
			return record.scheduleAdministrationTime || record.event;
		}
	}
}