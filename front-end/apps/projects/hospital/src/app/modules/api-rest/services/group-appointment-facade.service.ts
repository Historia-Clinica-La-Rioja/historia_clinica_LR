import { Injectable } from '@angular/core';
import { GroupAppointmentService } from './group-appointment.service';
import { GroupAppointmentResponseDto } from '@api-rest/api-model';
import { Subject } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class GroupAppointmentFacadeService {

	appointments$ = new Subject<GroupAppointmentResponseDto[]>();

  	constructor(private readonly groupAppointmentService: GroupAppointmentService) { }

	setAppointmentsFromDeterminatedDiaryDateTime(data: string) {
		this.groupAppointmentService.getAppointmentsFromDeterminatedDiaryDateTime(data)
			.subscribe({
				next: ((result: GroupAppointmentResponseDto[]) => this.appointments$.next(result))
			})
	}
}
