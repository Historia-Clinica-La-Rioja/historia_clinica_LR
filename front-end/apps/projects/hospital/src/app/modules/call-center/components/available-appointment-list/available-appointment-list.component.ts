import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { DiaryAvailableAppointmentsDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { format } from 'date-fns';
import { Observable, of } from 'rxjs';
import { DateFormat } from '@core/utils/date.utils';

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
	@Input()
	set availableAppointments(availableAppointments: DiaryAvailableAppointmentsDto[]) {
		this.appointments = availableAppointments;
		this.setPageSizeAndLoadFirstPage();
	}

	constructor(
		private readonly dialog: MatDialog,
		private readonly holidayService: HolidaysService,
		private readonly datePipe: DatePipe,
	) { }

	onPageChange($event: PageEvent) {
		const startPage = $event.pageIndex * $event.pageSize;
		this.appointmentsCurrentPage = this.appointments.slice(startPage, $event.pageSize + startPage);
	}

	checkHolidayAndAssign(indexOfAppointment: number) {

		const date = dateDtoToDate(this.appointments[indexOfAppointment].date);
		const stringDate = format(date, DateFormat.API_DATE);

		this.holidayService.getHolidays(stringDate, stringDate).subscribe(holidays => {
			if (!holidays.length) {
				this.assignAppointment();
				return;
			}
			this.openHolidayWarning(date);
		});
	}

	private openHolidayWarning(date: Date) {
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
				this.assignAppointment();
		});
	}

	private assignAppointment() {

	}

	private setPageSizeAndLoadFirstPage() {
		this.pageSize = of(this.pageSizeOptions[0]);
		this.loadFirstPage();
	}

	private loadFirstPage() {
		this.appointmentsCurrentPage = this.appointments.slice(0, PAGE_SIZE_OPTIONS[0]);
	}
}
