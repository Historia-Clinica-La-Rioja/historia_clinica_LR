import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

const MIN_APPOINTMENTS = 8;
const MILISECOND = 1000;
const MINUTES = 60;

@Component({
	selector: 'app-calendar-event-view',
	templateUrl: './calendar-event-view.component.html',
	styleUrls: ['./calendar-event-view.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})

export class CalendarEventViewComponent {

	showInformation = false;
	calendarEventProperties: CalendarEventProperties;
	endDate: Date;

	@Input() appointmentDuration: number;
	@Input() title: string;
	@Input() startDateSlot: Date;
	@Input()
	set endDateSlot(end: Date) {
		if (end) {
			this.endDate = end;
			this.showInformation = this.calculateAmountOfAppointmentsInSlot() >= MIN_APPOINTMENTS;
		}
	};

	@Input()
	set metaCalendarEvent(meta: any) {
		this.calendarEventProperties = toCalendarEventProperties(meta);
	};

	constructor() { }

	calculateAmountOfAppointmentsInSlot(): number {
		const endMilliseconds = this.startDateSlot.getTime();
		const startMilliseconds = this.endDate.getTime();

		const amountOfMinutesInSlot = (endMilliseconds - startMilliseconds) / MILISECOND / MINUTES;

		return Math.abs(Math.round(amountOfMinutesInSlot) / this.appointmentDuration);
	}

}

export interface CalendarEventProperties {
	onSiteAttentionAllowed: boolean;
	patientVirtualAttentionAllowed: boolean;
	secondOpinionVirtualAttentionAllowed: boolean;
	overturnCount: number;
	regulationProtectedAppointmentsAllowed: boolean;
	protectedAppointmentsAllowed: boolean;
	availableForBooking: boolean;
	medicalAttentionType: number;
	anyDetails: boolean;
}

function toCalendarEventProperties(calendarEvenMeta: any): CalendarEventProperties {
	return {
		onSiteAttentionAllowed: !!calendarEvenMeta?.onSiteAttentionAllowed,
		patientVirtualAttentionAllowed: !!calendarEvenMeta?.patientVirtualAttentionAllowed,
		secondOpinionVirtualAttentionAllowed: !!calendarEvenMeta?.secondOpinionVirtualAttentionAllowed,
		overturnCount: calendarEvenMeta?.overturnCount,
		regulationProtectedAppointmentsAllowed: calendarEvenMeta?.regulationProtectedAppointmentsAllowed,
		protectedAppointmentsAllowed: calendarEvenMeta?.protectedAppointmentsAllowed,
		availableForBooking: calendarEvenMeta?.availableForBooking,
		medicalAttentionType: calendarEvenMeta?.medicalAttentionType,
		anyDetails: calendarEvenMeta?.protectedAppointmentsAllowed || calendarEvenMeta?.availableForBooking || calendarEvenMeta?.regulationProtectedAppointmentsAllowed
	}
}