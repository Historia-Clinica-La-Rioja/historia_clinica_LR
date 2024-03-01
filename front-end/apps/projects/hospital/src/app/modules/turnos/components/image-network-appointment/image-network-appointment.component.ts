import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AppFeature, AppointmentDto, ERole, PatientMedicalCoverageDto, PersonPhotoDto, CompleteEquipmentDiaryDto, UpdateAppointmentDto, AppointmentListDto, DiagnosticReportInfoDto, TranscribedServiceRequestSummaryDto, ApiErrorMessageDto } from '@api-rest/api-model.d';
import { VALIDATIONS, getError, hasError, processErrors, updateControlValidator } from '@core/utils/form.utils';
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
	switchMap,
	take,
} from 'rxjs/operators';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PermissionsService } from '@core/services/permissions.service';
import {
	EMPTY,
	Observable,
	forkJoin,
	of,
} from 'rxjs';
import { PatientNameService } from "@core/services/patient-name.service";
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
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
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVE } from '@turnos/constants/appointment';
import { NewAttentionComponent } from '@turnos/dialogs/new-attention/new-attention.component';
import { PatientAppointmentInformation } from '@turnos/dialogs/appointment/appointment.component';
import { EquipmentAppointmentsFacadeService } from '../../services/equipment-appointments-facade.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { CancelAppointmentComponent } from '@turnos/dialogs/cancel-appointment/cancel-appointment.component';
import { getStudiesNames, toCalendarEvent } from '../../utils/appointment.utils';
import { medicalOrderInfo } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { differenceInDays } from 'date-fns';
import { TranslateService } from '@ngx-translate/core';
import { toStudyLabel } from '../../../image-network/utils/study.utils';

