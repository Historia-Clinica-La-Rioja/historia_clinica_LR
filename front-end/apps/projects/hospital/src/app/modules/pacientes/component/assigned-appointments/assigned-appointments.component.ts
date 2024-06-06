import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AssignedAppointmentDto, CompletePatientDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { ASSIGNED_APPOINTMENTS_SUMMARY } from '@pacientes/constants/summaries';
import { AppointmentHistoricComponent } from '@pacientes/dialogs/appointment-historic/appointment-historic.component';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { MapperService } from '@presentation/services/mapper.service';

@Component({
	selector: 'app-assigned-appointments',
	templateUrl: './assigned-appointments.component.html',
	styleUrls: ['./assigned-appointments.component.scss']
})
export class AssignedAppointmentsComponent implements OnInit {

	@Input() patientId: number;
	@Output() newAppointmentRequired = new EventEmitter();
	assignedAppointmentsSummary = ASSIGNED_APPOINTMENTS_SUMMARY;
	appointmentsList: AssignedAppointmentDto[] = [];
    patientFullName: string = '';

	constructor(
		private readonly appointmentsService: AppointmentsService,
		private readonly dialog: MatDialog, 
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
        private readonly patientNameService: PatientNameService,) {

		}

	ngOnInit(): void {
		this.getPatientFullName();
		this.appointmentsService.getAssignedAppointmentsList(this.patientId).subscribe(
			appointments => this.appointmentsList = appointments
		);
	}

	newAppointment() {
		this.newAppointmentRequired.emit();
	}

	openAppointmentHistoric(){
		this.dialog.open(AppointmentHistoricComponent, {
			autoFocus: false,
			width: '70%',
			maxHeight: 'fit-content',
			data: {
				patientName: this.patientFullName,
				patientId: this.patientId,
			}
		});
	}

	getPatientFullName() {
		this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
		.subscribe(completeData => {
			let patientBasicData: PatientBasicData = this.mapperService.toPatientBasicData(completeData);
			this.patientFullName = this.patientNameService.completeName(
				patientBasicData.firstName, 
				patientBasicData.nameSelfDetermination, 
				patientBasicData.lastName,
				patientBasicData.middleNames,
				patientBasicData.otherLastNames)
		})
	}
}
