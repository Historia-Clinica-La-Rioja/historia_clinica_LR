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
	ReducedPatientDto,
	PatientMedicalCoverageDto,
	DiaryAvailableProtectedAppointmentsDto,
	ReferenceSummaryDto,
	AppointmentShortSummaryDto,
	DiagnosticReportInfoDto,
} from '@api-rest/api-model';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { PersonIdentification } from '@presentation/pipes/person-identification.pipe';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { map } from 'rxjs/operators';
import { MapperService } from '@core/services/mapper.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { dateDtoToDate, timeDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { DatePipeFormat } from "@core/utils/date.utils";
import { DatePipe, TitleCasePipe } from "@angular/common";
import { DiscardWarningComponent } from "@presentation/dialogs/discard-warning/discard-warning.component";
import { ReferenceService } from '@api-rest/services/reference.service';
import { ReferenceAppointmentService } from '@turnos/services/reference-appointment.service';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { EquipmentAppointmentsFacadeService } from '@turnos/services/equipment-appointments-facade.service';
import { Observable } from 'rxjs';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';

const ROUTE_SEARCH = 'pacientes/search';
const TEMPORARY_PATIENT_ID = 3;
const MEDICAL_ORDER_PENDING_STATUS = '1';
const MEDICAL_ORDER_CATEGORY_ID = '363679005'

@Component({
	selector: 'app-new-appointment',
	templateUrl: './new-appointment.component.html',
	styleUrls: ['./new-appointment.component.scss'],
	providers: [EquipmentAppointmentsFacadeService]
})
export class NewAppointmentComponent implements OnInit {

