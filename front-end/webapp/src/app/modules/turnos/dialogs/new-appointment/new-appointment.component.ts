import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { VALIDATIONS, processErrors, hasError } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PatientService } from '@api-rest/services/patient.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { MatStepper, MatHorizontalStepper } from '@angular/material/stepper';
import { RenaperService } from '@api-rest/services/renaper.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { CreateAppointmentDto, MedicalCoverageDto, IdentificationTypeDto, GenderDto, BasicPersonalDataDto} from '@api-rest/api-model';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';

const ROUTE_SEARCH = 'pacientes/search';

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
	public person: BasicPersonalDataDto;
	public overturnMode = false;

	public readonly hasError = hasError;
	private readonly routePrefix;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			date: string, diaryId: number, hour: string, openingHoursId: number, overturnMode: boolean},
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
			completed: [null, Validators.required]
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
	}

	search(): void {
		if (this.isFormSearchValid()) {
			const searchRequest = {
				identificationTypeId: this.formSearch.controls.identifType.value,
				identificationNumber: this.formSearch.controls.identifNumber.value,
				genderId: this.formSearch.controls.gender.value,
			};

			this.patientService.quickGetPatient(searchRequest).subscribe(
				(data: number[]) => {
					if (data.length) {
						this.patientId = data[0];
						this.formSearch.controls.completed.setValue(true);
						this.patientService.getBasicPersonalData(this.patientId).subscribe((basicPersonalData: BasicPersonalDataDto) => {
							this.person = basicPersonalData;
							this.appointmentInfoForm.controls.phoneNumber.setValue(basicPersonalData.phoneNumber);
						});
						this.renaperService.getHealthInsurance(
							{ identificationNumber: this.formSearch.controls.identifNumber.value, genderId: this.formSearch.controls.gender.value })
							.subscribe(healthInsuranceData => {
								if (healthInsuranceData) {
									this.healtInsuranceOptions = healthInsuranceData;
								} else {
									this.setAllHealthInsuranceOptions();
								}
							}, () => this.setAllHealthInsuranceOptions());
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

		this.appointmentFacade.addAppointment(newAppointment).subscribe(data => {
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
		return this.formSearch.controls.identifType.valid
			&& this.formSearch.controls.identifNumber.valid
			&& this.formSearch.controls.gender.valid;
	}


}