const BELL_LABEL = 'Llamar paciente'
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
const ROLES_TO_EDIT: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
const MEDICAL_ORDER_PENDING_STATUS = '1';
const MEDICAL_ORDER_CATEGORY_ID = '363679005'
const ORDER_EXPIRED_DAYS = 30;
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
	medicalOrder: medicalOrderInfo;
	patientMedicalOrders: medicalOrderInfo[] = [];
	canCoverageBeEdited = false;
	canOrderBeEdited = false;
	hasCoverageAsociated = false;
	phoneNumber: string;
	summaryCoverageData: SummaryCoverageInformation = {};
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleToEdit$: Observable<boolean>;
	patientMedicalCoverages: PatientMedicalCoverage[];

	hideFilterPanel = false;
	hideAbsentMotiveForm = true;
	absentMotive: string;
	absentAppointment = false;

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

	nameSelfDeterminationFF = false;
	patientSummary: PatientSummary;
	transcribedLabelOrder:string

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
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly patientService: PatientService,
		private readonly imageDecoderService: ImageDecoderService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService,
		private prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService,
		private readonly patientNameService: PatientNameService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_LLAMADO).subscribe(isEnabled => this.isMqttCallEnabled = isEnabled);
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => this.nameSelfDeterminationFF = isOn);
	}

	ngOnInit(): void {
		this.formMotive = this.formBuilder.group({
			motive: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVE)]]
		});

		this.formEdit = this.formBuilder.group({
			medicalOrder: this.formBuilder.group({
				appointmentMedicalOrder: null
			}),
			//Medical Coverage selected in Edit Mode
			newCoverageData: null,
			phonePrefix: null,
			phoneNumber: null
		});

		this.getPatientMedicalOrders();
		this.setMedicalCoverages();
		this.formEdit.controls.phoneNumber.setValue(this.data.appointmentData.phoneNumber);
		this.formEdit.controls.phonePrefix.setValue(this.data.appointmentData.phonePrefix);
		if (this.data.appointmentData.phoneNumber) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
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

				if (this.hasMedicalOrder(appointment)) {
					appointment.orderData ? this.mapOrderToMedicalOrderInfo(appointment.orderData) : this.mapTranscribedOrderToMedicalOrderInfo(appointment.transcribedOrderData)
					this.patientMedicalOrders.unshift(this.medicalOrder)
				}

				this.absentAppointment = this.isMotiveRequired();
				this.absentMotive = this.appointment.stateChangeReason;
				if (this.absentMotive) {
					this.formMotive.controls.motive.setValue(this.absentMotive);
				}
				if ((this.appointment.patientMedicalCoverageId && this.data.appointmentData.patient?.id) || appointment.orderData?.coverageDto) {
					let coverageId = appointment.orderData?.coverageDto?.id || this.appointment.patientMedicalCoverageId;
					this.patientMedicalCoverageService.getPatientMedicalCoverage(coverageId)
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
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleToEdit$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).pipe(take(1));

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				const identificationType = identificationTypes.find(identificationType => identificationType.id == this.data.appointmentData.patient.identificationTypeId);
				this.patientSummary = {
					fullName: this.patientNameService.completeName(this.data.appointmentData.patient.names.firstName,this.data.appointmentData.patient.names.nameSelfDetermination,this.data.appointmentData.patient.names.lastName,this.data.appointmentData.patient.names.middleNames,this.data.appointmentData.patient.names.otherLastNames),
					id: this.data.appointmentData.patient.id,
					identification: {
						number: Number(this.data.appointmentData.patient.identificationNumber),
						type: identificationType.description
					}
				}
			});

		this.decodedPhoto$ = this.patientService.getPatientPhoto(this.data.appointmentData.patient.id).pipe(
			switchMap((personPhotoDto: PersonPhotoDto) => personPhotoDto?.imageData ? this.imageDecoderService.decode(personPhotoDto.imageData) : of('')))
	}

	private hasMedicalOrder(appointment: AppointmentDto): boolean {
		let hasOrder = (appointment.orderData || appointment.transcribedOrderData) ? true : false;
		return hasOrder;
	}

	private checkInputUpdatePermissions(orderHasCoverage?: boolean) {
		this.setEditionPermission(orderHasCoverage);
		this.changeInputUpdatePermissions();
	}

	private changeInputUpdatePermissions() {
		this.canCoverageBeEdited ? this.formEdit.get('newCoverageData').enable()
			: this.formEdit.get('newCoverageData').disable();
	}

	private setEditionPermission(orderHasCoverage?: boolean) {
		this.canCoverageBeEdited = this.isAssigned() && !orderHasCoverage;
		this.canOrderBeEdited = this.isAssigned();
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

	private getPatientMedicalOrders() {
		const prescriptions$ = this.prescripcionesService.getMedicalOrders(this.data.appointmentData.patient.id, MEDICAL_ORDER_PENDING_STATUS, MEDICAL_ORDER_CATEGORY_ID);
		const transcribedOrders$ = this.prescripcionesService.getTranscribedOrders(this.data.appointmentData.patient.id);
		forkJoin([prescriptions$, transcribedOrders$]).subscribe(masterdataInfo => {
			this.mapDiagnosticReportInfoDtoToMedicalOrderInfo(masterdataInfo[0]);
			this.mapTranscribeOrderToMedicalOrderInfo(masterdataInfo[1]);
		});
	}

	private mapOrderToMedicalOrderInfo(order: DiagnosticReportInfoDto) {
		let text = 'image-network.appointments.medical-order.ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			this.medicalOrder = {
				serviceRequestId: order.serviceRequestId,
				studyName: order.snomed.pt,
				studyId: order.id,
				displayText: `${translatedText} # ${order.serviceRequestId} - ${order.snomed.pt}`,
				isTranscribed: false,
				coverageDto: order.coverageDto,
			}
		})
	}

	private mapTranscribedOrderToMedicalOrderInfo(order: TranscribedServiceRequestSummaryDto) {
		let text = 'image-network.appointments.medical-order.TRANSCRIBED_ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			this.medicalOrder = {
				serviceRequestId: order.serviceRequestId,
				studyName: null,
				displayText: getStudiesNames(order.diagnosticReports.map(study => study.pt) , translatedText),
				isTranscribed: true
			}
			this.transcribedLabelOrder = toStudyLabel(this.medicalOrder.displayText)
		})
	}

	private mapDiagnosticReportInfoDtoToMedicalOrderInfo(patientMedicalOrders: DiagnosticReportInfoDto[]) {
		let text = 'image-network.appointments.medical-order.ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			patientMedicalOrders.map(diagnosticReportInfo => {
				if (differenceInDays(new Date(), new Date(diagnosticReportInfo.creationDate)) <= ORDER_EXPIRED_DAYS) {
					this.patientMedicalOrders.push({
						serviceRequestId: diagnosticReportInfo.serviceRequestId,
						studyName: diagnosticReportInfo.snomed.pt,
						studyId: diagnosticReportInfo.id,
						displayText: `${translatedText} # ${diagnosticReportInfo.serviceRequestId} - ${diagnosticReportInfo.snomed.pt}`,
						isTranscribed: false,
						coverageDto: diagnosticReportInfo.coverageDto,
					})
				}
			}).filter(value => value !== null && value !== undefined);
		});
	}

	private mapTranscribeOrderToMedicalOrderInfo(transcribedOrders: TranscribedServiceRequestSummaryDto[]) {
		let text = 'image-network.appointments.medical-order.TRANSCRIBED_ORDER';

		this.translateService.get(text).subscribe(translatedText => {
			transcribedOrders.map(medicalOrder => {
				this.patientMedicalOrders.push({
					serviceRequestId: medicalOrder.serviceRequestId,
					studyName: null,
					displayText: getStudiesNames(medicalOrder.diagnosticReports.map(study => study.pt) , translatedText),
					isTranscribed: true
				})
			}).filter(value => value !== null && value !== undefined);
		});
	}

	downloadMedicalOrder(medicalOrder: medicalOrderInfo){
		medicalOrder.isTranscribed ?
			this.prescripcionesService.downloadTranscribedOrderPdf(this.data.appointmentData.patient?.id, [medicalOrder.serviceRequestId], this.data.appointmentData.appointmentId)
			: this.prescripcionesService.downloadPrescriptionPdf(this.data.appointmentData.patient?.id, [medicalOrder.serviceRequestId], PrescriptionTypes.STUDY);
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
					content: `image-network.appointments.confirm-dialog.CONTENT`,
					okButtonLabel: `image-network.appointments.confirm-dialog.CONFIRM`,
					cancelButtonLabel: `image-network.appointments.confirm-dialog.CANCEL`,
					contentBold: `image-network.appointments.confirm-dialog.QUESTION`,
					showMatIconError: true,
				}
			});
		dialogRefConfirmation.afterClosed().subscribe((upDateState: boolean) => {
			if (upDateState) {
				this.updateState(newStateId);
				this.appointmentService.publishWorkList(this.data.appointmentData.appointmentId).subscribe();
			}
		});
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.selectedState !== newStateId) {
			this.checkIfAbsent(newStateId);
			if (this.selectedState === APPOINTMENT_STATES_ID.ASSIGNED && newStateId === APPOINTMENT_STATES_ID.CONFIRMED && this.coverageIsNotUpdate()) {
				this.confirmChangeState(newStateId);
			} else {
				this.updateState(newStateId);
			}
		}
	}

	private checkIfAbsent(newStateId: APPOINTMENT_STATES_ID) {
		this.absentAppointment = newStateId === APPOINTMENT_STATES_ID.ABSENT;
		this.hideAbsentMotiveForm = !(newStateId === APPOINTMENT_STATES_ID.ABSENT);
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	setHideAbsentMotiveForm(value: boolean): void {
		this.hideAbsentMotiveForm = value;
	}

	cancelEditMotive(): void {
		this.hideAbsentMotiveForm = true;
		this.formMotive.controls.motive.setValue(this.absentMotive);
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
			this.setMedicalOrder();
			this.saveCoverageData();
			if (this.formEdit.controls.phoneNumber.dirty || this.formEdit.controls.phonePrefix.dirty) {
				this.updatePhoneNumber(this.formEdit.controls.phonePrefix.value, this.formEdit.controls.phoneNumber.value);
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.formEdit.controls.phonePrefix.value, this.formEdit.controls.phoneNumber.value);
			}
			this.hideFilters();
		}
	}

	setAsociatedCoverageData(selectedOrder: medicalOrderInfo) {
		this.hasCoverageAsociated = selectedOrder?.coverageDto ? true : false;
		this.hasCoverageAsociated ? this.formEdit.controls.newCoverageData.setValue(selectedOrder.coverageDto.id) : null;
		this.checkInputUpdatePermissions(this.hasCoverageAsociated);
	}

	private saveCoverageData() {
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

	private setMedicalOrder() {
		this.medicalOrder = this.formEdit.get('medicalOrder').get('appointmentMedicalOrder').value;
		this.transcribedLabelOrder = toStudyLabel(this.medicalOrder.displayText)
		let parameters = {
			appointmentId: this.data.appointmentData.appointmentId,
			serviceRequestId: this.medicalOrder ? this.medicalOrder.serviceRequestId : null,
			isTranscribed: this.medicalOrder ? this.medicalOrder.isTranscribed : null,
			studyId: this.medicalOrder ?
				(!this.medicalOrder.isTranscribed ? this.medicalOrder.studyId : null) : null
		}
		this.appointmentService.updateAppointmentMedicalOrder(parameters.appointmentId, parameters.serviceRequestId, parameters.studyId, parameters.isTranscribed).subscribe();
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

	private setCoverageText(coverageData: PatientMedicalCoverage | PatientMedicalCoverageDto) {
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
						values.patientMedicalCoverages.map(pCoverage => this.mapperService.toPatientMedicalCoverageDto(pCoverage));

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

	callPatient(): void {
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
		this.formEdit.controls.medicalOrder.get('appointmentMedicalOrder').setValue(this.medicalOrder)
		this.setAsociatedCoverageData(this.medicalOrder);
		if (!this.hasCoverageAsociated) {
			this.formEdit.controls.newCoverageData.setValue(this.coverageData?.id);
		}
		this.formEdit.controls.phonePrefix.setValue(this.data.appointmentData.phonePrefix);
		this.formEdit.controls.phoneNumber.setValue(this.data.appointmentData.phoneNumber);
	}

	closeDialog(returnValue?: string) {
		if (!returnValue && (this.appointment.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || this.appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED))
			this.medicalCoverageInfo.setAppointmentMCoverage(this.summaryCoverageData);
		const appointmentInformation = { returnValue: returnValue, date: this.selectedDate };
		this.dialogRef.close(appointmentInformation);
	}

	clearCoverageData(): void {
		this.formEdit.controls.newCoverageData.setValue(null);
	}

	private updateSummaryCoverageData(): void {
		let summaryInfo: SummaryCoverageInformation = {}
		if (this.coverageData) {
			summaryInfo = {
				name: this.coverageData.medicalCoverage?.name,
				affiliateNumber: this.coverageData.affiliateNumber,
				plan: this.coverageData?.planName,
				condition: this.coverageData.condition,
				cuit: this.coverageData.medicalCoverage?.cuit,
				type: this.coverageData.medicalCoverage?.type
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

	getAppointmentTicketReport(): void {
		const isTranscribed: boolean = !!(this.medicalOrder && this.medicalOrder.isTranscribed)
		this.appointmentService.getAppointmentImageTicketPdf(this.data.appointmentData.appointmentId, isTranscribed).subscribe((pdf) => {
			const file = new Blob([pdf], { type: 'application/pdf' });
			const blobUrl = URL.createObjectURL(file);
			const div = document.querySelector("#pdfPrinter");
			const iframe = document.createElement("iframe");
			iframe.setAttribute("src", blobUrl);
			div.appendChild(iframe);
			iframe.contentWindow.print();
		});
	}
}
