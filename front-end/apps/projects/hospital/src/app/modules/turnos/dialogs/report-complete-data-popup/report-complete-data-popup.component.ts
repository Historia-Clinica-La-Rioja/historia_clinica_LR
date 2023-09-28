import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceAppointmentDto, ReferenceCompleteDataDto, ReferencePatientDto, ReferenceDataDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { ContactDetails } from '@turnos/components/contact-details/contact-details.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Observable, tap } from 'rxjs';
import { AppointmentSummary } from '@turnos/components/appointment-summary/appointment-summary.component';
import { PENDING, getState } from '@turnos/utils/reference.utils';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData$: Observable<ReferenceCompleteDataDto>;
	reportCompleteData: ReportCompleteData;

	colapseContactDetails = false;

	constructor(
		private readonly referenceReportService: ReferenceReportService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.referenceCompleteData$ = this.referenceReportService.getReferenceDetail(this.data.referenceId).pipe(tap(
			referenceDetails => {
				this.setReportData(referenceDetails);
				this.colapseContactDetails = referenceDetails.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
			}));
	}

	private setReportData(referenceDetails: ReferenceCompleteDataDto): void {
		const patient = referenceDetails.patient;
		const pendingAppointment: AppointmentSummary = { state: PENDING };
		this.reportCompleteData = {
			patient: this.mapToPatientSummary(patient),
			contactDetails: this.mapToContactDetails(patient),
			reference: referenceDetails.reference,
			appointment: referenceDetails.appointment ? this.mapToAppointmentSummary(referenceDetails.appointment) : pendingAppointment
		}
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

	private mapToContactDetails(patient: ReferencePatientDto): ContactDetails {
		return {
			phonePrefix: patient.phonePrefix,
			phoneNumber: patient.phoneNumber,
			email: patient.email
		}
	}

	private mapToAppointmentSummary(value: ReferenceAppointmentDto): AppointmentSummary {
		return {
			state: getState(value.appointmentStateId),
			date: value.date,
			professionalFullName: value.professionalFullName,
			institution: value.institution.description
		}
	}

}

interface ReportCompleteData {
	patient: PatientSummary;
	contactDetails: ContactDetails;
	reference: ReferenceDataDto;
	appointment: AppointmentSummary;
}
