import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PatientAppointmentHistoryDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';

const PAGE_SIZE_OPTIONS = [5];
const INITIAL_PAGE = 0;

@Component({
    selector: 'app-appointment-historic',
    templateUrl: './appointment-historic.component.html',
    styleUrls: ['./appointment-historic.component.scss']
})
export class AppointmentHistoricComponent implements OnInit {

    appointmentHistoric: PatientAppointmentHistoryDto[] = [];
    pageSizeOptions = PAGE_SIZE_OPTIONS;
    totalElements: number;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data,
        private readonly appointmentService: AppointmentsService,
    ) { }

    ngOnInit(): void {
        this.fetchData(INITIAL_PAGE);
    }

    onPageChange($event: any) {
        this.fetchData($event.pageIndex);
	}

	private fetchData(pageIndex: number): void {
		this.appointmentService.getAppointmentHistoric(pageIndex, this.data.patientId)
            .subscribe((documents: any) => {
				if (!this.totalElements)
					this.totalElements = documents.totalElements;
                this.appointmentHistoric = documents.content;
            });
	}
}
