import { Component, Input } from '@angular/core';
import { DateDto, DateTimeDto } from '@api-rest/api-model';
import { ExtraInfo, Status } from '@presentation/components/indication/indication.component';

@Component({
  selector: 'app-nursing-record',
  templateUrl: './nursing-record.component.html',
  styleUrls: ['./nursing-record.component.scss']
})
export class NursingRecordComponent {

	@Input() nursingSections: NursingSections[];

	constructor() { }

}

export interface NursingSections {
  title: string;
  records: NursingRecord[];
  time?: number;
}

export interface NursingRecord {
	id: number;
	matIcon: string;
	svgIcon?: string;
	content: NursingRecordContent;
}

export interface NursingRecordContent {
	status: Status;
	description: string;
	extra_info?: ExtraInfo[];
	indicationDate: Date;
	scheduledAdministrationTime: DateTimeDto;
	administeredBy: string;
	administeredTime: string;
}
