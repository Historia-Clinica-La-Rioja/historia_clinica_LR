import { Component, Input } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { DiaryAvailableAppointmentsDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';

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

	constructor() {	}

	onPageChange($event: PageEvent) {
		const startPage = $event.pageIndex * $event.pageSize;
		this.appointmentsCurrentPage = this.appointments.slice(startPage, $event.pageSize + startPage);
	}

	private setPageSizeAndLoadFirstPage() {
		this.pageSize = of(this.pageSizeOptions[0]);
		this.loadFirstPage();
	}

	private loadFirstPage() {
		this.appointmentsCurrentPage = this.appointments.slice(0, PAGE_SIZE_OPTIONS[0]);
	}
}
