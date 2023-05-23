import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AppFeature, AppointmentDto, ERole, IdentificationTypeDto, PatientMedicalCoverageDto, PersonPhotoDto, CompleteEquipmentDiaryDto, UpdateAppointmentDto, AppointmentListDto } from '@api-rest/api-model.d';
import { VALIDATIONS, getError, hasError, processErrors, updateControlValidator } from '@core/utils/form.utils';
import { MapperService } from '@core/services/mapper.service';
import {
	determineIfIsHealthInsurance,
	HealthInsurance,
	MedicalCoverageComponent,
	PatientMedicalCoverage,
	PrivateHealthInsurance
} from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { map, take } from 'rxjs/operators';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PermissionsService } from '@core/services/permissions.service';
import { Observable } from 'rxjs';
import { PatientNameService } from "@core/services/patient-name.service";
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
import { MedicalCoverageInfoService } from '@historia-clinica/modules/ambulatoria/services/medical-coverage-info.service';
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVE} from '@turnos/constants/appointment';
import { NewAttentionComponent } from '@turnos/dialogs/new-attention/new-attention.component';
import { PatientAppointmentInformation } from '@turnos/dialogs/appointment/appointment.component';
import { EquipmentAppointmentsFacadeService } from '../../services/equipment-appointments-facade.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { CancelAppointmentComponent } from '@turnos/dialogs/cancel-appointment/cancel-appointment.component';
import { toCalendarEvent } from '../../utils/appointment.utils';

const BELL_LABEL = 'Llamar paciente'
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
const ROLES_TO_EDIT: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];

@Component({
  selector: 'app-image-network-appointment',
  templateUrl: './image-network-appointment.component.html',
  styleUrls: ['./image-network-appointment.component.scss']
})
export class ImageNetworkAppointmentComponent implements OnInit {
	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly BELL_LABEL = BELL_LABEL;
	readonly Color = Color;
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
	institutionId = this.contextService.institutionId;
	coverageText: string;
	coverageNumber: any;
	coverageCondition: string;
	coverageData: PatientMedicalCoverage;
	phoneNumber: string;
	summaryCoverageData: SummaryCoverageInformation = {};
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleToEdit$: Observable<boolean>;
	patientMedicalCoverages: PatientMedicalCoverage[];
	identificationType: IdentificationTypeDto;

	hideFilterPanel = false;

	isDateFormVisible = false;
	startAgenda = moment();
	endAgenda = momentParseDate(this.data.agenda.endDate);
	availableDays: number[] = [];
	disableDays: Date[] = [];
	openingDateForm = false;
	possibleScheduleHours: Date[] = [];
	selectedDate = new Date(this.data.appointmentData.date);

	isMqttCallEnabled = false;
	firstCoverage: number;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			appointmentData: PatientAppointmentInformation,
			hasPermissionToAssignShift: boolean,
			agenda: CompleteEquipmentDiaryDto
		},
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly equipmentAppointmensFacade: EquipmentAppointmentsFacadeService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly patientNameService: PatientNameService,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly patientService: PatientService,
		private readonly imageDecoderService: ImageDecoderService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_LLAMADO).subscribe(isEnabled => this.isMqttCallEnabled = isEnabled);
	}

	ngOnInit(): void {
		this.formMotive = this.formBuilder.group({
			motive: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVE)]]
		});

		this.formEdit = this.formBuilder.group({
			//Medical Coverage selected in Edit Mode
			newCoverageData: null,
			phonePrefix: null,
			phoneNumber: null
		});

		this.setMedicalCoverages();
		this.formEdit.controls.phoneNumber.setValue(this.data.appointmentData.phoneNumber);
		this.formEdit.controls.phonePrefix.setValue(this.data.appointmentData.phonePrefix);
		if (this.data.appointmentData.phoneNumber) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER) ,Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER) ,Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
		}

		this.data.agenda.equipmentDiaryOpeningHours.forEach(DOH => {
			let day = DOH.openingHours.dayWeekId;
			if (!this.availableDays.includes(day))
				this.availableDays.push(day);
		});

		this.appointmentService.getAppointmentEquipment(this.data.appointmentData.appointmentId)
			.subscribe(appointment => {

				this.appointment = appointment;
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
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleToEdit$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).pipe(take(1));

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationType = identificationTypes.find(identificationType => identificationType.id == this.data.appointmentData.patient.identificationTypeId);
			});

		this.patientService.getPatientPhoto(this.data.appointmentData.patient.id)
			.subscribe((personPhotoDto: PersonPhotoDto) => {
				this.personPhoto = personPhotoDto;
				if (personPhotoDto?.imageData) {
					this.decodedPhoto$ = this.imageDecoderService.decode(personPhotoDto.imageData);
				}
			});
	}

	formatPhonePrefixAndNumber(phonePrefix: string, phoneNumber: string): string {
		return phoneNumber ? phonePrefix
			? "(" + phonePrefix + ") " + phoneNumber
			: phoneNumber
			: null;
	}

	updatePhoneValidators() {
		if (this.formEdit.controls.phoneNumber.value || this.formEdit.controls.phonePrefix.value) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER) ,Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER) ,Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
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
			if (upDateState){
				this.updateState(newStateId);
				this.appointmentService.publishWorkList(this.data.appointmentData.appointmentId).subscribe();
			}
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
				appointmentId: this.data.appointmentData.appointmentId,
				imageNetworkAppointment: true
			}
		});
		dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				const date = momentFormat(moment(this.data.appointmentData.date), DateFormat.API_DATE);
				this.appointmentService.getList([this.data.agenda.id], this.data.agenda.equipmentId, date, date)
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

	updateAppointmentOverturn(appointmentId: number, appointmentStateId: number, overturn: boolean, patientId: number): void {
		const appointment: UpdateAppointmentDto = {
			appointmentId: appointmentId,
			appointmentStateId: appointmentStateId,
			overturn: overturn,
			patientId: patientId,
		}
		this.equipmentAppointmensFacade.updateAppointment(appointment).subscribe(() => { },
			error => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
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

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motive?: string): void {
		this.equipmentAppointmensFacade.changeState(this.data.appointmentData.appointmentId, newStateId, motive)
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
		this.equipmentAppointmensFacade.updatePhoneNumber(this.data.appointmentData.appointmentId, phonePrefix, phoneNumber).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
			this.formEdit.controls.phonePrefix.setValue(phonePrefix);
			this.formEdit.controls.phoneNumber.setValue(phoneNumber);
			this.data.appointmentData.phonePrefix = phonePrefix;
			this.data.appointmentData.phoneNumber = phoneNumber;
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
		this.appointmentService.mqttCall(this.data.appointmentData.appointmentId).subscribe();
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

	viewPatientName(appointmentInformation: PatientAppointmentInformation): string {
		return this.patientNameService.getPatientName(appointmentInformation.patient.fullName, appointmentInformation.patient.fullNameWithNameSelfDetermination);
	}

	clear(): void {
		this.formEdit.controls.newCoverageData.setValue(null);
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
