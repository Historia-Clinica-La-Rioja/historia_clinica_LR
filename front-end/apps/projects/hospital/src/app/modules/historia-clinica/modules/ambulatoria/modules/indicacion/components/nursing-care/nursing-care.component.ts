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
	actualDate: Date;
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
		this.actualDate = actualDate;
		this.generalNursingRecords = [];
		this.specificNursingRecords = [];
		this.filterNursingRecords();
	}

	filterNursingRecords() {
		const records: any[] = this.nursingRecords.filter(r => isSameDay(dateDtoToDate(r.indication.indicationDate), this.actualDate));
		records.forEach(record => {
			if (record.scheduleAdministrationTime || record.event)
				this.specificNursingRecords.push(record);
			else
				this.generalNursingRecords.push(record);
		});
	}

}