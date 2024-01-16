import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { UntypedFormGroup, UntypedFormBuilder, Validators, UntypedFormControl } from '@angular/forms';
import { VALIDATIONS, processErrors, hasError, updateControlValidator } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PatientService } from '@api-rest/services/patient.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { MatStepper } from '@angular/material/stepper';
import {
	CreateAppointmentDto,
	MedicalCoverageDto,
	IdentificationTypeDto,
	GenderDto,
	BasicPersonalDataDto,
	ReducedPatientDto,
	PatientMedicalCoverageDto,
	ReferenceSummaryDto,
	DiagnosticReportInfoDto,
	TranscribedDiagnosticReportInfoDto,
	EAppointmentModality,
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
import { DatePipe } from "@angular/common";
import { DiscardWarningComponent } from "@presentation/dialogs/discard-warning/discard-warning.component";
import { ReferenceService } from '@api-rest/services/reference.service';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { EquipmentAppointmentsFacadeService } from '@turnos/services/equipment-appointments-facade.service';
import { Observable, forkJoin, of } from 'rxjs';
import { PrescripcionesService } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { differenceInDays } from 'date-fns';
import { SearchAppointmentCriteria } from '@turnos/components/search-appointments-in-care-network/search-appointments-in-care-network.component';
import { MODALITYS_TYPES } from '@turnos/constants/appointment';
import { TranscribedOrderService } from '@turnos/services/transcribed-order.service';

const ROUTE_SEARCH = 'pacientes/search';
const TEMPORARY_PATIENT_ID = 3;
const MEDICAL_ORDER_PENDING_STATUS = '1';
const MEDICAL_ORDER_CATEGORY_ID = '363679005'
const ORDER_EXPIRED_DAYS = 30;

@Component({
	selector: 'app-new-appointment',
	templateUrl: './new-appointment.component.html',
	styleUrls: ['./new-appointment.component.scss'],
	providers: [EquipmentAppointmentsFacadeService]
})
export class NewAppointmentComponent implements OnInit {
	indexStep = Steps;
	@ViewChild('stepper', { static: false }) stepper: MatStepper;
	initialIndex = this.indexStep.MODALITY;
	preselectedPatient = false;
	public formSearch: UntypedFormGroup;
	public appointmentInfoForm: UntypedFormGroup;
	public associateReferenceForm: UntypedFormGroup;
	public modalityForm: UntypedFormGroup;
	referenceList: ReferenceSummaryDto[] = [];
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public healtInsuranceOptions: MedicalCoverageDto[] = [];
	public patientId: any;
	public showAddPatient = false;
	public editable = true;
	patientMedicalCoverages: PatientMedicalCoverage[];
	patientMedicalOrders: medicalOrderInfo[] = [];
	patient: ReducedPatientDto;
	public readonly hasError = hasError;
	readonly TEMPORARY_PATIENT_ID = TEMPORARY_PATIENT_ID;
	private readonly routePrefix;
	isFormSubmitted = false;
	public isSubmitButtonDisabled = false;
	VALIDATIONS = VALIDATIONS;
	lastAppointmentId = -1;
	readonly dateFormats = DatePipeFormat;
	patientMedicalOrderTooltipDescription = '';
	isOrderTranscribed = false;
	transcribedOrder = null;
	editableStep1 = true;
	editableStepModality = true;
	readonly MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	readonly MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	readonly MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION;
	modalitySelected: EAppointmentModality = this.MODALITY_ON_SITE_ATTENTION;
	viewModalityLabel$: Observable<boolean> = of(false);
	modalitys = MODALITYS_TYPES.slice(0, 2);

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: NewAppointmentData,
		public dialogRef: MatDialogRef<NewAppointmentComponent>,
		private readonly formBuilder: UntypedFormBuilder,
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
		private readonly datePipe: DatePipe,
		private readonly equipmentAppointmentFacade: EquipmentAppointmentsFacadeService,
		private prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private readonly transcribedOrderService: TranscribedOrderService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		if (this.data.modalityAttention) {
			this.modalitySelected = this.data.modalityAttention;
			this.editableStepModality = false;
			this.initialIndex = this.indexStep.SEARCH;
			this.viewModalityLabel$ = of(true);
		}
		if(this.data.isEquipmentAppointment){
			this.editableStepModality = false;
			this.initialIndex = this.indexStep.SEARCH;
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
			medicalOrder: this.formBuilder.group({
				appointmentMedicalOrder: [null]
			}),
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

		this.transcribedOrderService.transcribedOrder$.subscribe(transcribedOrder => {
			this.transcribedOrder = transcribedOrder;
		})
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
		if (stepper.selectedIndex > this.indexStep.MODALITY && !this.data.isEquipmentAppointment) {
			this.viewModalityLabel$ = of(true);
		} else {
			this.viewModalityLabel$ = of(false);
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
				if (this.data.isEquipmentAppointment)
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
						if (this.data.isEquipmentAppointment)
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

	private patientSearch(patientId: number) {
		this.patientService.getBasicPersonalData(patientId)
			.subscribe((reducedPatientDto: ReducedPatientDto) => {
				this.patientFound();
				if (this.data.protectedAppointment)
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
		this.showAddPatient = this.data.protectedAppointment ? false : true;
	}

	submit(itComesFromStep3?: boolean): void {
		if (this.isAppointmentFormValid()) {
			this.isSubmitButtonDisabled = true;
			this.verifyExistingAppointment().subscribe((appointmentShortSummary) => {
				if (appointmentShortSummary) {
					let appointmentFor = this.data.isEquipmentAppointment ? appointmentShortSummary.equipmentName : appointmentShortSummary.doctorFullName;
					const date = this.datePipe.transform(dateDtoToDate(appointmentShortSummary.date), DatePipeFormat.SHORT_DATE)
					const hour = this.datePipe.transform(timeDtoToDate(appointmentShortSummary.hour), DatePipeFormat.SHORT_TIME)
					const content = `El paciente ya tiene un turno el ${date} a las ${hour} hs para ${appointmentFor} en ${appointmentShortSummary.institution}`

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
		const newAppointment: CreateAppointmentDto = {
			date: this.data.date,
			diaryId: this.data.diaryId,
			hour: this.data.hour,
			openingHoursId: this.data.openingHoursId,
			overturn: this.data.overturnMode,
			patientId: this.patientId,
			patientMedicalCoverageId: this.appointmentInfoForm.value.patientMedicalCoverage?.id,
			phonePrefix:this.appointmentInfoForm.value.phonePrefix,
			phoneNumber:  this.appointmentInfoForm.value.phoneNumber,
			modality: this.modalitySelected,
			patientEmail: this.appointmentInfoForm.controls.patientEmail.value,
			applicantHealthcareProfessionalEmail: this.associateReferenceForm.controls.professionalEmail.value ? this.associateReferenceForm.controls.professionalEmail.value : null,
			referenceId: this.associateReferenceForm?.controls?.reference?.value?.id
		};
		this.addAppointment(newAppointment).subscribe((appointmentId: number) => {
			this.lastAppointmentId = appointmentId;
			if (itComesFromStep3) {
				const valueEmail = this.getEmail();
				this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
				this.dialogRef.close({ id: this.lastAppointmentId, email: valueEmail });
			}
			else {
				this.snackBarService.showSuccess('turnos.new-appointment.messages.APPOINTMENT_SUCCESS');
				this.dialogRef.close({ id: appointmentId, email: this.appointmentInfoForm.controls.patientEmail.value });
			}
		}, error => {
			this.isSubmitButtonDisabled = false;
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	private getEmail(): string {
		return this.data.modalityAttention === this.MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION ? this.associateReferenceForm.controls.professionalEmail.value : this.appointmentInfoForm.controls.patientEmail.value;
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
		return this.formSearch.controls.completed.value && !this.data.protectedAppointment && this.stepper.selectedIndex === this.indexStep.INFO;
	}

	disableConfirmButtonStep3(): boolean {
		return !(this.formSearch.controls.completed.value && this.isAppointmentFormValid() && this.data.protectedAppointment && this.associateReferenceForm.valid);
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
			this.patientMedicalOrders = [];
			if (this.transcribedOrder) {
				this.prescripcionesService.deleteTranscribedOrder(this.patientId, this.transcribedOrder.serviceRequestId).subscribe(() => {
					this.transcribedOrderService.resetTranscribedOrder();
				});
			}
		}
		this.goBack(stepper);
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

	cancelBtnActions() {
		if (this.transcribedOrder) {
			this.prescripcionesService.deleteTranscribedOrder(this.patientId, this.transcribedOrder.serviceRequestId).subscribe(() => {
				this.transcribedOrderService.resetTranscribedOrder();
			});
			
		}
		this.clearQueryParams();
	}

	getPatientMedicalOrders() {
		const prescriptions$ = this.prescripcionesService.getMedicalOrders(this.patientId, MEDICAL_ORDER_PENDING_STATUS, MEDICAL_ORDER_CATEGORY_ID);
		const transcribedOrders$ = this.prescripcionesService.getTranscribedOrders(this.patientId);
		forkJoin([prescriptions$, transcribedOrders$]).subscribe(masterdataInfo => {
			this.mapDiagnosticReportInfoDtoToMedicalOrderInfo(masterdataInfo[0]);
			this.mapTranscribeOrderToMedicalOrderInfo(masterdataInfo[1]);
		});
	}

	mapDiagnosticReportInfoDtoToMedicalOrderInfo(patientMedicalOrders: DiagnosticReportInfoDto[]) {
		let text = 'image-network.appointments.medical-order.ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			patientMedicalOrders.map(diagnosticReportInfo => {
				if (differenceInDays(new Date(), new Date(diagnosticReportInfo.creationDate)) <= ORDER_EXPIRED_DAYS) {
					this.patientMedicalOrders.push({
						serviceRequestId: diagnosticReportInfo.serviceRequestId,
						studyName: diagnosticReportInfo.snomed.pt,
						studyId: diagnosticReportInfo.id,
						displayText: `${translatedText} # ${diagnosticReportInfo.serviceRequestId} - ${diagnosticReportInfo.snomed.pt}`,
						isTranscribed: false
					})
				}
			}).filter(value => value !== null && value !== undefined);
		});
	}

	mapTranscribeOrderToMedicalOrderInfo(transcribedOrders: TranscribedDiagnosticReportInfoDto[]) {
		let text = 'image-network.appointments.medical-order.TRANSCRIBED_ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			transcribedOrders.map(medicalOrder => {
				this.patientMedicalOrders.push({
					serviceRequestId: medicalOrder.serviceRequestId,
					studyName: medicalOrder.studyName,
					displayText: `${translatedText} - ${medicalOrder.studyName}`,
					isTranscribed: true
				})
			}).filter(value => value !== null && value !== undefined);
		});
	}

	goBack(stepper: MatStepper) {
		stepper.previous();
		this.showAddPatient = false;
	}

	private verifyExistingAppointment(): Observable<any> {
		return this.data.isEquipmentAppointment ? this.equipmentAppointmentFacade.verifyExistingEquipmentAppointment(this.patientId, this.data.date) : this.appointmentFacade.verifyExistingAppointment(this.patientId, this.data.date, this.data.hour, this.data.institutionId)
	}

	private addAppointment(newAppointment: CreateAppointmentDto): Observable<number> {
		if (this.data.isEquipmentAppointment) {
			let medicalOrder = this.appointmentInfoForm.get('medicalOrder').get('appointmentMedicalOrder').value;
			let orderId = medicalOrder?.serviceRequestId;
			let studyId = medicalOrder?.studyId;
			if (medicalOrder?.isTranscribed) {
				this.transcribedOrderService.resetTranscribedOrder();
				return this.equipmentAppointmentFacade.addAppointmentWithTranscribedOrder(newAppointment, orderId);
			}
			return this.equipmentAppointmentFacade.addAppointment(newAppointment, orderId, studyId);
		}
		else
			return this.appointmentFacade.addAppointment(newAppointment);
	}

	private setReferenceInformation() {
		if (this.data.referenceSummary) {
			const referenceSummary = this.data.referenceSummary;
			this.referenceList = [referenceSummary];
			this.associateReferenceForm.controls.reference.setValue(referenceSummary);
			this.associateReferenceForm.controls.reference.disable();
		}
		else
			this.referenceService.getReferencesSummary(this.patientId, this.data.searchAppointmentCriteria).subscribe(references => this.referenceList = references);
	}
}

export interface NewAppointmentData {
	date: string,
	diaryId: number,
	hour: string,
	openingHoursId: number,
	overturnMode: boolean,
	patientId?: number,
	protectedAppointment?: boolean,
	isEquipmentAppointment?: boolean,
	modalityAttention: EAppointmentModality,
	searchAppointmentCriteria?: SearchAppointmentCriteria,
	referenceSummary?: ReferenceSummaryDto,
	institutionId?: number,
}

export interface medicalOrderInfo {
	serviceRequestId: number,
	studyName: string,
	studyId?: number,
	displayText: string,
	isTranscribed: boolean,
	coverageDto?: PatientMedicalCoverageDto,
}
enum Steps {
	MODALITY = 0,
	SEARCH = 1,
	INFO = 2,
	PROTECTED = 3,
}
