import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceCompleteDataDto, ReferencePatientDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Observable, tap } from 'rxjs';

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData$: Observable<ReferenceCompleteDataDto>;
	reportCompleteData: ReportCompleteData;

	constructor(
		private readonly referenceReportService: ReferenceReportService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.referenceCompleteData$ = this.referenceReportService.getReferenceDetail(this.data.referenceId).pipe(tap(
			referenceDetails => {
				const patient = referenceDetails.patient;
				this.reportCompleteData = {
					patient: this.mapToPatientSummary(patient)
				}
			}));
	}

	private mapToPatientSummary(patient: ReferencePatientDto): PatientSummary {
		return {
			fullName: patient.patientFullName,
			identification: {
				type: patient.identificationType,
				number: +patient.identificationNumber
			},
			id: patient.patientId,
		}
	}

}

interface ReportCompleteData {
	patient: PatientSummary;
}