	@ViewChild('stepper', { static: false }) stepper: MatStepper;
	initialIndex = 0;
	preselectedPatient = false;
	public formSearch: FormGroup;
	public appointmentInfoForm: FormGroup;
	public associateReferenceForm: FormGroup;
	referenceList: ReferenceSummaryDto[] = [];
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public healtInsuranceOptions: MedicalCoverageDto[] = [];
	public patientId: any;
	public showAddPatient = false;
	public editable = true;
	patientMedicalCoverages: PatientMedicalCoverage[];
	patientMedicalOrders$: Observable<DiagnosticReportInfoDto[]>
	patient: ReducedPatientDto;
	public readonly hasError = hasError;
	readonly TEMPORARY_PATIENT_ID = TEMPORARY_PATIENT_ID;
	private readonly routePrefix;
	isFormSubmitted = false;
	public isSubmitButtonDisabled = false;
	VALIDATIONS = VALIDATIONS;
	referenceDateViewList: string[];
	lastAppointmentId = -1;
	readonly dateFormats = DatePipeFormat;
	patientMedicalOrderTooltipDescription = '';
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			date: string, diaryId: number, hour: string, openingHoursId: number, overturnMode: boolean, patientId?: number,
			protectedAppointment?: DiaryAvailableProtectedAppointmentsDto, careLineId?: number, isEquipmentAppointment?: boolean,
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
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientNameService: PatientNameService,
		private readonly referenceService: ReferenceService,
		private readonly referenceAppointmentService: ReferenceAppointmentService,
		private readonly datePipe: DatePipe,
		private readonly titleCasePipe: TitleCasePipe,
		private readonly equipmentAppointmentFacade: EquipmentAppointmentsFacadeService,
		private prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService
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
			patientMedicalCoverage: [null],
			phonePrefix: [null, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]],
			phoneNumber: [null, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]],
			appointmentMedicalOrder: [null, Validators.required]
		});

		this.associateReferenceForm = this.formBuilder.group({
			reference: [null, Validators.required]
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
			this.initialIndex = 1;
			this.patientId = this.data.patientId;
			this.isFormSubmitted = true;
			this.patientSearch(this.data.patientId);
		}

	}

	search(): void {
		this.clearQueryParams();
		this.isFormSubmitted = true;
		if (this.isFormSearchValid()) {
			const formSearchValue = this.formSearch.value;
			if (formSearchValue.patientId) {
				this.patientSearch(formSearchValue.patientId);
				this.patientId = formSearchValue.patientId;
				this.getPatientMedicalOrders();
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
						this.getPatientMedicalOrders();
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

	generateTooltipOnMedicalOrderChange() {
		this.translateService.get('image-network.appointments.ORDER').subscribe(translatedText => 
			this.patientMedicalOrderTooltipDescription = 
				`${translatedText} # 
				${this.appointmentInfoForm.controls.appointmentMedicalOrder.value?.serviceRequestId} - 
				${this.titleCasePipe.transform(this.appointmentInfoForm.controls.appointmentMedicalOrder.value?.snomed?.pt)}`
		);
	}

	private patientSearch(patientId: number) {
		this.patientService.getBasicPersonalData(patientId)
			.subscribe((reducedPatientDto: ReducedPatientDto) => {
				this.patientFound();
				if (this.data?.protectedAppointment) {
					this.referenceService.getReferencesSummary(patientId, this.data.protectedAppointment.clinicalSpecialty.id, this.data.careLineId).subscribe(
						references => {
							this.referenceList = references ? references : [];
							this.createReferenceDateViewList();
						}
					);
				}
				this.patient = reducedPatientDto;
				this.appointmentInfoForm.controls.phonePrefix.setValue(reducedPatientDto.personalDataDto.phonePrefix);
				this.appointmentInfoForm.controls.phoneNumber.setValue(reducedPatientDto.personalDataDto.phoneNumber);
				for (let control in this.appointmentInfoForm.controls) {
					this.appointmentInfoForm.controls[control].setErrors(null);
				}
				if (reducedPatientDto.personalDataDto.phoneNumber) {
					updateControlValidator(this.appointmentInfoForm, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
					updateControlValidator(this.appointmentInfoForm, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
				}
				this.setMedicalCoverages();
			}, _ => {
				this.patientNotFound();
			});

	}

	private createReferenceDateViewList(): void {
		let resultList = [];
		this.referenceList.forEach(
			(reference) => {
				resultList.push(this.datePipe.transform(dateDtoToDate(reference.date), DatePipeFormat.SHORT_DATE));
			}
		);
		this.referenceDateViewList = resultList;
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
		if (this.initialIndex !== 1)
			this.stepper.next();
	}

	private patientNotFound() {
		this.snackBarService.showError('turnos.new-appointment.messages.ERROR');
		this.showAddPatient = this.data.protectedAppointment ? false : true;
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const condition = (patientMedicalCoverage.condition) ? patientMedicalCoverage.condition.toLowerCase() : null;
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber, condition].filter(Boolean).join(' / ');
	}

	submit(itComesFromStep3?: boolean): void {
		updateControlValidator(this.appointmentInfoForm, 'appointmentMedicalOrder', [Validators.required]);
		if (this.isAppointmentFormValid()) {
			this.isSubmitButtonDisabled = true;
			this.verifyExistingAppointment().subscribe((appointmentShortSummary: AppointmentShortSummaryDto) => {
				if (appointmentShortSummary) {
					const date = this.datePipe.transform(dateDtoToDate(appointmentShortSummary.date), DatePipeFormat.SHORT_DATE)
					const hour = this.datePipe.transform(timeDtoToDate(appointmentShortSummary.hour), DatePipeFormat.SHORT_TIME)
					const content = `El paciente ya tiene un turno el ${date} a las ${hour} hs para ${appointmentShortSummary.doctorFullName} en ${appointmentShortSummary.institution}`

					const warnignComponent = this.dialog.open(DiscardWarningComponent,
						{
							disableClose: true,
							data: {
								title: 'turnos.new-appointment.appointment-exists.TITLE',
								content: content,
								contentBold: 'turnos.new-appointment.appointment-exists.ASSIGNMENT-QUESTION',
								okButtonLabel: 'turnos.new-appointment.appointment-exists.buttons.ASSIGN',
								cancelButtonLabel: 'turnos.new-appointment.appointment-exists.buttons.NOT-ASSIGN'
							},
							maxWidth: '500px'
						});
					warnignComponent.afterClosed().subscribe(confirmed => {
						if (confirmed) {
							this.createAppointment(itComesFromStep3);
						} else {
							this.dialogRef.close(-1);
						}
					});
				} else {
					this.createAppointment(itComesFromStep3);
				}
			}, error => {
				this.dialogRef.close();
				this.isSubmitButtonDisabled = false;
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			})
		}
	}

	private createAppointment(itComesFromStep3?: boolean) {
		this.clearQueryParams();
		const phonePrefix = this.setPhonePrefix(itComesFromStep3);
		const phoneNumber = this.setPhoneNumber(itComesFromStep3);
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			hour: this.data.hour,
			openingHoursId: this.data.openingHoursId,
			overturn: this.data.overturnMode,
			patientId: this.patientId,
			patientMedicalCoverageId: this.appointmentInfoForm.value.patientMedicalCoverage?.id,
			phonePrefix,
			phoneNumber
		};
		this.addAppointment(newAppointment).subscribe((appointmentId: number) => {
			this.lastAppointmentId = appointmentId;
			if (itComesFromStep3) {
				this.assignAppointment();
			}
			else {
				this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
				this.dialogRef.close(appointmentId);
			}
		}, error => {
			this.isSubmitButtonDisabled = false;
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
		return this.formSearch.controls.completed.value && !this.data.protectedAppointment;
	}

	disableConfirmButtonStep3(): boolean {
		return !(this.formSearch.controls.completed.value && this.isAppointmentFormValid() && this.data.protectedAppointment && this.associateReferenceForm.valid);
	}

	private assignAppointment(): void {
		this.referenceAppointmentService.associateReferenceAppointment(this.associateReferenceForm.controls.reference.value.referenceId, this.lastAppointmentId).subscribe(
			successfullyAssociated => {
				if (successfullyAssociated) {
					this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
					this.dialogRef.close(this.lastAppointmentId);
				}
				else {
					this.snackBarService.showError('turnos.new-appointment.messages.COULD_NOT_ASSOCIATE')
				}
			}
		);
	}

	disablePreviuosStep(stepperParam: MatHorizontalStepper) {
		if (stepperParam.selectedIndex === 0) {
			this.editable = false;
		}
	}

	private isFormSearchValid() {
		return (this.formSearch.controls.identifType.valid
			&& this.formSearch.controls.identifNumber.valid
			&& this.formSearch.controls.gender.valid) || this.formSearch.controls.patientId.value;
	}

	private isAppointmentFormValid() {
		return this.appointmentInfoForm.valid;
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
				identificationTypeId: this.patient.personalDataDto.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.patientId
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				this.appointmentInfoForm.patchValue({ patientMedicalCoverage: null });
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(this.patientId, patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('Las coberturas fueron actualizadas correctamente');
						},
						_ => this.snackBarService.showError('Ocurrió un error al actualizar las coberturas')
					);
				}
			}
		);
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

	clearQueryParams() {
		this.router.navigate([]);
	}

	getPatientMedicalOrders() {
		this.patientMedicalOrders$ = this.prescripcionesService.getPrescription(PrescriptionTypes.STUDY, this.patientId, MEDICAL_ORDER_PENDING_STATUS, null, null, null, MEDICAL_ORDER_CATEGORY_ID);
	}

	private setPhonePrefix(itComesFromStep3: boolean): string {
		if (!this.appointmentInfoForm.controls.phonePrefix.value && !itComesFromStep3)
			return "";
		if (this.appointmentInfoForm.controls.phonePrefix.value)
			return this.appointmentInfoForm.controls.phonePrefix.value;
		if (itComesFromStep3)
			return this.associateReferenceForm.controls.reference.value.phonePrefix;
	}

	private setPhoneNumber(itComesFromStep3: boolean): string {
		if (!this.appointmentInfoForm.controls.phoneNumber.value && !itComesFromStep3)
			return "";
		if (this.appointmentInfoForm.controls.phoneNumber.value)
			return this.appointmentInfoForm.controls.phoneNumber.value;
		if (itComesFromStep3)
			return this.associateReferenceForm.controls.reference.value.phoneNumber;
	}

	private verifyExistingAppointment(): Observable<AppointmentShortSummaryDto> {
		if (this.data.isEquipmentAppointment) {
			return this.equipmentAppointmentFacade.verifyExistingEquipmentAppointment(this.patientId, this.data.date)

		}
		else {
			return this.appointmentFacade.verifyExistingAppointment(this.patientId, this.data.date, this.data.hour)
		}
	}

	private addAppointment(newAppointment: CreateAppointmentDto): Observable<number> {
		if (this.data.isEquipmentAppointment) {
			let orderId = this.appointmentInfoForm.controls.appointmentMedicalOrder.value.serviceRequestId;
			let studyId = this.appointmentInfoForm.controls.appointmentMedicalOrder.value.id;
			return this.equipmentAppointmentFacade.addAppointment(newAppointment, orderId, studyId);
		}
		else
			return this.appointmentFacade.addAppointment(newAppointment);
	}
}
