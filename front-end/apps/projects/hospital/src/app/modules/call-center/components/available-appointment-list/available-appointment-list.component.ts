import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { DiaryAvailableAppointmentsDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtotoString } from '@api-rest/mapper/date-dto.mapper';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { format } from 'date-fns';
import { Observable, of } from 'rxjs';
import { DateFormat } from '@core/utils/date.utils';
import { NewAppointmentForThirdPartyPopupComponent, NewAppointmentForThirdPartyPopupData } from '@call-center/dialogs/new-appointment-for-third-party-popup/new-appointment-for-third-party-popup.component';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
@Component({
	selector: 'app-available-appointment-list',
	templateUrl: './available-appointment-list.component.html',
	styleUrls: ['./available-appointment-list.component.scss'],
})
export class AvailableAppointmentListComponent {

	readonly pageSizeOptions = PAGE_SIZE_OPTIONS;
	appointmentsCurrentPage: DiaryAvailableAppointmentsDto[] = [];
	appointments: DiaryAvailableAppointmentsDto[] = [];	
	pageSize: Observable<number>;
	@Input() showResults: boolean;
	@Input() practiceId: number;
	@Input()
	set availableAppointments(availableAppointments: DiaryAvailableAppointmentsDto[]) {
		this.appointments = availableAppointments;
		this.setPageSizeAndLoadFirstPage();		
	}
	@Output() clearSearch = new EventEmitter<void>();

	constructor(
		private readonly dialog: MatDialog,
		private readonly holidayService: HolidaysService,
		private readonly datePipe: DatePipe,
	) { }

	onPageChange($event: PageEvent) {
		const startPage = $event.pageIndex * $event.pageSize;
		this.appointmentsCurrentPage = this.appointments.slice(startPage, $event.pageSize + startPage);
	}

	checkHolidayAndAssign(appointmentToAssign: DiaryAvailableAppointmentsDto) {
		const date = dateDtoToDate(appointmentToAssign.date);
		const stringDate = format(date, DateFormat.API_DATE);

		this.holidayService.getHolidays(stringDate, stringDate).subscribe(holidays => {
			if (!holidays.length) {
				this.assignAppointment(appointmentToAssign);
				return;
			}
			this.openHolidayWarning(appointmentToAssign, date);
		});
	}

	private openHolidayWarning(appointmentToAssign: DiaryAvailableAppointmentsDto, date: Date) {
		const holidayText = 'corresponde a un día feriado.';
		const holidayDate = this.datePipe.transform(date, DatePipeFormat.FULL_DATE);
		const dialogRef = this.dialog.open(DiscardWarningComponent, {
			data: {
				title: 'access-management.holiday_warning.TITLE',
				content: `${holidayDate} ${holidayText}`,
				contentBold: `access-management.holiday_warning.HOLIDAY_DISCLAIMER`,
				okButtonLabel: 'access-management.holiday_warning.OK_BUTTON',
				cancelButtonLabel: 'access-management.holiday_warning.CANCEL_BUTTON',
			},
			disableClose: true,
		});

		dialogRef.afterClosed().subscribe(assignAnotherAppointment => {
			if (!assignAnotherAppointment)
				this.assignAppointment(appointmentToAssign);
		});
	}

	private assignAppointment(appointmentToAssign: DiaryAvailableAppointmentsDto) {
		const data: NewAppointmentForThirdPartyPopupData = {
			diaryId: appointmentToAssign.diaryId,
			openingHoursId: appointmentToAssign.openingHoursId,
			specialtyId: appointmentToAssign.clinicalSpecialty?.id,
			day: format(dateDtoToDate(appointmentToAssign.date), DateFormat.API_DATE),
			hour: timeDtotoString(appointmentToAssign.hour),
			practiceId: this.practiceId
		}
		const dialogRef = this.dialog.open(NewAppointmentForThirdPartyPopupComponent, {
			width: '45%',
			disableClose: true,
			data,
		});
		dialogRef.afterClosed().subscribe(created => {
			if (created)
				this.clearSearch.emit();
		});

	}

	private setPageSizeAndLoadFirstPage() {
		this.pageSize = of(this.pageSizeOptions[0]);
		this.loadFirstPage();
	}

	private loadFirstPage() {
		this.appointmentsCurrentPage = this.appointments.slice(0, PAGE_SIZE_OPTIONS[0]);
	}
}
