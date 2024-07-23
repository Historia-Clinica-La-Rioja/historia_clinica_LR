import { MedicalCoverageInfoService } from './../../../historia-clinica/modules/ambulatoria/services/medical-coverage-info.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ContextService } from '@core/services/context.service';
import {
	APPOINTMENT_CANCEL_OPTIONS,
	APPOINTMENT_STATES_ID,
	getAppointmentState,
	getScanStatusCustom,
	MAX_LENGTH_MOTIVE,
	modality,
	MODALITYS_TYPES,
	SCAN_COMPLETED,
} from '../../constants/appointment';
import {
	RECURRING_APPOINTMENT_OPTIONS,
} from '../../constants/appointment';
import {
	ApiErrorMessageDto,
	AppFeature,
	AppointmentDto,
	AppointmentListDto,
	CompleteDiaryDto,
	DateDto,
	CreateAppointmentDto,
	CreateCustomAppointmentDto,
	CustomRecurringAppointmentDto,
	DateTimeDto,
	DiaryOpeningHoursFreeTimesDto,
	EAppointmentModality,
	ERole,
	FreeAppointmentSearchFilterDto,
	PatientMedicalCoverageDto,
	ProfessionalPersonDto,
	TimeDto,
	RecurringTypeDto,
	UpdateAppointmentDateDto,
	UpdateAppointmentDto,
	ItsCoveredResponseDto,
	GenderDto,
	IdentificationTypeDto,
} from '@api-rest/api-model.d';

import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';
import { VALIDATIONS, getError, hasError, processErrors, updateControlValidator } from '@core/utils/form.utils';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { MapperService } from '@core/services/mapper.service';
import {
	determineIfIsHealthInsurance,
	HealthInsurance,
	MedicalCoverageComponent,
	PatientMedicalCoverage,
	PrivateHealthInsurance
} from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import {
	catchError,
	finalize,
	map,
	take,
} from 'rxjs/operators';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PermissionsService } from '@core/services/permissions.service';
import {
	EMPTY,
	Observable,
	combineLatest,
} from 'rxjs';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { PersonMasterDataService } from "@api-rest/services/person-master-data.service";
import { SummaryCoverageInformation } from '@historia-clinica/modules/ambulatoria/components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { CalendarEvent } from 'angular-calendar';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { Color } from '@presentation/colored-label/colored-label.component';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { getAppointmentEnd, getAppointmentStart, toCalendarEvent } from '@turnos/utils/appointment.utils';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { Router } from '@angular/router';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { convertDateTimeDtoToDate, dateDtoToDate, dateTimeDtoToDate, dateToDateDto, dateToTimeDto, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DiaryService } from '@api-rest/services/diary.service';

import { PatientNameService } from '@core/services/patient-name.service';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { RecurringCustomizePopupComponent } from '../recurring-customize-popup/recurring-customize-popup.component';
import { RecurringCancelPopupComponent } from '../recurring-cancel-popup/recurring-cancel-popup.component';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { sameHourAndMinute, timeDifference, toHourMinuteSecond } from '@core/utils/date.utils';
import { ButtonType } from '@presentation/components/button/button.component';
import { pushIfNotExists } from '@core/utils/array.utils';
import { ScanPatientComponent } from '@pacientes/dialogs/scan-patient/scan-patient.component';
import { PatientInformationScan } from '@pacientes/pacientes.model';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';

const TEMPORARY_PATIENT = 3;
const REJECTED_PATIENT = 6;
const BELL_LABEL = 'Llamar paciente'
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const ROLES_TO_EDIT: ERole[]
	= [ERole.ADMINISTRATIVO];
const ROLE_TO_DOWNDLOAD_REPORTS: ERole[] = [ERole.ADMINISTRATIVO];
const ROLE_TO_MAKE_VIRTUAL_CONSULTATION: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const MONTHS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

