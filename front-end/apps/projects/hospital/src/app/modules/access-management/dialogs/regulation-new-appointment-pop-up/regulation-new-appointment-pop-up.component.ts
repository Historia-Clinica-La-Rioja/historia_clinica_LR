import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder, Validators, UntypedFormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import { ReferenceSummaryDto, IdentificationTypeDto, GenderDto, MedicalCoverageDto, ReducedPatientDto, EAppointmentModality, BasicPersonalDataDto, CreateAppointmentDto, AppointmentShortSummaryDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ReferenceService } from '@api-rest/services/reference.service';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { MapperService } from '@core/services/mapper.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { hasError, VALIDATIONS, updateControlValidator, processErrors } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { PersonIdentification } from '@presentation/pipes/person-identification.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SearchAppointmentCriteria } from '@turnos/components/search-appointments-in-care-network/search-appointments-in-care-network.component';
import { MODALITYS_TYPES } from '@turnos/constants/appointment';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { Observable, of, map } from 'rxjs';
import { buildFullDateFromDate, dateISOParseDate } from '@core/utils/moment.utils';

const TEMPORARY_PATIENT_ID = 3;

@Component({
	selector: 'app-regulation-new-appointment-pop-up',
	templateUrl: './regulation-new-appointment-pop-up.component.html',
	styleUrls: ['./regulation-new-appointment-pop-up.component.scss']
})
export class RegulationNewAppointmentPopUpComponent implements OnInit {

