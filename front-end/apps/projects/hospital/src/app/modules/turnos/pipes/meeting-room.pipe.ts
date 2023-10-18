import { Pipe, PipeTransform } from '@angular/core';
import { CompleteDiaryDto } from '@api-rest/api-model';

@Pipe({
	name: 'meetingRoom'
})
export class MeetingRoomPipe implements PipeTransform {

	transform(agenda: CompleteDiaryDto): string {
		const doctorsOfficeDescription = agenda.doctorsOfficeDescription || '';
		const sectorDescription = agenda.sectorDescription || '';

		return doctorsOfficeDescription + (doctorsOfficeDescription && sectorDescription ? ' | ' : '') + sectorDescription;
	  }

}