const enum itsCovered {
	COVERED = 1,
	NOT_COVERED = 2
}

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {
	readonly SECOND_OPINION_VIRTUAL_ATTENTION = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION;
	readonly ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly TEMPORARY_PATIENT = TEMPORARY_PATIENT;
	readonly BELL_LABEL = BELL_LABEL;
	readonly Color = Color;
	modalitys: modality[] = [];
	datestypes = DATESTYPES;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;

	appointment: AppointmentDto;
	parentAppointment: AppointmentDto;
	appointments: CalendarEvent[];
	selectedState: APPOINTMENT_STATES_ID;
	formMotive: UntypedFormGroup;
	formEdit: UntypedFormGroup;
	formDate: UntypedFormGroup;
	formObservations: UntypedFormGroup;
	institutionId = this.contextService.institutionId;
	coverageText: string;
	coverageNumber: any;
	coverageCondition: string;
	coverageData: PatientMedicalCoverage;
	phoneNumber: string;
	summaryCoverageData: SummaryCoverageInformation = {};
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleAdmin$: Observable<boolean>;
	hasRoleToDownloadReports$: Observable<boolean>;
	hasRoleToAddObservations$: Observable<boolean>;
	agendaOwner: boolean;
	attachProfessional: boolean;
	patientMedicalCoverages: PatientMedicalCoverage[];

	hideFilterPanel = false;

	isDateFormVisible = false;
	startAgenda = dateToDateDto(new Date(this.data.agenda.startDate));
	endAgenda = dateToDateDto(new Date(this.data.agenda.endDate));
	availableDays: number[] = [];
	availableMonths: number[] = [];
	availableYears: number[] = [];
	possibleScheduleHours: TimeDto[] = [];
	selectedDate = new Date(this.data.appointmentData.date);
	recurringTypes: RecurringTypeDto[] = [];

	isCheckedDownloadAnexo = false;
	isCheckedDownloadFormulario = false;
	downloadReportIsEnabled: boolean;
	isMqttCallEnabled = false;

	hideAbsentMotiveForm = true;
	absentMotive: string;
	absentAppointment = false;
	hideObservationForm = true;
	hideObservationTitle = true;
	observation: string;
	firstCoverage: number;
	canCoverageBeEdited = false;

	isRejectedPatient: boolean = false;
	selectedModality: modality;
	isVirtualConsultationModality: boolean = false;
	canDownloadReport = false;
	dateAppointment: DateDto;
	viewInputEmail = false;
	selectedOpeningHourId: number;

	diaryOpeningHoursFreeTimes: DiaryOpeningHoursFreeTimesDto[];
	HABILITAR_TELEMEDICINA: boolean = false;

	patientSummary: PatientSummary;

	today = dateToDateDto(new Date())

	isHabilitarRecurrencia: boolean = false;
	HABILITAR_RECURRENCIA_EN_DESARROLLO: AppFeature = AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO;

	customRecurringAppointmentDto: CustomRecurringAppointmentDto;
	recurringTypeSelected: RecurringTypeDto;
	isAppointmentAfterToday: boolean = false;
	everyWeekOption: string = "Cada semana";
	NO_REPEAT: number = RECURRING_APPOINTMENT_OPTIONS.NO_REPEAT;
	EVERY_WEEK: number = RECURRING_APPOINTMENT_OPTIONS.EVERY_WEEK;
	saveButtonType = ButtonType.RAISED;
	isSaveLoading: boolean = false;
	coverage: Coverage;
	HABILITAR_VISTA_COBERTURA_TURNOS: boolean = false;
	waitingTime: string;
	HABILITAR_ATENDER_TURNO_MANUAL: boolean = false;
	patientInformationScan: string;
	genderOptions: GenderDto[];
	identificationTypeList: IdentificationTypeDto[];
	HABILITAR_ANEXO_II_MENDOZA = false;
	TYPE_DNI: string;
	scanMenssage: ColoredIconText = SCAN_COMPLETED;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			appointmentData: PatientAppointmentInformation,
			hasPermissionToAssignShift: boolean,
			agenda: CompleteDiaryDto
		},
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly appointmentFacade: AppointmentsFacadeService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService,
		private readonly jitsiCallService: JitsiCallService,
		private readonly router: Router,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly patientNameService: PatientNameService,
		private readonly diaryService: DiaryService,
	) {
		this.setFeatureFlags();
	}

	ngOnInit(): void {
		this.startAgenda.day = this.startAgenda.day + 1;
		this.medicalCoverageInfo.clearAll();
		if (this.data.appointmentData.patient.typeId === REJECTED_PATIENT) {
			this.isRejectedPatient = true;
		}
		this.formMotive = this.formBuilder.group({
			motive: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVE)]]
		});

		this.formEdit = this.formBuilder.group({
			//Medical Coverage selected in Edit Mode
			newCoverageData: null,
			phonePrefix: null,
			phoneNumber: null
		});

		this.formDate = this.formBuilder.group({
			hour: ['', [Validators.required]],
			day: ['', Validators.required],
			month: ['', Validators.required],
			year: ['', Validators.required],
			modality: ['', [Validators.required]],
			email: [''],
			recurringType: ['']
		});

		this.formObservations = this.formBuilder.group({
			observation: ['', [Validators.required]]
		});

		this.setMedicalCoverages();
		this.formEdit.controls.phoneNumber.setValue(this.data.appointmentData.phoneNumber);
		this.formEdit.controls.phonePrefix.setValue(this.data.appointmentData.phonePrefix);
		if (this.data.appointmentData.phoneNumber) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
		}

		this.appointmentService.get(this.data.appointmentData.appointmentId)
			.subscribe(appointment => {
				if (this.isHabilitarRecurrencia) {
					this.recurringTypeSelected = appointment?.recurringTypeDto;
					this.changeRecurringTypeText(appointment);
					this.formDate.get('recurringType').setValue(this.recurringTypeSelected?.id);
					this.isAppointmentAfterToday = this.data.appointmentData.date >= new Date();
					this.setParentAppointment(appointment.parentAppointmentId);
				}
				this.waitingTime = timeDifference(convertDateTimeDtoToDate(appointment.updatedOn));
				this.appointment = appointment;
				this.observation = appointment.observation;
				if (this.observation) {
					this.hideObservationTitle = false;
					this.formObservations.controls.observation.setValue(this.observation);
				}
				this.selectedState = this.appointment?.appointmentStateId;
				this.absentAppointment = this.isMotiveRequired();
				this.absentMotive = this.appointment.stateChangeReason;
				if (this.absentMotive) {
					this.formMotive.controls.motive.setValue(this.absentMotive);
				}
				if (this.appointment.patientMedicalCoverageId && this.data.appointmentData.patient?.id) {
					this.patientMedicalCoverageService.getPatientMedicalCoverage(this.appointment.patientMedicalCoverageId)
						.subscribe(coverageData => {
							if (coverageData) {
								this.coverageData = this.mapperService.toPatientMedicalCoverage(coverageData);
								this.isMedicalCoverage();
								this.updateSummaryCoverageData();
								this.formEdit.controls.newCoverageData.setValue(coverageData.id);
								this.firstCoverage = coverageData.id;
								this.setCoverageText(coverageData);
							}
						});
				}
				this.scanMenssage = getScanStatusCustom(this.appointment.patientIdentityAccreditationStatus);
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.data.appointmentData.phonePrefix, this.data.appointmentData.phoneNumber);
				this.checkInputUpdatePermissions();
				this.selectedModality = MODALITYS_TYPES.find(m => m.value === this.appointment.modality);
				this.modalitys.push(MODALITYS_TYPES[0]);
				this.pushPatientVirtualAttentionOption();
				if (this.selectedModality.value === this.SECOND_OPINION_VIRTUAL_ATTENTION) {
					this.modalitys.push(MODALITYS_TYPES[2])
				}
				if (this.selectedModality.value === this.SECOND_OPINION_VIRTUAL_ATTENTION || this.selectedModality.value === EAppointmentModality.PATIENT_VIRTUAL_ATTENTION) {
					this.isVirtualConsultationModality = true;
				}
				this.checkDownloadReportAvailability();
				this.initializeFormDate();
				this.loadAvailableDays(this.dateAppointment, true);
				this.setAvailableMonths();
				this.loadAppointmentsHours(dateToDateDto(this.selectedDate), true);
				this.setAvailableYears();
				this.setModalityAndValidator(false);
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleAdmin$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).pipe(take(1));

		this.hasRoleToDownloadReports$ = this.permissionsService.hasContextAssignments$(ROLE_TO_DOWNDLOAD_REPORTS).pipe(take(1));

		const loggedUserHealthcareProfessionalId$ = this.healthcareProfessionalService.getHealthcareProfessionalByUserId().pipe(take(1));
		const loggedUserHasRoleToMakeVirtualConsultation$ = this.permissionsService.hasContextAssignments$(ROLE_TO_MAKE_VIRTUAL_CONSULTATION).pipe(take(1));

		combineLatest([loggedUserHealthcareProfessionalId$, loggedUserHasRoleToMakeVirtualConsultation$]).subscribe(([healthcareProfessionalId, hasRole]) => {
			this.agendaOwner = (this.data.agenda.healthcareProfessionalId === healthcareProfessionalId) && hasRole;
			this.attachProfessional = (this.data.agenda.associatedProfessionalsInfo.find(professional => professional.id === healthcareProfessionalId) && hasRole)
		});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				const identificationType = identificationTypes.find(identificationType => identificationType.id == this.data.appointmentData.patient.identificationTypeId);
				this.patientSummary = {
					fullName: this.patientNameService.completeName(this.data.appointmentData.patient.names.firstName, this.data.appointmentData.patient.names.nameSelfDetermination, this.data.appointmentData.patient.names.lastName, this.data.appointmentData.patient.names.middleNames, this.data.appointmentData.patient.names.otherLastNames),
					id: this.data.appointmentData.patient.id,
					identification: {
						number: this.data.appointmentData.patient.identificationNumber,
						type: identificationType.description
					}
				}
			});

		if (this.isHabilitarRecurrencia) {
			this.setRecurringAppointmentType();
			this.formDate.get('recurringType').setValidators([Validators.required]);
			this.setCustomAppointment();
		}

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => {
				this.identificationTypeList = identificationTypes;
				this.TYPE_DNI = this.identificationTypeList.find(type => type.description === 'DNI').description;
			});

		this.personMasterDataService.getGenders().subscribe(
			genders => { this.genderOptions = genders; });
	}

	setCustomAppointment() {
		this.appointmentService.getCustomAppointment(this.data.appointmentData.appointmentId)
			.subscribe((result: CustomRecurringAppointmentDto) => {
				if (!result) return;

				this.customRecurringAppointmentDto = {
					endDate: result.endDate,
					weekDayId: result.weekDayId,
					repeatEvery: result.repeatEvery
				}
			});
	}

	setRecurringAppointmentType() {
		this.appointmentService.getRecurringAppointmentType()
			.subscribe((resp: RecurringTypeDto[]) => {
				if (resp) {
					this.recurringTypes = resp;
					this.changeRecurringTypeSelector(new Date(this.data.appointmentData.date));
				}
			});
	}

	initializeFormDate() {
		const date = new Date(this.appointment.date)
		date.setMinutes(date.getMinutes() + date.getTimezoneOffset())
		this.dateAppointment = dateToDateDto(date)	
		if (this.today.month === this.dateAppointment.month && this.today.year === this.dateAppointment.year) {
			if (this.startAgenda.year === this.today.year && this.startAgenda.month === this.today.month)
				this.dateAppointment.day = this.startAgenda.day > this.today.day ? this.startAgenda.day : this.today.day;
			else
				this.dateAppointment.day = this.today.day
		} else {
			this.calculateSetAppointmentDay();
		}
		this.formDate.controls.day.setValue(this.selectedDate.getDate());
		this.formDate.controls.month.setValue(this.dateAppointment.month);
		this.formDate.controls.year.setValue(this.dateAppointment.year);
		this.formDate.controls.modality.setValue(this.selectedModality.value);
	}

	private pushPatientVirtualAttentionOption = () => {
		this.modalitys.push(MODALITYS_TYPES[1]);

		if (!this.HABILITAR_TELEMEDICINA) {
			this.formDate.controls.modality.setValue(MODALITYS_TYPES[1]);
			this.formDate.controls.modality.disable();
		}
	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES).subscribe(isOn => this.downloadReportIsEnabled = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_LLAMADO).subscribe(isEnabled => this.isMqttCallEnabled = isEnabled);
		this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.HABILITAR_TELEMEDICINA = isEnabled);
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO).subscribe((isOn: boolean) => this.isHabilitarRecurrencia = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_VISTA_COBERTURA_TURNOS).subscribe((isOn: boolean) => this.HABILITAR_VISTA_COBERTURA_TURNOS = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_ATENDER_TURNO_MANUAL).subscribe((isOn: boolean) => this.HABILITAR_ATENDER_TURNO_MANUAL = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_ANEXO_II_MENDOZA).subscribe((isOn: boolean) => this.HABILITAR_ANEXO_II_MENDOZA = isOn);
	}

	private checkInputUpdatePermissions() {
		this.canCoverageBeEdited = this.isAssigned();
		this.changeInputUpdatePermissions();
	}

	private changeInputUpdatePermissions() {
		this.canCoverageBeEdited ? this.formEdit.get('newCoverageData').enable()
			: this.formEdit.get('newCoverageData').disable();
	}

	entryCall() {
		this.jitsiCallService.open(this.appointment.callLink);
		this.closeDialog();
		this.router.navigate([`${AppRoutes.Institucion}/${this.contextService.institutionId}/ambulatoria/paciente/${this.appointment.patientId}`]);
	}

	private checkDownloadReportAvailability() {
		this.hasRoleToDownloadReports$.subscribe(hasRole => {
			this.canDownloadReport = this.downloadReportIsEnabled && !this.isAbsent() && (hasRole === true || this.data.hasPermissionToAssignShift)
		})
	}

	dateFormToggle(): void {
		this.isDateFormVisible = !this.isDateFormVisible;
		this.changeRecurringTypeSelector(new Date(this.data.appointmentData.date));
	}

	cancelDateForm(): void {
		this.formDate.reset();
		this.initializeFormDate();
		this.dateFormToggle();
	}

	openDateForm(): void {
		this.dateFormToggle();
	}

	isToday(date: DateDto): boolean {
		return (this.today.year === date.year && this.today.month === date.month && this.today.day === date.day)
	}

	selectDate(dateType: DATESTYPES) {
		switch (dateType) {
			case DATESTYPES.DAY:
				this.dateAppointment.day = this.formDate.controls.day.value;
				this.loadAppointmentsHours(this.dateAppointment);
				break;
			case DATESTYPES.MONTH:
				this.dateAppointment.month = this.formDate.controls.month.value;
				this.calculateSetAppointmentDay();
				this.loadAvailableDays(this.dateAppointment);
				this.possibleScheduleHours = [];
				this.formDate.controls.day.setValue(null);
				this.formDate.controls.hour.setValue(null);
				break;
			case DATESTYPES.YEAR:
				this.dateAppointment.year = this.formDate.controls.year.value;
				this.startAgenda.year = this.dateAppointment.year;
				const now = dateToDateDto(new Date());
				if (now.year === this.dateAppointment.year) {
					this.dateAppointment.month = now.month;
				} else {
					this.dateAppointment.month = 1;
				}
				this.setAvailableMonths();
				this.formDate.controls.day.setValue(null);
				this.formDate.controls.hour.setValue(null);
				this.formDate.controls.month.setValue(null);
				break;
		}
	}

	checkOption(id: number) {
		if (id != RECURRING_APPOINTMENT_OPTIONS.CUSTOM) return;

		if (this.appointment.recurringTypeDto.id == RECURRING_APPOINTMENT_OPTIONS.CUSTOM
			|| (this.appointment.recurringTypeDto.id == RECURRING_APPOINTMENT_OPTIONS.NO_REPEAT
				&& !this.appointment.parentAppointmentId
				|| this.parentAppointment?.recurringTypeDto.id == RECURRING_APPOINTMENT_OPTIONS.CUSTOM)
		) {
			this.dialog.open(RecurringCustomizePopupComponent, {
				disableClose: true,
				width: '35%',
				data: {
					appointmentDate: dateTimeDtoToDate(this.getDateNow()),
					customAppointment: this.customRecurringAppointmentDto
				}
			}).afterClosed()
				.subscribe((data: CustomRecurringAppointmentDto) => {
					this.customRecurringAppointmentDto = data

					if (!this.customRecurringAppointmentDto)
						this.formDate.controls.recurringType.setErrors({ invalid: 'Faltan completar datos de Personalizar' });
					else
						this.formDate.controls.recurringType.setErrors(null);
				});
		}
	}

	calculateSetAppointmentDay() {
		if (this.startAgenda.month === this.dateAppointment.month && this.startAgenda.year === this.dateAppointment.year) {
			this.dateAppointment.day = this.startAgenda.day >= this.today.day ? this.startAgenda.day : this.today.day;
		} else {
			if (this.today.month === this.dateAppointment.month && this.today.year === this.dateAppointment.year) {
				this.dateAppointment.day = this.today.day;
			} else {
				this.dateAppointment.day = 1;
			}
		}
	}

	setAvailableDays(dates: DateDto[], isInitial?: boolean) {
		this.availableDays = [];
		dates.forEach(element => {
			if (!this.availableDays.includes(element.day))
				this.availableDays.push(element.day);
		});
		const appointmentSelectedDate = this.selectedDate.getDate();
		if (isInitial && !this.availableDays.includes(appointmentSelectedDate)) {
			this.availableDays.push(appointmentSelectedDate);
		}
		this.availableDays.sort((a, b) => a - b);
	}

	setAvailableMonths() {
		let selectedYear = this.formDate.controls.year.getRawValue();
		this.availableMonths = MONTHS.filter(month => 
			selectedYear === this.today.year ? 
				selectedYear === this.endAgenda.year ? month >= this.today.month && month <= this.endAgenda.month : month >= this.today.month
			:
				selectedYear === this.endAgenda.year ? month <= this.endAgenda.month : month > 0
		);
	}

	setAvailableYears() {
		for (var i = this.startAgenda.year; i <= this.endAgenda.year; i++) {
			this.availableYears.push(i);
		}
	}

	setModalityAndValidator(change: boolean) {
		switch (this.formDate.controls.modality.value) {
			case EAppointmentModality.PATIENT_VIRTUAL_ATTENTION:
				updateControlValidator(this.formDate, 'email', [Validators.required, Validators.email]);
				this.viewInputEmail = true;
				if (this.appointment.patientEmail) {
					this.formDate.controls.email.setValue(this.appointment.patientEmail);
				}
				break;
			case EAppointmentModality.ON_SITE_ATTENTION:
				updateControlValidator(this.formDate, 'email', []);
				this.formDate.controls.email.setValue(null);
				this.viewInputEmail = false;
				break;
		}
		if (change) {
			this.selectDate(DATESTYPES.MONTH);
		}
	}

	setDefaultAppointmentHour() {
		let appointmentHour: TimeDto = dateToTimeDto(new Date());
		let partes = this.appointment.hour.split(':');
		appointmentHour.hours = parseInt(partes[0]);
		appointmentHour.minutes = parseInt(partes[1]);
		appointmentHour.seconds = parseInt(partes[2]);
		this.possibleScheduleHours = pushIfNotExists<TimeDto>(this.possibleScheduleHours, appointmentHour, compareHours);
		this.possibleScheduleHours.sort((a, b) => a.hours - b.hours || a.minutes - b.minutes);
		const hourToSet = this.possibleScheduleHours.find(hours => compareHours(hours, appointmentHour));
		this.formDate.controls.hour.setValue(hourToSet);

		function compareHours(time1: TimeDto, time2: TimeDto): boolean {
			const dateTime1 = timeDtoToDate(time1);
			const dateTime2 = timeDtoToDate(time2);
			return sameHourAndMinute(dateTime1, dateTime2);
		}
	}

	loadAppointmentsHours(date: DateDto, isInitial?: boolean) {
		this.possibleScheduleHours = [];
		this.selectedOpeningHourId = null;
		this.formDate.controls.hour.setValue(null);
		const searchCriteria = this.prepareSearchCriteria(date);
		this.diaryService.getDailyFreeAppointmentTimes(this.data.agenda.id, searchCriteria).subscribe((diaryOpeningHours: DiaryOpeningHoursFreeTimesDto[]) => {
			if (diaryOpeningHours.length) {
				this.filterAndUpdateDiaryOpeningHoursFreeTimes(diaryOpeningHours, date);
				if (isInitial) {
					this.setDefaultAppointmentHour();
				}
			}
		})
	}

	filterAndUpdateDiaryOpeningHoursFreeTimes(diaryOpeningHours: DiaryOpeningHoursFreeTimesDto[], date: DateDto) {
		diaryOpeningHours.forEach(times => {
			times.freeTimes.forEach(time => {
				if (this.isToday(date)) {
					const now = new Date();
					if (time.hours > now.getHours() || (time.hours === now.getHours() && time.minutes > now.getMinutes())) {
						this.possibleScheduleHours.push(time);
					}
				} else {
					this.possibleScheduleHours.push(time);
				}
			})
		})
		this.diaryOpeningHoursFreeTimes = diaryOpeningHours;
	}

	getSelectedOpeningHourId(): number {
		let timeSelected = this.formDate.controls.hour.value;
		let openingHoursId = null;
		this.diaryOpeningHoursFreeTimes.forEach(times => {
			if (times.freeTimes.find(t => t = timeSelected)) {
				openingHoursId = times.openingHoursId
			}
		})
		return openingHoursId
	}

	prepareSearchCriteria(dateSelect: DateDto): FreeAppointmentSearchFilterDto {
		const searchCriteria: FreeAppointmentSearchFilterDto = {
			date: dateSelect,
			modality: this.formDate.controls.modality.value,
			mustBeProtected: this.appointment.protected,
		}
		return searchCriteria;
	}

	loadAvailableDays(date: DateDto, isInitial?: boolean) {
		const searchCriteria = this.prepareSearchCriteria(date);
		this.diaryService.getMonthlyFreeAppointmentDates(this.data.agenda.id, searchCriteria).subscribe((dates: DateDto[]) => {
			this.setAvailableDays(dates, isInitial);
		}, error => {
			this.availableDays = [];
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		})
	}

	updateAppointmentOverturn(appointmentId: number, appointmentStateId: number, overturn: boolean, patientId: number): void {
		const appointment: UpdateAppointmentDto = {
			appointmentId: appointmentId,
			appointmentStateId: appointmentStateId,
			overturn: overturn,
			patientId: patientId,
			phonePrefix: this.data.appointmentData.phonePrefix,
			phoneNumber: this.data.appointmentData.phoneNumber,
			patientMedicalCoverageId: this.appointment.patientMedicalCoverageId
		}
		this.appointmentFacade.updateAppointment(appointment).subscribe(() => { },
			error => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			});
	}

	updateAppointmentDate() {
		const previousDate = new Date(this.data.appointmentData.date);
		const dateNow: DateTimeDto = this.getDateNow();
		const updateAppointmentDate: UpdateAppointmentDateDto = {
			appointmentId: this.data.appointmentData.appointmentId,
			date: dateNow,
			openingHoursId: this.getSelectedOpeningHourId(),
			modality: this.formDate.controls.modality.value,
			patientEmail: this.formDate.controls.email.value,
		};

		if (!this.isHabilitarRecurrencia)
			this.updateDate(updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
		else {
			updateAppointmentDate.recurringAppointmentTypeId = this.formDate.get('recurringType').value;
			if (updateAppointmentDate.recurringAppointmentTypeId == RECURRING_APPOINTMENT_OPTIONS.NO_REPEAT) {
				if (previousDate.getTime() === dateTimeDtoToDate(dateNow).getTime() && this.isParentAppointmentOrHasAppointmentChilds()) {
					this.openConfirmDialog()
						.afterClosed()
						.subscribe((result: boolean) => {
							if (result)
								this.save(updateAppointmentDate.openingHoursId, updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
						});
				} else {
					this.updateDate(updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
				}
			} else {
				// Date changed
				if (previousDate.getDate() !== dateTimeDtoToDate(dateNow).getDate()) {
					// Is recurring appointment
					if (this.appointment.parentAppointmentId) {
						this.updateDate(updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
					} else {
						// The original/father appointment have recurring appointments
						if (this.appointment.hasAppointmentChilds)
							this.updateDate(updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
						else
							this.save(updateAppointmentDate.openingHoursId, updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
					}
				} else {
					// Hour or minute changed and have childs or is recurring appointment
					if (this.isHourOrMinuteChanged(previousDate, dateTimeDtoToDate(dateNow))
						&& (this.appointment.hasAppointmentChilds
							|| this.appointment.parentAppointmentId)) {
						this.openRecurringCancelPopUp('turnos.new-appointment.EDIT')
							.afterClosed()
							.subscribe((editOption: number) => {
								if (editOption)
									this.save(updateAppointmentDate.openingHoursId, updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow), editOption)
							}
							);
					} else {
						if (this.appointment.parentAppointmentId || this.appointment.hasAppointmentChilds)
							this.updateDate(updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
						else
							this.save(updateAppointmentDate.openingHoursId, updateAppointmentDate, previousDate, dateTimeDtoToDate(dateNow));
					}
				}
			}
		}
	}

	private isParentAppointmentOrHasAppointmentChilds = (): boolean => {
		return (this.appointment.hasAppointmentChilds || this.appointment.parentAppointmentId != undefined);
	}

	private getDateNow = (): DateTimeDto => {
		const hour = this.formDate.get('hour').value;
		const dateNow: DateTimeDto = {
			date: {
				year: this.formDate.controls.year.value,
				month: this.formDate.controls.month.value,
				day: this.formDate.controls.day.value,
			},
			time: {
				hours: hour.hours,
				minutes: hour.minutes,
				seconds: hour.seconds,
			}
		};
		return dateNow;
	}

	private isHourOrMinuteChanged(previousDate: Date, newDate: Date) {
		return (previousDate.getHours() !== newDate.getHours()
			|| previousDate.getMinutes() !== newDate.getMinutes());
	}

	private openRecurringCancelPopUp(translate: string): MatDialogRef<RecurringCancelPopupComponent> {
		return this.dialog.open(RecurringCancelPopupComponent, {
			data: {
				title: translate
			}
		});
	}

	private updateDate(updateAppointmentDate: UpdateAppointmentDateDto, previousDate: Date, newDate: Date) {
		this.appointmentFacade.updateDate(updateAppointmentDate).subscribe(() => {
			const date = toApiFormat(previousDate);
			this.appointmentService.getList([this.data.agenda.id], this.data.agenda.healthcareProfessionalId, date, date)
				.subscribe((appointments: AppointmentListDto[]) => {
					this.appointmentFacade.loadAppointments();
					const appointmentsInDate = this.generateEventsFromAppointments(appointments)
						.filter(appointment => appointment.start.getTime() == previousDate.getTime());

					if (appointmentsInDate.length > 0 && !this.data.appointmentData.overturn) {
						this.updateAppointmentOverturn(
							appointmentsInDate[0].meta.appointmentId,
							appointmentsInDate[0].meta.appointmentStateId,
							false,
							appointmentsInDate[0].meta.patient.id
						);
					}
					this.snackBarService.showSuccess('turnos.appointment.date.UPDATE_SUCCESS');
					this.data.appointmentData.date = this.selectedDate;
					this.selectedModality = this.modalitys.find(m => m.value === updateAppointmentDate.modality);
					this.selectedDate = newDate;
					this.data.appointmentData.date = newDate;
					const appointmentUpdate = appointments.find(a => a.id = this.data.appointmentData.appointmentId);
					this.appointment.protected = appointmentUpdate?.protected;
					if (this.isHabilitarRecurrencia) {
						this.changeRecurringTypeSelector(newDate);
						this.recurringTypeSelected = this.recurringTypes.find(v => v.id == updateAppointmentDate.recurringAppointmentTypeId);
						this.recurringTypeSelected.value = this.recurringTypes.find(rt => rt.id === this.formDate.get('recurringType').value).value;
						this.setAppointment();
					}
				});
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
		this.dateFormToggle();
	}

	private changeRecurringTypeText(appointment: AppointmentDto) {
		if (appointment.recurringTypeDto.id === RECURRING_APPOINTMENT_OPTIONS.EVERY_WEEK
			|| this.formDate.get('recurringType').value === RECURRING_APPOINTMENT_OPTIONS.EVERY_WEEK) {
			this.recurringTypeSelected.value = this.everyWeekOption;
			this.recurringTypeSelected.value += ` el ${new Date(this.data.appointmentData.date).toLocaleString('es-AR', { weekday: 'long' })}`;
		}
	}

	private changeRecurringTypeSelector(date: Date) {
		if (this.isHabilitarRecurrencia) {
			this.recurringTypes[1].value = this.everyWeekOption;
			this.recurringTypes[1].value += ` el ${date.toLocaleString('es-AR', { weekday: 'long' })}`;
		}
	}

	private save(openingHoursId: number,
		updateAppointmentDate: UpdateAppointmentDateDto,
		previousDate: Date,
		newDate: Date,
		editOption?: number) {
		this.isSaveLoading = true;
		this.getRecurringSave(openingHoursId, editOption)
			.pipe(
				finalize(() => this.isSaveLoading = false)
			)
			.subscribe(_ => {
				this.setCustomAppointment();
				this.updateDate(updateAppointmentDate, previousDate, newDate);
			}, (error: ApiErrorMessageDto) => {
				this.formDate.controls.recurringType.setErrors({ invalid: error.text });
			})
	}

	private setAppointment() {
		this.appointmentService.get(this.data.appointmentData.appointmentId)
			.subscribe(appointment => this.appointment = appointment);
	}

	private openConfirmDialog(): MatDialogRef<ConfirmDialogComponent> {
		return this.dialog.open(ConfirmDialogComponent, {
			data: {
				title: 'turnos.cancel.confirm-cancel.TITLE',
				content: 'turnos.cancel.confirm-cancel.CONTENT',
				okButtonLabel: 'turnos.cancel.confirm-cancel.CONFIRM',
				cancelButtonLabel: 'turnos.cancel.confirm-cancel.CANCEL',
			},
			width: '35%'
		})
	}

	getRecurringSave(openingHoursId: number, editOption?: number): Observable<boolean> {
		const recurringType = this.formDate.get('recurringType').value;
		if (recurringType === RECURRING_APPOINTMENT_OPTIONS.EVERY_WEEK)
			return this.everyWeekSave(openingHoursId, this.data.appointmentData.appointmentId, editOption);

		if (recurringType === RECURRING_APPOINTMENT_OPTIONS.CUSTOM)
			return this.customSave(openingHoursId, this.data.appointmentData.appointmentId, editOption);

		if (recurringType === RECURRING_APPOINTMENT_OPTIONS.NO_REPEAT)
			return this.appointmentService.noRepeat(this.data.appointmentData.appointmentId);
	}

	private customSave(openingHoursId: number, appointmentId: number, editOption?: number): Observable<boolean> {
		const createAppointmentDto: CreateAppointmentDto = this.createAppointmentDto(openingHoursId, appointmentId, editOption);
		const createCustomAppointmentDto: CreateCustomAppointmentDto = {
			createAppointmentDto,
			customRecurringAppointmentDto: this.customRecurringAppointmentDto
		}
		return this.appointmentService.customSave(createCustomAppointmentDto);
	}

	private everyWeekSave(openingHoursId: number, appointmentId: number, editOption?: number): Observable<boolean> {
		const createAppointmentDto: CreateAppointmentDto = this.createAppointmentDto(openingHoursId, appointmentId, editOption);
		return this.appointmentService.everyWeekSave(createAppointmentDto);
	}

	private createAppointmentDto(openingHoursId: number, appointmentId: number, editOption?: number): CreateAppointmentDto {
		const hour = this.formDate.get('hour').value;
		const dateNow: DateTimeDto = this.getDateNow();
		const dto: CreateAppointmentDto = {
			date: toApiFormat(dateDtoToDate(dateNow.date)),
			diaryId: this.data.agenda.id,
			hour: toHourMinuteSecond(timeDtoToDate(hour)),
			openingHoursId: openingHoursId,
			overturn: this.data.appointmentData.overturn,
			patientId: this.data.appointmentData.patient.id,
			id: appointmentId,
			appointmentOptionId: editOption,
			modality: this.appointment.modality,
			phonePrefix: this.data.appointmentData.phonePrefix,
			phoneNumber: this.data.appointmentData.phoneNumber,
			patientMedicalCoverageId: this.formEdit.controls.newCoverageData.value
		}
		return dto;
	}

	formatPhonePrefixAndNumber(phonePrefix: string, phoneNumber: string): string {
		return phoneNumber ? phonePrefix
			? "(" + phonePrefix + ") " + phoneNumber
			: phoneNumber
			: null;
	}

	updatePhoneValidators() {
		if (this.formEdit.controls.phoneNumber.value || this.formEdit.controls.phonePrefix.value) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
		} else {
			updateControlValidator(this.formEdit, 'phoneNumber', []);
			updateControlValidator(this.formEdit, 'phonePrefix', []);
		}
	}

	isInvalidFormEdit(): boolean {
		this.formEdit.markAllAsTouched();
		return this.formEdit.invalid;
	}

	private setMedicalCoverages(): void {
		if (this.data.appointmentData.patient?.id) {
			this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.data.appointmentData.patient.id))
				.pipe(
					map(
						patientMedicalCoveragesDto =>
							patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
					)
				)
				.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
		}
	}

	changeState(newStateId: APPOINTMENT_STATES_ID): void {
		this.selectedState = newStateId;
	}

	private coverageIsNotUpdate(): boolean {
		return this.formEdit.controls.newCoverageData?.value ? this.firstCoverage === this.formEdit.controls.newCoverageData?.value : true;
	}

	private updateState(newStateId: APPOINTMENT_STATES_ID): void {
		this.changeState(newStateId);
		if (this.isANewState(newStateId) && !this.isMotiveRequired()) {
			this.submitNewState(newStateId);
		}
	}

	private confirmChangeState(newStateId: APPOINTMENT_STATES_ID): void {
		const dialogRefConfirmation = this.dialog.open(DiscardWarningComponent,
			{
				data: {
					content: `turnos.appointment.confirm-dialog.CONTENT`,
					okButtonLabel: `turnos.appointment.confirm-dialog.CONFIRM`,
					cancelButtonLabel: `turnos.appointment.confirm-dialog.CANCEL`,
					contentBold: `turnos.appointment.confirm-dialog.QUESTION`,
					showMatIconError: true,
				}
			});
		dialogRefConfirmation.afterClosed().subscribe((upDateState: boolean) => {
			if (upDateState)
				if (this.isAptToScan()) {
					this.openScanPatientDialog(newStateId);
				} else {
					this.updateState(newStateId);
				}
		});
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.selectedState !== newStateId) {
			this.checkIfAbsent(newStateId);
			if (this.selectedState === APPOINTMENT_STATES_ID.ASSIGNED && newStateId === APPOINTMENT_STATES_ID.CONFIRMED && this.coverageIsNotUpdate()) {
				this.confirmChangeState(newStateId);
			} else {
				if (this.isAptToScan(newStateId)) {
					this.openScanPatientDialog(newStateId);
				} else {
					this.updateState(newStateId);
				}
			}
		}
	}

	private isAptToScan(newStateId?: APPOINTMENT_STATES_ID): boolean {
		const isCommonConditionsMet = this.HABILITAR_ANEXO_II_MENDOZA
			&& this.hasRoleAdmin$
			&& this.patientSummary.identification.type === this.TYPE_DNI
			&& this.selectedModality.value === this.ON_SITE_ATTENTION
			&& (this.selectedState === APPOINTMENT_STATES_ID.ASSIGNED || this.selectedState === APPOINTMENT_STATES_ID.ABSENT);

		if (newStateId) {
			return isCommonConditionsMet
				&& newStateId !== APPOINTMENT_STATES_ID.ABSENT
				&& newStateId !== APPOINTMENT_STATES_ID.ASSIGNED
				&& !this.appointment.patientIdentityAccreditationStatus;
		}
		return isCommonConditionsMet;
	}

	public openScanPatientDialog(newStateId: APPOINTMENT_STATES_ID): void {
		const dialogRef = this.dialog.open(ScanPatientComponent, {
			width: "32%",
			height: "600px",
			data: {
				genderOptions: this.genderOptions,
				identifyTypeArray: this.identificationTypeList,
			}
		});
		dialogRef.afterClosed().subscribe((patientInformationScan: PatientInformationScan) => {
			this.patientInformationScan = patientInformationScan?.infoRawBarCodeScan;
			if (this.patientInformationScan) {
				this.scanMenssage = SCAN_COMPLETED;
			}
			this.updateState(newStateId);
		});
	}

	private checkIfAbsent(newStateId: APPOINTMENT_STATES_ID) {
		this.absentAppointment = newStateId === APPOINTMENT_STATES_ID.ABSENT;
		this.hideAbsentMotiveForm = !(newStateId === APPOINTMENT_STATES_ID.ABSENT);
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	private cancelOptions(value: number) {
		if (value === APPOINTMENT_CANCEL_OPTIONS.CURRENT_AND_NEXTS_TURNS) {
			this.appointmentFacade.cancelRecurringAppointments(this.data.appointmentData.appointmentId, false)
				.subscribe(_ => {
					this.snackBarService.showSuccess('turnos.cancel.PLURAL_SUCCESS');
					this.appointmentFacade.loadAppointments();
					this.closeDialog('statuschanged')
				});
		}

		if (value === APPOINTMENT_CANCEL_OPTIONS.ALL_TURNS) {
			this.appointmentFacade.cancelRecurringAppointments(this.data.appointmentData.appointmentId, true)
				.subscribe(_ => {
					this.snackBarService.showSuccess('turnos.cancel.PLURAL_SUCCESS');
					this.appointmentFacade.loadAppointments();
					this.closeDialog('statuschanged')
				});
		}

		if (value === APPOINTMENT_CANCEL_OPTIONS.CURRENT_TURN) {
			const dialogRefCancelAppointment = this.dialog.open(CancelAppointmentComponent, {
				data: {
					appointmentId: this.data.appointmentData.appointmentId
				}
			});
			dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
				if (canceledAppointment) {
					const date = toApiFormat(this.data.appointmentData.date);
					this.appointmentService.getList([this.data.agenda.id], this.data.agenda.healthcareProfessionalId, date, date)
						.subscribe((appointments: AppointmentListDto[]) => {
							const appointmentsInDate = this.generateEventsFromAppointments(appointments)
								.filter(appointment => appointment.start.getTime() == new Date(this.data.appointmentData.date).getTime());

							if (appointmentsInDate.length > 0 && !this.data.appointmentData.overturn) {
								this.updateAppointmentOverturn(
									appointmentsInDate[0].meta.appointmentId,
									appointmentsInDate[0].meta.appointmentStateId,
									false,
									appointmentsInDate[0].meta.patient.id
								);
							}
						});
					this.closeDialog('statuschanged');
				}
			});
		}
	}

	cancelAppointment(): void {
		if (this.isHabilitarRecurrencia) {
			if (this.appointment?.hasAppointmentChilds || this.appointment?.parentAppointmentId) {
				this.openRecurringCancelPopUp('turnos.cancel.CANCEL')
					.afterClosed()
					.subscribe((value: number) => {
						this.cancelOptions(value);
					})
			} else {
				this.cancelOptions(APPOINTMENT_CANCEL_OPTIONS.CURRENT_TURN);
			}
		} else {
			this.cancelOptions(APPOINTMENT_CANCEL_OPTIONS.CURRENT_TURN);
		}
	}

	saveAbsent(): void {
		if (this.formMotive.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotive.value.motive);
		}
	}

	edit(): void {
		if (this.formEdit.valid) {
			if (this.isAssigned()) {
				if (this.formEdit.controls.newCoverageData.value) {
					this.coverageData = this.patientMedicalCoverages.find(mc => this.formEdit.controls.newCoverageData.value == mc.id);
					this.updateSummaryCoverageData();
					this.updateCoverageData(this.coverageData.id);
					this.setCoverageText(this.coverageData);
				} else {
					this.coverageData = null;
					this.coverageNumber = null;
					this.coverageCondition = null;
					this.updateSummaryCoverageData();
					this.updateCoverageData(null);
				}
			}
			if (this.formEdit.controls.phoneNumber.dirty || this.formEdit.controls.phonePrefix.dirty) {
				this.updatePhoneNumber(this.formEdit.controls.phonePrefix.value, this.formEdit.controls.phoneNumber.value);
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.formEdit.controls.phonePrefix.value, this.formEdit.controls.phoneNumber.value);
			}
			this.checkInputUpdatePermissions();
			this.hideFilters();
		}
	}

	isMotiveRequired(): boolean {
		return this.selectedState === APPOINTMENT_STATES_ID.ABSENT;
	}

	isAssigned(): boolean {
		return this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED;
	}

	isCancelable(): boolean {
		return (this.selectedState === APPOINTMENT_STATES_ID.ASSIGNED &&
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED) ||
			(this.selectedState === APPOINTMENT_STATES_ID.CONFIRMED &&
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) ||
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.OUT_OF_DIARY;
	}

	isAbsent(): boolean {
		return this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT;
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motive?: string): void {
		this.appointmentFacade.changeState(this.data.appointmentData.appointmentId, newStateId, motive, this.patientInformationScan)
			.subscribe(() => {
				const appointmentInformation = { id: this.data.appointmentData.appointmentId, stateId: newStateId, date: this.selectedDate };
				this.dialogRef.close(appointmentInformation);
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, _ => {
				this.changeState(this.appointment?.appointmentStateId);
				this.snackBarService.showError(`Error al actualizar estado de turno
				${getAppointmentState(this.appointment?.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	updatePhoneNumber(phonePrefix: string, phoneNumber: string) {
		this.appointmentFacade.updatePhoneNumber(this.data.appointmentData.appointmentId, phonePrefix, phoneNumber).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	updateCoverageData(patientMedicalCoverageId: number) {
		this.appointmentService.updateMedicalCoverage(this.data.appointmentData.appointmentId, patientMedicalCoverageId).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
			this.isMedicalCoverage();
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	private setCoverageText(coverageData) {
		this.coverageNumber = coverageData.affiliateNumber;
		this.coverageCondition = coverageData.condition;
		const isHealthInsurance = determineIfIsHealthInsurance(coverageData.medicalCoverage);
		if (isHealthInsurance) {
			let healthInsurance: HealthInsurance;
			healthInsurance = coverageData.medicalCoverage as HealthInsurance;
			this.coverageText = healthInsurance.acronym ?
				healthInsurance.acronym : healthInsurance.name;
		} else {
			let privateHealthInsurance: PrivateHealthInsurance;
			privateHealthInsurance = coverageData.medicalCoverage as PrivateHealthInsurance;
			this.coverageText = privateHealthInsurance.name;
		}
	}

	openMedicalCoverageDialog(): void {
		this.formEdit.controls.newCoverageData.setValue(null);
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				identificationTypeId: this.data.appointmentData.patient.identificationTypeId,
				identificationNumber: this.data.appointmentData.patient.identificationNumber,
				genderId: this.data.appointmentData.patient.genderId,
				initValues: this.patientMedicalCoverages,
				patientId: this.data.appointmentData.patient.id,
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.data.appointmentData.patient.id), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

	callPatient() {
		this.appointmentService.mqttCall(this.data.appointmentData.appointmentId)
			.pipe(
				catchError((error: ApiErrorMessageDto) => {
					this.snackBarService.showError(error.text);
					return EMPTY;
				})
			)
			.subscribe();
	}

	hideFilters(): void {
		this.hideFilterPanel = !this.hideFilterPanel;
		this.formEdit.controls.newCoverageData.setValue(this.coverageData?.id);
		this.formEdit.controls.phonePrefix.setValue(this.data.appointmentData.phonePrefix);
		this.formEdit.controls.phoneNumber.setValue(this.data.appointmentData.phoneNumber);
	}

	closeDialog(returnValue?: string) {
		if (!returnValue && (this.appointment.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || this.appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED))
			this.medicalCoverageInfo.setAppointmentMCoverage(this.summaryCoverageData);
		const appointmentInformation = { returnValue: returnValue, date: this.selectedDate };
		this.dialogRef.close(appointmentInformation);
	}

	enableDowndloadAnexo(option: boolean) {
		this.isCheckedDownloadAnexo = option;
	}

	enableDowndloadFormulario(option: boolean) {
		this.isCheckedDownloadFormulario = option;
	}

	getAppointmentTicketReport(): void {
		this.appointmentService.getAppointmentTicketPdf(this.data.appointmentData.appointmentId).subscribe((pdf) => {
			const file = new Blob([pdf], { type: 'application/pdf' });
			const blobUrl = URL.createObjectURL(file);
			const div = document.querySelector("#pdfPrinter");
			const iframe = document.createElement("iframe");
			iframe.setAttribute("src", blobUrl);
			div.appendChild(iframe);
			iframe.contentWindow.print();
		});
	}

	getReportAppointment(): void {
		if (this.isCheckedDownloadAnexo && this.isCheckedDownloadFormulario) {
			this.appointmentService.getAnexoPdf(this.data.appointmentData).subscribe();
			this.appointmentService.getFormPdf(this.data.appointmentData).subscribe();
		} else if (this.isCheckedDownloadAnexo && !this.isCheckedDownloadFormulario) {
			this.appointmentService.getAnexoPdf(this.data.appointmentData).subscribe();
		} else {
			this.appointmentService.getFormPdf(this.data.appointmentData).subscribe();
		}
	}

	clear(): void {
		this.formEdit.controls.newCoverageData.setValue(null);
	}

	setHideObservationTitle(value: boolean): void {
		this.hideObservationTitle = value;
	}

	setHideObservationForm(value: boolean): void {
		this.hideObservationForm = value;
	}

	setHideAbsentMotiveForm(value: boolean): void {
		this.hideAbsentMotiveForm = value;
	}

	updateObservation(): void {
		this.observation = this.formObservations.get('observation').value;
		this.appointmentFacade.updateObservation(this.data.appointmentData.appointmentId, this.formObservations.controls.observation.value).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.observations.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
		this.setHideObservationForm(true);
	}

	cancelObservation(): void {
		this.hideObservationForm = true;
		this.hideObservationTitle = !this.observation;
		this.formObservations.controls.observation.setValue(this.observation);
		this.formObservations.controls.observation.markAsUntouched();
	}

	cancelEditMotive(): void {
		this.hideAbsentMotiveForm = true;
		this.formMotive.controls.motive.setValue(this.absentMotive);
	}

	copied() {
		this.snackBarService.showSuccess('messages.COPIED');
		setTimeout(() => {
		}, 300);
	}

	private updateSummaryCoverageData(): void {
		let summaryInfo: SummaryCoverageInformation = {};
		if (this.coverageData) {
			if (this.coverageData.medicalCoverage.name) {
				summaryInfo.name = this.coverageData.medicalCoverage.name;
			}
			if (this.coverageData.affiliateNumber) {
				summaryInfo.affiliateNumber = this.coverageData.affiliateNumber;
			}
			if (this.coverageData?.planName) {
				summaryInfo.plan = this.coverageData?.planName;
			}
			if (this.coverageData.condition) {
				summaryInfo.condition = this.coverageData.condition;
			}
			if (this.coverageData.medicalCoverage.cuit) {
				summaryInfo.cuit = this.coverageData.medicalCoverage.cuit;
			}
			if (this.coverageData.medicalCoverage.type) {
				summaryInfo.type = this.coverageData.medicalCoverage.type;
			}
		}
		this.summaryCoverageData = summaryInfo;
	}

	private generateEventsFromAppointments(appointments: AppointmentListDto[]): CalendarEvent[] {
		return appointments.map(appointment => {
			const from = getAppointmentStart(appointment.hour);
			const to = getAppointmentEnd(appointment.hour, this.data.agenda.appointmentDuration);
			return toCalendarEvent(from, to, dateISOParseDate(appointment.date), appointment);
		});
	}

	private setParentAppointment(parentAppointmentId?: number) {
		if (!parentAppointmentId) return;

		this.appointmentService.get(parentAppointmentId)
			.subscribe((parentAppointment: AppointmentDto) => this.parentAppointment = parentAppointment);
	}

	private isMedicalCoverage = () => {
		if (this.HABILITAR_VISTA_COBERTURA_TURNOS && this.coverageData?.medicalCoverage.id) {
			this.patientMedicalCoverageService.verifyMedicalCoverage(
				this.coverageData.medicalCoverage.id,
				this.data.agenda.healthcareProfessionalId
			).subscribe((response: ItsCoveredResponseDto) => this.setCoverage(response.message, response.covered === itsCovered.COVERED ? Color.GREEN : Color.RED))
		}
	}

	private setCoverage = (description: string, color: string) => {
		this.coverage = {
			description,
			color
		}
	}

}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		identificationNumber?: string,
		identificationTypeId?: number,
		typeId: number,
		genderId?: number,
		email?: string,
		names: {
			firstName: string,
			lastName: string,
			middleNames?: string,
			nameSelfDetermination?: string,
			otherLastNames?: string,
		}

	};
	appointmentId: number;
	appointmentStateId: number;
	date: Date;
	phonePrefix: string;
	phoneNumber: string;
	healthInsurance?: {
		name: string,
		acronym?: string;
	};
	medicalCoverageName: string;
	affiliateNumber: string;
	overturn: boolean;
	createdOn: Date;
	updatedOn: Date;
	professionalPersonDto: ProfessionalPersonDto;
}

interface Coverage {
	description: string;
	color: string
}

export enum DATESTYPES { DAY, MONTH, YEAR, MODALITY };