	indexStep = Steps;
	@ViewChild('stepper', { static: false }) stepper: MatStepper;
	initialIndex = this.indexStep.MODALITY;
	preselectedPatient = false;
	public formSearch: UntypedFormGroup;
	public appointmentInfoForm: UntypedFormGroup;
	public associateReferenceForm: UntypedFormGroup;
	public modalityForm: UntypedFormGroup;
	referenceList: ReferenceSummaryDto[];
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public healtInsuranceOptions: MedicalCoverageDto[] = [];
	public patientId: any;
	public editable = true;
	patientMedicalCoverages: PatientMedicalCoverage[];
	patient: ReducedPatientDto;
	public readonly hasError = hasError;
	readonly TEMPORARY_PATIENT_ID = TEMPORARY_PATIENT_ID;
	isFormSubmitted = false;
	public isSubmitButtonDisabled = false;
	VALIDATIONS = VALIDATIONS;
	lastAppointmentId = -1;
	editableStep1 = true;
	editableStepModality = true;
	readonly MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	readonly MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	readonly MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION;
	modalitySelected: EAppointmentModality = this.MODALITY_ON_SITE_ATTENTION;
	viewModalityLabel$: Observable<boolean> = of(false);
	modalitys = MODALITYS_TYPES.slice(0, 2);
	fullDate: Date

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: RegulationNewAppointmentData,
		public dialogRef: MatDialogRef<NewAppointmentComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly appointmentService: AppointmentsService,
		public dialog: MatDialog,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientNameService: PatientNameService,
		private readonly referenceService: ReferenceService,
	) {
		if (this.data.modalityAttention) {
			this.modalitySelected = this.data.modalityAttention;
			this.editableStepModality = false;
			this.initialIndex = this.indexStep.SEARCH;
			this.viewModalityLabel$ = of(true);
		}
	}

	ngOnInit(): void {
		this.modalityForm = this.formBuilder.group({
			modality: [this.modalitySelected, Validators.required],
		})

		this.formSearch = this.formBuilder.group({
			identifType: [null, Validators.required],
			identifNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			gender: [null, Validators.required],
			completed: [null, Validators.required],
			patientId: [],
		});

		this.appointmentInfoForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			phonePrefix: [null, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]],
			phoneNumber: [null, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]],
			patientEmail: [null, [Validators.email]]
		});


		this.associateReferenceForm = this.formBuilder.group({
			reference: [null, Validators.required],
			professionalEmail: [null, Validators.email]
		});

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => {
				this.identifyTypeArray = identificationTypes;
				this.formSearch.controls.identifType.setValue(IDENTIFICATION_TYPE_IDS.DNI);
			});

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
		this.appointmentInfoForm.markAllAsTouched();

		this.formSearch.controls.patientId.patchValue(this.data.patientId);
		if (this.data.patientId) {
			this.preselectedPatient = true;
			this.initialIndex = this.indexStep.INFO;
			this.patientId = this.data.patientId;
			this.isFormSubmitted = true;
			this.patientSearch(this.data.patientId);
			this.editableStep1 = false;
		}
		this.setModalityValidation(this.modalitySelected);

		this.fullDate = buildFullDateFromDate(this.data.hour,dateISOParseDate(this.data.date))

	}

	setModalityValidation(modality) {
		this.modalitySelected = modality;

		switch (this.modalitySelected) {
			case this.MODALITY_PATIENT_VIRTUAL_ATTENTION:
				this.appointmentInfoForm.setControl('patientEmail', new UntypedFormControl(null, [Validators.required, Validators.email]));
				this.appointmentInfoForm.controls.patientEmail.updateValueAndValidity();
				break;

			case this.MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION:
				this.associateReferenceForm.setControl('professionalEmail', new UntypedFormControl(null, [Validators.required, Validators.email]));
				this.associateReferenceForm.controls.professionalEmail.updateValueAndValidity();
				break;

			case this.MODALITY_ON_SITE_ATTENTION:
				this.appointmentInfoForm.setControl('patientEmail', new UntypedFormControl(null, [Validators.email]));
				this.appointmentInfoForm.controls.patientEmail.updateValueAndValidity();
				break;
		}
	}

	onStepChange(stepper: MatStepper) {
		if (stepper.selectedIndex > this.indexStep.MODALITY) {
			this.viewModalityLabel$ = of(true);
		} else {
			this.viewModalityLabel$ = of(false);
		}
	}

	search(): void {
		this.isFormSubmitted = true;
		if (this.isFormSearchValid()) {
			const formSearchValue = this.formSearch.value;
			if (formSearchValue.patientId) {
				this.patientSearch(formSearchValue.patientId);
				this.patientId = formSearchValue.patientId;
				return;
			}

			const searchRequest = {
				identificationTypeId: formSearchValue.identifType,
				identificationNumber: +formSearchValue.identifNumber.replace(REMOVE_SUBSTRING_DNI, ''),
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
				});
		}
	}

	updatePhoneValidators() {
		if (this.appointmentInfoForm.controls.phoneNumber.value || this.appointmentInfoForm.controls.phonePrefix.value) {
			updateControlValidator(this.appointmentInfoForm, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.appointmentInfoForm, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
		} else {
			updateControlValidator(this.appointmentInfoForm, 'phoneNumber', []);
			updateControlValidator(this.appointmentInfoForm, 'phonePrefix', []);
		}
	}

	private patientSearch(patientId: number) {
		this.patientService.getBasicPersonalData(patientId)
			.subscribe((reducedPatientDto: ReducedPatientDto) => {
				this.patientFound();
				this.setReferenceInformation();
				this.patient = reducedPatientDto;
				this.appointmentInfoForm.controls.phonePrefix.setValue(reducedPatientDto.personalDataDto.phonePrefix);
				this.appointmentInfoForm.controls.phoneNumber.setValue(reducedPatientDto.personalDataDto.phoneNumber);
				this.updatePhoneValidators();
				if (reducedPatientDto.personalDataDto.phoneNumber) {
					updateControlValidator(this.appointmentInfoForm, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
					updateControlValidator(this.appointmentInfoForm, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
				}
				this.setMedicalCoverages();
			}, _ => {
				this.patientNotFound();
			});

	}

	mapToPersonIdentification(personalDataDto: BasicPersonalDataDto): PersonIdentification {
		return {
			firstName: this.patientNameService.getPatientName(personalDataDto.firstName, personalDataDto.nameSelfDetermination),
			lastName: personalDataDto.lastName,
			identificationNumber: personalDataDto.identificationNumber
		};
	}

	private patientFound() {
		this.formSearch.controls.completed.setValue(true);
		this.snackBarService.showSuccess('turnos.new-appointment.messages.SUCCESS');
		if (this.initialIndex !== this.indexStep.INFO)
			this.stepper.next();
	}

	private patientNotFound() {
		this.snackBarService.showError('turnos.new-appointment.messages.ERROR');
	}

	submit(): void {
		if (this.isAppointmentFormValid()) {
			this.isSubmitButtonDisabled = true;
			this.verifyExistingAppointment().subscribe((appointmentShortSummary) => {
				appointmentShortSummary ? this.existsAppointment() : this.createAppointment();
			}, error => {
				this.dialogRef.close();
				this.isSubmitButtonDisabled = false;
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			})
		}
	}

	private existsAppointment() {
		const warnignComponent = this.dialog.open(DiscardWarningComponent,
			{
				disableClose: true,
				data: {
					title: 'turnos.new-appointment.appointment-exists.TITLE',
					contentBold: 'turnos.new-appointment.appointment-exists.ASSIGNMENT-QUESTION',
					okButtonLabel: 'turnos.new-appointment.appointment-exists.buttons.ASSIGN',
					cancelButtonLabel: 'turnos.new-appointment.appointment-exists.buttons.NOT-ASSIGN'
				},
				maxWidth: '500px'
			});
		warnignComponent.afterClosed().subscribe(confirmed =>
			confirmed ? this.createAppointment() : this.dialogRef.close(-1)
		);
	}

	private createAppointment() {
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			hour: this.data.hour,
			openingHoursId: this.data.openingHoursId,
			overturn: this.data.overturnMode,
			patientId: this.patientId,
			patientMedicalCoverageId: this.appointmentInfoForm.value.patientMedicalCoverage?.id,
			phonePrefix: this.appointmentInfoForm.value.phonePrefix,
			phoneNumber: this.appointmentInfoForm.value.phoneNumber,
			modality: this.modalitySelected,
			patientEmail: this.appointmentInfoForm.controls.patientEmail.value,
			applicantHealthcareProfessionalEmail: this.associateReferenceForm.controls.professionalEmail.value ? this.associateReferenceForm.controls.professionalEmail.value : null,
			referenceId: this.associateReferenceForm?.controls?.reference?.value?.id
		};
		this.create(newAppointment).subscribe((appointmentId: number) => {
			this.lastAppointmentId = appointmentId;
			const valueEmail = this.getEmail();
			this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
			this.dialogRef.close({ id: this.lastAppointmentId, email: valueEmail });
		}, error => {
			this.isSubmitButtonDisabled = false;
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	private getEmail(): string {
		return this.data.modalityAttention === this.MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION ? this.associateReferenceForm.controls.professionalEmail.value : this.appointmentInfoForm.controls.patientEmail.value;
	}

	disableConfirmButtonStep3(): boolean {
		return !(this.formSearch.controls.completed.value && this.isAppointmentFormValid() && this.associateReferenceForm.valid);
	}

	disablePreviuosStep(stepperParam: MatStepper) {
		if (stepperParam.selectedIndex === this.indexStep.MODALITY) {
			this.editable = false;
		}
	}

	private isFormSearchValid() {
		return (this.formSearch.controls.identifType.valid
			&& this.formSearch.controls.identifNumber.valid
			&& this.formSearch.controls.gender.valid) || this.formSearch.controls.patientId.value;
	}

	private isAppointmentFormValid() {
		this.appointmentInfoForm.markAllAsTouched();
		return this.appointmentInfoForm.valid;
	}

	back(stepper: MatStepper) {
		if (stepper.selectedIndex === this.indexStep.INFO) {
			this.formSearch.controls.completed.reset();
			this.appointmentInfoForm.reset();
		}
		this.goBack(stepper);
	}


	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

	goBack(stepper: MatStepper) {
		stepper.previous();
	}

	private verifyExistingAppointment(): Observable<AppointmentShortSummaryDto> {
		return this.appointmentService.verifyExistingAppointments(this.patientId, this.data.date, this.data.hour, this.data.institutionId);
	}

	private create(newAppointment: CreateAppointmentDto): Observable<number> {
		return this.appointmentService.create(newAppointment);
	}

	private setReferenceInformation() {
		if (this.data.referenceSummary) {
			const referenceSummary = this.data.referenceSummary;
			this.referenceList = [referenceSummary];
			this.associateReferenceForm.controls.reference.setValue(referenceSummary);
			this.associateReferenceForm.controls.reference.disable();
		}
		else this.referenceService.getReferencesSummary(this.patientId, this.data.searchAppointmentCriteria).subscribe(reference => this.referenceList = reference);
	}
}

export interface RegulationNewAppointmentData {
	date: string,
	diaryId: number,
	hour: string,
	openingHoursId: number,
	overturnMode: boolean,
	patientId?: number,
	modalityAttention: EAppointmentModality,
	searchAppointmentCriteria?: SearchAppointmentCriteria,
	referenceSummary?: ReferenceSummaryDto,
	institutionId: number,
}

enum Steps {
	MODALITY = 0,
	SEARCH = 1,
	INFO = 2,
	PROTECTED = 3,
}
