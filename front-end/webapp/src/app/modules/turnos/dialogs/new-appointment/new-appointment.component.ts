import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { VALIDATIONS, processErrors, hasError, updateControlValidator } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PatientService } from '@api-rest/services/patient.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { MatStepper, MatHorizontalStepper } from '@angular/material/stepper';
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
import { MedicalCoverageComponent, PatientMedicalCoverage } from '../../../core/dialogs/medical-coverage/medical-coverage.component';
import { newMoment } from '@core/utils/moment.utils';

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
	patientMedicalCoverages: PatientMedicalCoverage[];
	patient: ReducedPatientDto;
	public readonly hasError = hasError;
	readonly TEMPORARY_PATIENT_ID = TEMPORARY_PATIENT_ID;
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
		private readonly appointmentFacade: AppointmentsFacadeService,
		public dialog: MatDialog,
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
			patientMedicalCoverageId: [null, Validators.required],
			phoneNumber: [null, [Validators.maxLength(20)]]
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

			this.patientService.getPatientMinimal(searchRequest).subscribe(
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
				this.patient = reducedPatientDto;
				this.appointmentInfoForm.controls.phoneNumber.setValue(reducedPatientDto.personalDataDto.phoneNumber);
				this.setHealthInsuranceoptions();
			}, _ => {
				this.patientNotFound();
			});

	}

	mapToPersonIdentification(personalDataDto: BasicPersonalDataDto): PersonIdentification {
		return {
			firstName: personalDataDto.firstName,
			lastName: personalDataDto.lastName,
			identificationNumber: personalDataDto.identificationNumber
		};
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

	private setHealthInsuranceoptions() {

		//Llamar al/los mismos endpoints que se llama desde adentro del dialog
		this.patientMedicalCoverages = [
			{
				id: 1,
				medicalCoverage: {
					acronym: 'OSPEA', name: 'OBRA SOCIAL DEL PERSONAL DE DIRE', rnos: '1',
				},
				affiliateNumber: '100',
				validDate: newMoment()

			},
			{
				id: 2,
				medicalCoverage: {
					acronym: 'OSA', name: 'OBRA SOCIAL DE AERONAVEGANTES', rnos: '400800',
				},
				affiliateNumber: '400800',
				validDate: newMoment()
			},

		];
	}

	getFullHealthInsuranceText(medicalCoverage): string {
		return [medicalCoverage.acronym, medicalCoverage.name].filter(Boolean).join(' - ');
	}

	submit(): void {
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			hour: this.data.hour,
			openingHoursId: this.data.openingHoursId,
			overturn: this.data.overturnMode,
			patientId: this.patientId,
			patientMedicalCoverageId: this.appointmentInfoForm.value.patientMedicalCoverageId,
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

	showConfirmButton(): boolean {
		return this.appointmentInfoForm.valid;
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

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patient.personalDataDto.genderId,
				identificationNumber: this.patient.personalDataDto.identificationNumber,
				identificationTypeId: 1 // Tiene que ser el tipo de la persona
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				this.patientMedicalCoverages = values.patientHealthInsurances.concat(values.patientPrivateHealthInsurances);
				console.log(this.patientMedicalCoverages);
			}
		);
	}
}
