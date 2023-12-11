import { MedicalCoverageInfoService } from './../../../historia-clinica/modules/ambulatoria/services/medical-coverage-info.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ContextService } from '@core/services/context.service';
import {
	APPOINTMENT_STATES_ID,
	getAppointmentState,
	getDiaryLabel,
	MAX_LENGTH_MOTIVE,
	modality,
	MODALITYS_TYPES,
} from '../../constants/appointment';
import {
	ApiErrorMessageDto,
	AppFeature,
	AppointmentDto,
	AppointmentListDto,
	CompleteDiaryDto,
	DateDto,
	DateTimeDto,
	DiaryOpeningHoursFreeTimesDto,
	EAppointmentModality,
	DiaryLabelDto,
	ERole,
	FreeAppointmentSearchFilterDto,
	PatientMedicalCoverageDto,
	PersonPhotoDto,
	ProfessionalPersonDto,
	TimeDto,
	UpdateAppointmentDateDto,
	UpdateAppointmentDto,
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
import { PatientService } from '@api-rest/services/patient.service';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { CalendarEvent } from 'angular-calendar';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { DateFormat, momentFormat, momentParseDate, momentParseTime } from '@core/utils/moment.utils';
import * as moment from 'moment';
import { Color } from '@presentation/colored-label/colored-label.component';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { toCalendarEvent } from '@turnos/utils/appointment.utils';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { DiaryLabelService } from '@api-rest/services/diary-label.service';
import { Router } from '@angular/router';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { dateTimeDtoToDate, dateToDateDto, dateToTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { DiaryService } from '@api-rest/services/diary.service';

import { PatientNameService } from '@core/services/patient-name.service';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';

const TEMPORARY_PATIENT = 3;
const REJECTED_PATIENT = 6;
const BELL_LABEL = 'Llamar paciente'
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const ROLES_TO_EDIT: ERole[]
	= [ERole.ADMINISTRATIVO];
const ROLE_TO_DOWNDLOAD_REPORTS: ERole[] = [ERole.ADMINISTRATIVO];
const ROLE_TO_MAKE_VIRTUAL_CONSULTATION: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const MONTHS = [1,2,3,4,5,6,7,8,9,10,11,12];

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {
	readonly SECOND_OPINION_VIRTUAL_ATTENTION = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION;
	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly TEMPORARY_PATIENT = TEMPORARY_PATIENT;
	readonly BELL_LABEL = BELL_LABEL;
	readonly Color = Color;
	modalitys : modality [] = [];
	datestypes = DATESTYPES;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;
	medicalCoverageId: number;

	personPhoto: PersonPhotoDto;
	decodedPhoto$: Observable<string>;

	appointment: AppointmentDto;
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
	agendaOwner : boolean;
	attachProfessional : boolean;
	patientMedicalCoverages: PatientMedicalCoverage[];

	hideFilterPanel = false;

	isDateFormVisible = false;
	startAgenda = dateToDateDto(new Date());
	endAgenda = dateToDateDto(new Date(this.data.agenda.endDate)).year;
	availableDays: number[]= [];
	availableMonths: number[] = [];
	availableYears: number[] = [];
	possibleScheduleHours: TimeDto[] = [];
	selectedDate = new Date(this.data.appointmentData.date); 
	isCheckedDownloadAnexo = false;
	isCheckedDownloadFormulario = false;
	downloadReportIsEnabled: boolean;
	isMqttCallEnabled = false;

	hideObservationForm = true;
	hideObservationTitle = true;
	observation: string;
	firstCoverage: number;
	canCoverageBeEdited = false;

	isRejectedPatient: boolean = false;
	selectedModality: modality;
	isVirtualConsultationModality: boolean = false;
	canDownloadReport = false;
	dateAppointment : DateDto;
	viewInputEmail: boolean = false;
	selectedOpeningHourId: number;

	isLabelSelectorVisible: boolean = false;
	diaryLabels: DiaryLabelDto[] = [];
	formLabel: UntypedFormGroup;
	selectedDiaryLabelId: number;

	patientSummary: PatientSummary;
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
		private readonly patientService: PatientService,
		private readonly imageDecoderService: ImageDecoderService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService,
		private readonly jitsiCallService: JitsiCallService,
		private readonly router: Router,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly diaryLabelService: DiaryLabelService,
		private readonly patientNameService: PatientNameService,
		private readonly diaryService: DiaryService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES).subscribe(isOn => this.downloadReportIsEnabled = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_LLAMADO).subscribe(isEnabled => this.isMqttCallEnabled = isEnabled);
	}

	ngOnInit(): void {
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
			month: ['',Validators.required],
			year: ['',Validators.required],
			modality: ['',[Validators.required]],
			email: [''],
		});

		this.formObservations = this.formBuilder.group({
			observation: ['', [Validators.required]]
		});

		this.formLabel = this.formBuilder.group({
			color: [null],
			description: [null]
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
				this.appointment = appointment;
				this.observation = appointment.observation;
				if (this.observation) {
					this.hideObservationTitle = false;
					this.formObservations.controls.observation.setValue(this.observation);
				}
				this.selectedState = this.appointment?.appointmentStateId;
				if (this.appointment.stateChangeReason) {
					this.formMotive.controls.motive.setValue(this.appointment.stateChangeReason);
				}
				if (this.appointment.patientMedicalCoverageId && this.data.appointmentData.patient?.id) {
					this.patientMedicalCoverageService.getPatientMedicalCoverage(this.appointment.patientMedicalCoverageId)
						.subscribe(coverageData => {
							if (coverageData) {
								this.coverageData = this.mapperService.toPatientMedicalCoverage(coverageData);
								this.updateSummaryCoverageData();
								this.formEdit.controls.newCoverageData.setValue(coverageData.id);
								this.firstCoverage = coverageData.id;
								this.setCoverageText(coverageData);
							}
						});
				}
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.data.appointmentData.phonePrefix, this.data.appointmentData.phoneNumber);
				this.checkInputUpdatePermissions();
				this.selectedModality = MODALITYS_TYPES.find( m => m.value === this.appointment.modality);
				this.modalitys.push(MODALITYS_TYPES[0]);
				this.modalitys.push(MODALITYS_TYPES[1]);
				if(this.selectedModality.value ===  this.SECOND_OPINION_VIRTUAL_ATTENTION){
					this.modalitys.push(MODALITYS_TYPES[2])
				}
				if(this.selectedModality.value ===  this.SECOND_OPINION_VIRTUAL_ATTENTION || this.selectedModality.value === EAppointmentModality.PATIENT_VIRTUAL_ATTENTION){
					this.isVirtualConsultationModality = true;
				}
				if(this.appointment.patientEmail){
					this.formDate.controls.email.setValue(this.appointment.patientEmail);
				}
				
				this.checkDownloadReportAvailability();

				if (appointment.diaryLabelDto) {
					const diaryLabel: DiaryLabelDto = {
						colorId: appointment.diaryLabelDto.colorId,
						description: appointment.diaryLabelDto.description,
						id: appointment.diaryLabelDto.id,
						diaryId: appointment.diaryLabelDto.diaryId
					}
					this.setSelectedDiaryLabel(diaryLabel);
				}
				this.initializeFormDate();
				this.loadAvailableDays(this.dateAppointment,true);
				this.setAvailableMonths(this.dateAppointment);
				this.loadAppointmentsHours(this.dateAppointment,this.isToday(),true);
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
			this.attachProfessional= (this.data.agenda.associatedProfessionalsInfo.find(professional => professional.id === healthcareProfessionalId) && hasRole) 
		});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				const identificationType = identificationTypes.find(identificationType => identificationType.id == this.data.appointmentData.patient.identificationTypeId);
				this.patientSummary = {
					fullName: this.patientNameService.completeName(this.data.appointmentData.patient.names.firstName, this.data.appointmentData.patient.names.nameSelfDetermination, this.data.appointmentData.patient.names.lastName, this.data.appointmentData.patient.names.middleNames, this.data.appointmentData.patient.names.otherLastNames),
					id: this.data.appointmentData.patient.id,
					identification: {
						number: Number(this.data.appointmentData.patient.identificationNumber),
						type: identificationType.description
					}
				}
			});

		this.patientService.getPatientPhoto(this.data.appointmentData.patient.id)
			.subscribe((personPhotoDto: PersonPhotoDto) => {
				this.personPhoto = personPhotoDto;
				if (personPhotoDto?.imageData) {
					this.decodedPhoto$ = this.imageDecoderService.decode(personPhotoDto.imageData);
				}
			});
		this.setDiaryLabels();
	}

	initializeFormDate(){
		const date = new Date(this.appointment.date)
		date.setMinutes(date.getMinutes() + date.getTimezoneOffset())
		this.dateAppointment = dateToDateDto(date)
		this.formDate.controls.day.setValue(this.dateAppointment.day);
		this.formDate.controls.month.setValue(this.dateAppointment.month);
		this.formDate.controls.year.setValue(this.dateAppointment.year);
		this.formDate.controls.modality.setValue(this.selectedModality.value);
	}

	private setDiaryLabels() {
		this.diaryLabelService.getLabelsByDiary(this.data.agenda.id)
			.subscribe((result: DiaryLabelDto[]) => this.diaryLabels = result);
	}

	updateLabel(value?: DiaryLabelDto) {
		this.appointmentService.setAppointmentLabel(value ? value.id: null, this.data.appointmentData.appointmentId)
		.subscribe({
			next: (result: boolean) => {
				if (result) {
					this.snackBarService.showSuccess('turnos.appointment.labels.UPDATED_LABEL');
					this.setSelectedDiaryLabel(value);
				}
			},
			error: () => this.snackBarService.showError('turnos.appointment.labels.ERROR_UPDATE_LABEL')
		})
	}

	private setSelectedDiaryLabel(diaryLabel?: DiaryLabelDto) {
		this.isLabelSelectorVisible = diaryLabel ? true : false;
		this.selectedDiaryLabelId = diaryLabel ? diaryLabel.id: null;
		this.formLabel.get('color').setValue(diaryLabel ? getDiaryLabel(diaryLabel.colorId): null);
		this.formLabel.get('description').setValue(diaryLabel ? diaryLabel.description: null);
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
	}

	cancelDateForm(): void {
		this.formDate.reset();
		this.initializeFormDate();
		this.dateFormToggle();
	}

	openDateForm(): void {
		this.dateFormToggle();
	}

	isToday():boolean{
		return (this.startAgenda.year === this.dateAppointment.year && this.startAgenda.month === this.dateAppointment.month && this.startAgenda.day === this.dateAppointment.day )
	}

	selectDate(dateType: DATESTYPES) {
		switch (dateType){
			case DATESTYPES.DAY :
				this.dateAppointment.day = this.formDate.controls.day.value;
				this.loadAppointmentsHours(this.dateAppointment,this.isToday());
				break;
			case DATESTYPES.MONTH :
				this.dateAppointment.month = this.formDate.controls.month.value;
				if(this.startAgenda.month === this.dateAppointment.month && this.startAgenda.year === this.dateAppointment.year){
					this.dateAppointment.day = this.startAgenda.day;
				}else{
					this.dateAppointment.day = 1;
				}
				this.loadAvailableDays(this.dateAppointment);
				this.possibleScheduleHours = [];
				this.formDate.controls.day.setValue(null);
				this.formDate.controls.hour.setValue(null);
				break;
			case DATESTYPES.YEAR : 
				this.dateAppointment.year = this.formDate.controls.year.value;
				this.startAgenda.year = this.dateAppointment.year;
				const now = dateToDateDto(new Date());
				if(now.year === this.dateAppointment.year){
					this.dateAppointment.month = now.month;
				}else{
					this.dateAppointment.month = 1;
				}
				this.setAvailableMonths(this.dateAppointment);
				this.formDate.controls.day.setValue(null);
				this.formDate.controls.hour.setValue(null);
				this.formDate.controls.month.setValue(null);
				break;
		}
	}

	setAvailableDays(arr: any[], isInitial?:boolean) {
		this.availableDays = [];
		arr.forEach(element => {
			if (!this.availableDays.includes(element.day))
				this.availableDays.push(element.day);
		});
		if(this.selectedDate.getUTCDate() === this.dateAppointment.day && isInitial && !this.availableDays.includes(this.dateAppointment.day)){
			this.availableDays.push(this.dateAppointment.day);
		}
		this.availableDays.sort((a, b) => a - b);
	}

	setAvailableMonths(date:DateDto){
		this.availableMonths = [];
		MONTHS.forEach(m =>{
			if(m >= date.month){
				this.availableMonths.push(m);
			}
		})
	}

	setAvailableYears(){
		 for (var i = this.startAgenda.year; i <= this.endAgenda; i++) {
			this.availableYears.push(i);
	  	 }
	}

	setModalityAndValidator(change:boolean){
		switch (this.formDate.controls.modality.value) {
			case EAppointmentModality.PATIENT_VIRTUAL_ATTENTION : 
				updateControlValidator(this.formDate, 'email', [Validators.required]);
				this.viewInputEmail = true;
				break;
			case EAppointmentModality.ON_SITE_ATTENTION : 
				updateControlValidator(this.formDate, 'email', []);
				this.viewInputEmail = false;
				break;
		}
		if(change){
			this.selectDate(DATESTYPES.MONTH);
		}
	}

	setDefaultTime(){
		let appointmentHour: TimeDto = dateToTimeDto(new Date()) ;
		let partes = this.appointment.hour.split(':');
		appointmentHour.hours = parseInt(partes[0]);
		appointmentHour.minutes = parseInt(partes[1]);
		appointmentHour.seconds = parseInt(partes[2]);
		this.possibleScheduleHours.push(appointmentHour);
		this.possibleScheduleHours.sort((a, b) => a.hours - b.hours || a.minutes - b.minutes);
		this.formDate.controls.hour.setValue(appointmentHour);
	}

	loadAppointmentsHours(date: DateDto,isToday:boolean,isInitial?:boolean) {
		this.possibleScheduleHours = [];
		this.selectedOpeningHourId = null;
		const searchCriteria = this.prepareSearchCriteria(date);
	 	this.diaryService.getDailyFreeAppointmentTimes(this.data.agenda.id,searchCriteria).subscribe((times: DiaryOpeningHoursFreeTimesDto[]) => {
			if(times.length){
				if(isToday){
					const now = new Date();
					this.possibleScheduleHours = times[0].freeTimes.filter(({ hours, minutes }) => {
						return hours > now.getHours() || (hours === now.getHours() && minutes > now.getMinutes());
					  });
				}else{
					this.possibleScheduleHours = times[0].freeTimes;
					if(isInitial){
						this.setDefaultTime();
					}
				}
				this.selectedOpeningHourId = times[0].openingHoursId;
			}
		})
	}

	prepareSearchCriteria(dateSelect: DateDto): FreeAppointmentSearchFilterDto {
		const searchCriteria: FreeAppointmentSearchFilterDto = {
			date: dateSelect,
			modality: this.formDate.controls.modality.value,
			mustBeProtected: this.appointment.protected,
		}
		return searchCriteria;
	}

	loadAvailableDays(date: DateDto, isInitial?:boolean): void {
		const searchCriteria = this.prepareSearchCriteria(date);
		this.diaryService.getMonthlyFreeAppointmentDates(this.data.agenda.id, searchCriteria).subscribe(res => {
			this.setAvailableDays(res,isInitial);
		},error => {
			this.availableDays= [];
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		})
	}

	updateAppointmentOverturn(appointmentId: number, appointmentStateId: number, overturn: boolean, patientId: number): void {
		const appointment: UpdateAppointmentDto = {
			appointmentId: appointmentId,
			appointmentStateId: appointmentStateId,
			overturn: overturn,
			patientId: patientId,
		}
		this.appointmentFacade.updateAppointment(appointment).subscribe(() => { },
			error => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			});
	}
	
	updateAppointmentDate(): void {
		const previousDate = new Date(this.data.appointmentData.date);
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
		const updateAppointmentDate: UpdateAppointmentDateDto = {
			appointmentId: this.data.appointmentData.appointmentId,
			date: dateNow,
			openingHoursId: this.selectedOpeningHourId,
			modality: this.formDate.controls.modality.value,
			patientEmail: this.formDate.controls.email.value,
		};

		this.appointmentFacade.updateDate(updateAppointmentDate).subscribe(() => {
			const date = momentFormat(moment(previousDate), DateFormat.API_DATE);
			this.appointmentService.getList([this.data.agenda.id], this.data.agenda.healthcareProfessionalId, date, date)
				.subscribe((appointments: AppointmentListDto[]) => {
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

					if (this.data.appointmentData.overturn) {
						this.updateAppointmentOverturn(
							this.data.appointmentData.appointmentId,
							this.data.appointmentData.appointmentStateId,
							false,
							this.data.appointmentData.patient.id
						);
					}
					this.snackBarService.showSuccess('turnos.appointment.date.UPDATE_SUCCESS');
					this.selectedDate = dateTimeDtoToDate(dateNow);
					this.data.appointmentData.date = this.selectedDate;
					this.selectedModality = this.modalitys.find( m => m.value === updateAppointmentDate.modality);
					const appointmentUpdate = appointments.find(a => a.id = this.data.appointmentData.appointmentId);
					this.appointment.protected = appointmentUpdate?.protected;
				});
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
		this.dateFormToggle();
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
				this.updateState(newStateId);
		});
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.selectedState !== newStateId) {
			if (this.selectedState === APPOINTMENT_STATES_ID.ASSIGNED && newStateId === APPOINTMENT_STATES_ID.CONFIRMED && this.coverageIsNotUpdate()) {
				this.confirmChangeState(newStateId);
			} else {
				this.updateState(newStateId);
			}
		}
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	cancelAppointment(): void {
		const dialogRefCancelAppointment = this.dialog.open(CancelAppointmentComponent, {
			data: {
				appointmentId: this.data.appointmentData.appointmentId
			}
		});
		dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				const date = momentFormat(moment(this.data.appointmentData.date), DateFormat.API_DATE);
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
		this.appointmentFacade.changeState(this.data.appointmentData.appointmentId, newStateId, motive)
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
			const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
			let to = momentParseTime(from).add(this.data.agenda.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
			if (from > to) {
				to = momentParseTime(from).set({ hour: 23, minute: 59 }).format(DateFormat.HOUR_MINUTE);
			}
			const calendarEvent = toCalendarEvent(from, to, momentParseDate(appointment.date), appointment);
			return calendarEvent;
		});
	}

}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		identificationNumber?: string,
		identificationTypeId?: number,
		typeId: number,
		genderId?: number,
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
	professionalPersonDto: ProfessionalPersonDto;
}
export enum DATESTYPES {DAY,MONTH,YEAR,MODALITY};
