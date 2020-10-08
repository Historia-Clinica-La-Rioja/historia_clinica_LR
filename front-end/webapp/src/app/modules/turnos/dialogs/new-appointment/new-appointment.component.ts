import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { VALIDATIONS, processErrors, hasError, updateControlValidator } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PatientService } from '@api-rest/services/patient.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { MatStepper, MatHorizontalStepper } from '@angular/material/stepper';
import { RenaperService } from '@api-rest/services/renaper.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import {
	CreateAppointmentDto,
	MedicalCoverageDto,
	IdentificationTypeDto,
	GenderDto,
	BasicPersonalDataDto,
	ReducedPatientDto
} from '@api-rest/api-model';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { PersonIdentification } from '@presentation/pipes/person-identification.pipe';

const ROUTE_SEARCH = 'pacientes/search';
const TEMPORARY_PATIENT_ID = 3;

@Component({
	selector: 'app-new-appointment',
	templateUrl: './new-appointment.component.html',
	styleUrls: ['./new-appointment.component.scss']
})
export class NewAppointmentComponent implements OnInit {

	@ViewChild('stepper', { static: false }) stepper: MatStepper;

	public formSearch: FormGroup;
	public appointmentInfoForm: FormGroup;
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public healtInsuranceOptions: MedicalCoverageDto[] = [];
	public patientId: any;
	public showAddPatient = false;
	public editable = true;
	public person: PersonIdentification;
	isTemporaryPatient = false;

	public readonly hasError = hasError;
	private readonly routePrefix;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			date: string, diaryId: number, hour: string, openingHoursId: number, overturnMode: boolean
		},
		public dialogRef: MatDialogRef<NewAppointmentComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly renaperService: RenaperService,
		private readonly healthInsurance: HealthInsuranceService,
		private readonly appointmentFacade: AppointmentsFacadeService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {
		this.formSearch = this.formBuilder.group({
			identifType: [null, Validators.required],
			identifNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			gender: [null, Validators.required],
			completed: [null, Validators.required],
			patientId: [],
		});

		this.appointmentInfoForm = this.formBuilder.group({
			medicalCoverage: [null],
			prepaid: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageName)],
			affiliateNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageAffiliateNumber)]],
			phoneNumber: [null, [Validators.required, Validators.maxLength(20)]]
		});

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => { this.identifyTypeArray = identificationTypes; });

		this.personMasterDataService.getGenders().subscribe(
			genders => { this.genderOptions = genders; });


		this.formSearch.controls.patientId.valueChanges
			.subscribe(value => {
				if (value) {
					updateControlValidator(this.formSearch, 'identifType', []);
					updateControlValidator(this.formSearch, 'identifNumber', []);
					updateControlValidator(this.formSearch, 'gender', []);
					return;
				}
				updateControlValidator(this.formSearch, 'identifType', [Validators.required]);
				updateControlValidator(this.formSearch, 'identifNumber', [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]);
				updateControlValidator(this.formSearch, 'gender', [Validators.required]);
			});
	}

	search(): void {
		if (this.isFormSearchValid()) {
			const formSearchValue = this.formSearch.value;
			if (formSearchValue.patientId) {
				this.patientSearch(formSearchValue.patientId);
				this.patientId = formSearchValue.patientId;
				return;
			}

			const searchRequest = {
				identificationTypeId: formSearchValue.identifType,
				identificationNumber: formSearchValue.identifNumber,
				genderId: formSearchValue.gender,
			};

			this.patientService.quickGetPatient(searchRequest).subscribe(
				(data: number[]) => {
					if (data.length) {
						this.patientId = data[0];
						this.patientSearch(this.patientId);
					} else {
						this.patientNotFound();
					}
				}
			);
		}
	}

	private patientSearch(patientId: number) {
		this.patientService.getBasicPersonalData(patientId)
			.subscribe((reducedPatientDto: ReducedPatientDto) => {
				this.patientFound();
				this.person = mapToPersonIdentification(reducedPatientDto.personalDataDto);
				this.appointmentInfoForm.controls.phoneNumber.setValue(reducedPatientDto.personalDataDto.phoneNumber);
				this.isTemporaryPatient = reducedPatientDto.patientTypeId === TEMPORARY_PATIENT_ID;
				this.setHealthInsuranceoptions(reducedPatientDto);
			}, _ => {
				this.patientNotFound();
			});

		function mapToPersonIdentification(personalDataDto: BasicPersonalDataDto): PersonIdentification {
			return {
				firstName: personalDataDto.firstName,
				lastName: personalDataDto.lastName,
				identificationNumber: personalDataDto.identificationNumber
			};
		}
	}

	private patientFound() {
		this.formSearch.controls.completed.setValue(true);
		this.snackBarService.showSuccess('turnos.new-appointment.messages.SUCCESS');
		this.stepper.next();
	}

	private patientNotFound() {
		this.snackBarService.showError('turnos.new-appointment.messages.ERROR');
		this.showAddPatient = true;
	}

	private setHealthInsuranceoptions(reducedPatientDto: ReducedPatientDto) {

		if (reducedPatientDto.patientTypeId === TEMPORARY_PATIENT_ID) {
			this.setAllHealthInsuranceOptions();
			return;
		}

		this.renaperService.getHealthInsurance(
			{ identificationNumber: reducedPatientDto.personalDataDto.identificationNumber, genderId: reducedPatientDto.personalDataDto.genderId })
			.subscribe(healthInsuranceData => {
				if (healthInsuranceData) {
					this.healtInsuranceOptions = healthInsuranceData;
				} else {
					this.setAllHealthInsuranceOptions();
				}
			}, () => this.setAllHealthInsuranceOptions());
	}

	private setAllHealthInsuranceOptions() {
		this.healthInsurance.getAll().subscribe(allHealthInsuranceData => {
			this.healtInsuranceOptions = allHealthInsuranceData;
		});
	}

	submit(): void {
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			healthInsuranceId: this.appointmentInfoForm.controls.medicalCoverage.value,
			hour: this.data.hour,
			medicalCoverageAffiliateNumber: this.appointmentInfoForm.controls.affiliateNumber.value,
			medicalCoverageName: this.appointmentInfoForm.controls.prepaid.value,
			openingHoursId: this.data.openingHoursId,
			overturn: this.data.overturnMode,
			patientId: this.patientId,
			phoneNumber: this.appointmentInfoForm.controls.phoneNumber.value
		};

		this.appointmentFacade.addAppointment(newAppointment).subscribe(_ => {
			this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
			this.dialogRef.close(true);
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
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
		return this.appointmentInfoForm.controls.affiliateNumber.valid
			&& (this.appointmentInfoForm.controls.medicalCoverage.valid || this.appointmentInfoForm.controls.prepaid.valid)
			&& this.appointmentInfoForm.controls.phoneNumber.valid;
	}

	disablePreviuosStep(stepperParam: MatHorizontalStepper) {
		if (stepperParam.selectedIndex === 0) {
			this.editable = false;
		}
	}

	private isFormSearchValid() {
		return (this.formSearch.controls.identifType.valid
			&& this.formSearch.controls.identifNumber.valid
			&& this.formSearch.controls.gender.valid) || this.formSearch.controls.patientId.value.length > 0;
	}


	toFirstStep() {
		this.formSearch.controls.completed.reset();
		this.appointmentInfoForm.reset();
	}
}
