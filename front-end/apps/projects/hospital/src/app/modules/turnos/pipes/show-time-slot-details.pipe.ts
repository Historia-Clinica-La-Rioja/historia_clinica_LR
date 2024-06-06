import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CalendarEventProperties } from '@turnos/components/calendar-event-view/calendar-event-view.component';

@Pipe({
	name: 'showTimeSlotDetails'
})
export class ShowTimeSlotDetailsPipe implements PipeTransform {

	constructor(
		private readonly translateService: TranslateService
	) { }

	transform(details: CalendarEventProperties): string {
		let timeSlotDetails: string[] = [];

		const noDetails = !details?.protectedAppointmentsAllowed && !details?.availableForBooking && !details?.regulationProtectedAppointmentsAllowed;

		if (details?.protectedAppointmentsAllowed)
			timeSlotDetails.push(this.translateService.instant('turnos.search-appointments-in-care-network.CARELINE'));

		if (details?.availableForBooking)
			timeSlotDetails.push(this.translateService.instant('turnos.agenda-setup.dialog.ONLINE_APPOINTMENTS'));

		if (details?.regulationProtectedAppointmentsAllowed)
			timeSlotDetails.push(this.translateService.instant('turnos.appointment-event.PROTECTED_APPOINTMENT_REGULATION'));

		if (noDetails)
			timeSlotDetails.push(this.translateService.instant('turnos.appointment-event.NO_PROTECTED_APPOINTMENT'));

		return timeSlotDetails.join(', ');
	}

}
