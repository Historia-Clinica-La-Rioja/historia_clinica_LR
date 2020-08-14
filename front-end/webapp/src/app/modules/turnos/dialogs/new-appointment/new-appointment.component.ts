import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { VALIDATIONS } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PatientService } from '@api-rest/services/patient.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { MatStepper } from '@angular/material/stepper';
import { RenaperService } from '@api-rest/services/renaper.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { AppointmentsService } from './../../../api-rest/services/appointments.service';
import { CreateAppointmentDto, MedicalCoverageDto, IdentificationTypeDto, GenderDto, HealthInsurancePatientDataDto } from '@api-rest/api-model';

const ROUTE_SEARCH = 'pacientes/search';

@Component({
  selector: 'app-new-appointment',
  templateUrl: './new-appointment.component.html',
  styleUrls: ['./new-appointment.component.scss']
})
export class NewAppointmentComponent implements OnInit {

	@ViewChild('stepper', {static: false}) stepper: MatStepper;

	public formSearch: FormGroup;
	public formMedicalCoverage: FormGroup;
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public healtInsuranceOptions: MedicalCoverageDto[];
	public patientId: any;
	public showAddPatient = false;
	public patientAppointmentDto: HealthInsurancePatientDataDto;

	private readonly routePrefix;

  	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {date: string, diaryId: number, hour: string, openingHoursId: number},
		public dialogRef: MatDialogRef<NewAppointmentComponent>,
		private readonly formBuilder: FormBuilder,
		private personMasterDataService: PersonMasterDataService,
		private patientService: PatientService,
		private snackBarService: SnackBarService,
		private router: Router,
		private contextService: ContextService,
		private renaperService: RenaperService,
		private healthInsurance: HealthInsuranceService,
		private appointmentsService: AppointmentsService,
  	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

  	ngOnInit(): void {
		this.formSearch = this.formBuilder.group({
			identifType: [null, Validators.required],
			identifNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			gender: [null, Validators.required],
			completed: [null, Validators.required]
		});

		this.formMedicalCoverage = this.formBuilder.group({
			medicalCoverage: [null],
			prepaid: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageName)],
			affiliateNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageAffiliateNumber)]]
		});

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => { this.identifyTypeArray = identificationTypes; });

		this.personMasterDataService.getGenders().subscribe(
			genders => { this.genderOptions = genders; });
  	}

	search(): void {
		if (this.isFormSearchValid()) {
			const searchRequest = {
				identificationTypeId: this.formSearch.controls.identifType.value,
				identificationNumber: this.formSearch.controls.identifNumber.value,
				genderId: this.formSearch.controls.gender.value,
			};

			this.patientService.quickGetPatient(searchRequest).subscribe(
				data => {
					if (data.length) {
						this.patientId = data[0];
						this.formSearch.controls.completed.setValue(true);
						this.patientService.getAppointmentPatientData(this.patientId).subscribe(appointmentPatientData => {
							this.patientAppointmentDto = appointmentPatientData;
						});
						this.renaperService.getHealthInsurance({identificationNumber: this.formSearch.controls.identifNumber.value, genderId: this.formSearch.controls.gender.value}).subscribe(healthInsuranceData => {
							if (healthInsuranceData) {
								this.healtInsuranceOptions = healthInsuranceData;
							} else {
								this.healthInsurance.getAll().subscribe(allHealthInsuranceData => {
									this.healtInsuranceOptions = allHealthInsuranceData;
								});
							}
						});
						this.snackBarService.showSuccess('turnos.new-appointment.messages.SUCCESS');
						this.stepper.next();
					} else {
						this.snackBarService.showError('turnos.new-appointment.messages.ERROR');
						this.showAddPatient = true;
					}
				}
			);
		}
	}

	submit(): void {
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			healthInsuranceId: this.formMedicalCoverage.controls.medicalCoverage.value,
			hour: this.data.hour,
			medicalCoverageAffiliateNumber: this.formMedicalCoverage.controls.affiliateNumber.value,
			medicalCoverageName: this.formMedicalCoverage.controls.prepaid.value,
			openingHoursId: this.data.openingHoursId,
			overturn: false,
			patientId: this.patientId
		};

		this.appointmentsService.create(newAppointment).subscribe(data => {
			this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
			this.dialogRef.close();
		}, error => {
			this.snackBarService.showError('turnos.new-appointment.messages.APPOINTMENT_ERROR');
		});
	}

	goCreatePatient() {
		this.router.navigate([this.routePrefix + ROUTE_SEARCH],
		{
			queryParams: {
				identificationTypeId: this.formSearch.controls.identifType.value,
				identificationNumber: this.formSearch.controls.identifNumber.value,
				genderId: this.formSearch.controls.gender.value
			}
		});
	}

	showConfirmButton() {
		return this.formMedicalCoverage.controls.affiliateNumber.valid && (this.formMedicalCoverage.controls.medicalCoverage.valid || this.formMedicalCoverage.controls.prepaid.valid);
	}

	private isFormSearchValid() {
		return this.formSearch.controls.identifType.valid && this.formSearch.controls.identifNumber.valid && this.formSearch.controls.gender.valid;
	}


}
