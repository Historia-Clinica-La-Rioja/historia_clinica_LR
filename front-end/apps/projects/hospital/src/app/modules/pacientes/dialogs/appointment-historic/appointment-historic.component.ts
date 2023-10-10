import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PatientAppointmentHistoryDto } from '@api-rest/api-model';
import { AppointmentHistoricService } from '@api-rest/services/appointment-historic.service';

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
        private readonly appointmentHistoricService: AppointmentHistoricService,
    ) { }

    ngOnInit(): void {
        this.fetchData(INITIAL_PAGE);
    }

    onPageChange($event: any) {
        this.fetchData($event.pageIndex);
	}

	private fetchData(pageIndex: number): void {
		this.appointmentHistoricService.getAppointmentHistoric(pageIndex, this.data.patientId)
            .subscribe((documents: PaginatorDocumentData<PatientAppointmentHistoryDto[]>) => {
				if (!this.totalElements)
					this.totalElements = documents.totalElements;
                this.appointmentHistoric = documents.content;
            });
	}
}

export interface PaginatorDocumentData<T> {
    totalElements: number,
    content: T,
}