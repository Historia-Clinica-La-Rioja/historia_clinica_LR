import { MedicalCoverageInfoService } from './../../../historia-clinica/modules/ambulatoria/services/medical-coverage-info.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVO } from '../../constants/appointment';
import { ContextService } from '@core/services/context.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppFeature, AppointmentDto, CompleteDiaryDto, ERole, IdentificationTypeDto, PatientMedicalCoverageDto, PersonPhotoDto } from '@api-rest/api-model.d';
import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';
import { getError, hasError, processErrors, updateControlValidator } from '@core/utils/form.utils';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
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
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { PersonMasterDataService } from "@api-rest/services/person-master-data.service";
import { SummaryCoverageInformation } from '@historia-clinica/modules/ambulatoria/components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { PatientService } from '@api-rest/services/patient.service';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { momentParseDate } from '@core/utils/moment.utils';


const TEMPORARY_PATIENT = 3;
const BELL_LABEL = 'Llamar paciente'
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const ROLES_TO_EDIT: ERole[]
	= [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const ROLE_TO_DOWNDLOAD_REPORTS: ERole[] = [ERole.ADMINISTRATIVO];

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly TEMPORARY_PATIENT = TEMPORARY_PATIENT;
	readonly BELL_LABEL = BELL_LABEL;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;
	medicalCoverageId: number;

	personPhoto: PersonPhotoDto;
	decodedPhoto$: Observable<string>;

	appointment: AppointmentDto;
	estadoSelected: APPOINTMENT_STATES_ID;
	formMotivo: FormGroup;
	formEdit: FormGroup;
	formDate: FormGroup;
	formObservations: FormGroup;
	institutionId = this.contextService.institutionId;
	coverageText: string;
	coverageNumber: any;
	coverageCondition: string;
	coverageData: PatientMedicalCoverage;
	phoneNumber: string;
	summaryCoverageData: SummaryCoverageInformation = {};
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleToEditPhoneNumber$: Observable<boolean>;
	hasRoleToDownloadReports$: Observable<boolean>;
	hasRoleToAddObservations$: Observable<boolean>;
	patientMedicalCoverages: PatientMedicalCoverage[];
	identificationType: IdentificationTypeDto;

	hideFilterPanel = false;

	isDateFormVisible = false;
	startAgenda = new Date();
	endAgenda = new Date(this.params.agenda.endDate);
	availableDays: number[] = [];

	isCheckedDownloadAnexo = false;
	isCheckedDownloadFormulario = false;
	downloadReportIsEnabled: boolean;
	isMqttCallEnabled = false;
	
	hideObservationForm = true;
	hideObservationTitle = true;
	observation: string;

	constructor(
		@Inject(MAT_DIALOG_DATA) public params: { appointmentData: PatientAppointmentInformation, hasPermissionToAssignShift: boolean, agenda: CompleteDiaryDto },
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly formBuilder: FormBuilder,
		private readonly appointmentFacade: AppointmentsFacadeService,
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
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES).subscribe(isOn => this.downloadReportIsEnabled = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_LLAMADO).subscribe(isEnabled => this.isMqttCallEnabled = isEnabled);
	}

	ngOnInit(): void {

		this.formMotivo = this.formBuilder.group({
			motivo: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVO)]]
		});

		this.formEdit = this.formBuilder.group({
			//Medical Coverage selected in Edit Mode
			newCoverageData: null,
			phonePrefix: null,
			phoneNumber: null
		});

		this.formDate = this.formBuilder.group({
			date: [this.params.appointmentData.date,[Validators.required]],
			hour: ['',[Validators.required]]
		});

		this.formObservations = this.formBuilder.group({
			observation: ['',[Validators.required]]
		});

		this.setMedicalCoverages();
		this.formEdit.controls.phoneNumber.setValue(this.params.appointmentData.phoneNumber);
		this.formEdit.controls.phonePrefix.setValue(this.params.appointmentData.phonePrefix);
		if (this.params.appointmentData.phoneNumber) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.maxLength(20)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.maxLength(10)]);
		}

		this.formDate.controls.hour.setValue(this.params.appointmentData.date);

		this.appointmentService.get(this.params.appointmentData.appointmentId)
			.subscribe(appointment => {
				this.appointment = appointment;
				this.observation = appointment.observation;

				if(this.observation){
					this.hideObservationTitle = false;
					this.formObservations.controls.observation.setValue(this.observation);
				}
				this.estadoSelected = this.appointment?.appointmentStateId;
				if (this.appointment.stateChangeReason) {
					this.formMotivo.controls.motivo.setValue(this.appointment.stateChangeReason);
				}
				if (this.appointment.patientMedicalCoverageId && this.params.appointmentData.patient?.id) {
					this.patientMedicalCoverageService.getPatientMedicalCoverage(this.appointment.patientMedicalCoverageId)
						.subscribe(coverageData => {
							if (coverageData) {
								this.coverageData = this.mapperService.toPatientMedicalCoverage(coverageData);
								this.updateSummaryCoverageData();
								this.formEdit.controls.newCoverageData.setValue(coverageData.id);
								this.setCoverageText(coverageData);
							}
						});
				}
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.params.appointmentData.phonePrefix,this.params.appointmentData.phoneNumber);
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleToEditPhoneNumber$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).pipe(take(1));

		this.hasRoleToDownloadReports$ = this.permissionsService.hasContextAssignments$(ROLE_TO_DOWNDLOAD_REPORTS).pipe(take(1));

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationType = identificationTypes.find(identificationType => identificationType.id == this.params.appointmentData.patient.identificationTypeId);
			});

		this.patientService.getPatientPhoto(this.params.appointmentData.patient.id)
			.subscribe((personPhotoDto: PersonPhotoDto) => {
				this.personPhoto = personPhotoDto;
				if (personPhotoDto?.imageData) {
					this.decodedPhoto$ = this.imageDecoderService.decode(personPhotoDto.imageData);
				}
			});

		for (let i = 0; i < this.params.agenda.diaryOpeningHours.length; i++) {
			let day = this.params.agenda.diaryOpeningHours[i].openingHours.dayWeekId;
			if(!this.availableDays.includes(day))
				this.availableDays.push(day);
		}
		console.log(this.availableDays);
	}

	formatPhonePrefixAndNumber(phonePrefix: string, phoneNumber: string): string {
		return phoneNumber ? phonePrefix
			? "(" + phonePrefix + ") " + phoneNumber
			: phoneNumber
			: "Sin información";
	}

	updatePhoneValidators() {
		if (this.formEdit.controls.phoneNumber.value || this.formEdit.controls.phonePrefix.value) {
			updateControlValidator(this.formEdit, 'phoneNumber', [Validators.required, Validators.maxLength(20)]);
			updateControlValidator(this.formEdit, 'phonePrefix', [Validators.required, Validators.maxLength(10)]);
		} else {
			updateControlValidator(this.formEdit, 'phoneNumber', []);
			updateControlValidator(this.formEdit, 'phonePrefix', []);
		}
	}

	private setMedicalCoverages(): void {
		if (this.params.appointmentData.patient?.id) {
			this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.params.appointmentData.patient.id))
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
		this.estadoSelected = newStateId;
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.estadoSelected !== newStateId) {
			this.changeState(newStateId);
			if (this.isANewState(newStateId) && !this.isMotivoRequired()) {
				this.submitNewState(newStateId);
			}
		}
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	cancelAppointment(): void {
		const dialogRefCancelAppointment = this.dialog.open(CancelAppointmentComponent, {
			data: this.params.appointmentData.appointmentId
		});
		dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				this.closeDialog('statuschanged');
			}
		});
	}

	saveAbsent(): void {
		if (this.formMotivo.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotivo.value.motivo);
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
				this.phoneNumber = this.formatPhonePrefixAndNumber(this.formEdit.controls.phonePrefix.value ,this.formEdit.controls.phoneNumber.value);
			}
			this.hideFilters();
		}
	}

	isMotivoRequired(): boolean {
		return this.estadoSelected === APPOINTMENT_STATES_ID.ABSENT;
	}

	isAssigned(): boolean {
		return this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED;
	}

	isCancelable(): boolean {
		return (this.estadoSelected === APPOINTMENT_STATES_ID.ASSIGNED &&
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED) ||
			(this.estadoSelected === APPOINTMENT_STATES_ID.CONFIRMED &&
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) ||
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.OUT_OF_DIARY;
	}

	isAbsent(): boolean {
		return this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT;
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motivo?: string): void {
		this.appointmentFacade.changeState(this.params.appointmentData.appointmentId, newStateId, motivo)
			.subscribe(() => {
				const appointmentInformation = { id: this.params.appointmentData.appointmentId, stateId: newStateId };
				this.dialogRef.close(appointmentInformation);
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, _ => {
				this.changeState(this.appointment?.appointmentStateId);
				this.snackBarService.showError(`Error al actualizar estado de turno
				${getAppointmentState(this.appointment?.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	updatePhoneNumber(phonePrefix: string, phoneNumber: string) {
		this.appointmentFacade.updatePhoneNumber(this.params.appointmentData.appointmentId, phonePrefix, phoneNumber).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	updateCoverageData(patientMedicalCoverageId: number) {
		this.appointmentService.updateMedicalCoverage(this.params.appointmentData.appointmentId, patientMedicalCoverageId).subscribe(() => {
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
				identificationTypeId: this.params.appointmentData.patient.identificationTypeId,
				identificationNumber: this.params.appointmentData.patient.identificationNumber,
				genderId: this.params.appointmentData.patient.genderId,
				initValues: this.patientMedicalCoverages,
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.params.appointmentData.patient.id), patientCoverages).subscribe(
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
		this.appointmentService.mqttCall(this.params.appointmentData.appointmentId).subscribe();
	}

	hideFilters(): void {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const condition = (patientMedicalCoverage.condition) ? patientMedicalCoverage.condition.toLowerCase() : null;
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name, patientMedicalCoverage.affiliateNumber, condition]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText].filter(Boolean).join(' / ');
	}

	closeDialog(returnValue?: string) {
		if (!returnValue)
			this.medicalCoverageInfo.setAppointmentMCoverage(this.summaryCoverageData);
		this.dialogRef.close(returnValue);
	}

	enableDowndloadAnexo(option: boolean) {
		this.isCheckedDownloadAnexo = option;
	}

	enableDowndloadFormulario(option: boolean) {
		this.isCheckedDownloadFormulario = option;
	}

	getReportAppointment(): void {
		if (this.isCheckedDownloadAnexo && this.isCheckedDownloadFormulario) {
			this.appointmentService.getAnexoPdf(this.params.appointmentData).subscribe();
			this.appointmentService.getFormPdf(this.params.appointmentData).subscribe();
		} else if (this.isCheckedDownloadAnexo && !this.isCheckedDownloadFormulario) {
			this.appointmentService.getAnexoPdf(this.params.appointmentData).subscribe();
		} else {
			this.appointmentService.getFormPdf(this.params.appointmentData).subscribe();
		}
	}

	viewPatientName(appointmentInformation: PatientAppointmentInformation): string {
		return this.patientNameService.getPatientName(appointmentInformation.patient.fullName, appointmentInformation.patient.fullNameWithNameSelfDetermination);
	}

	clear(): void {
		this.formEdit.controls.newCoverageData.setValue(null);
	}

	dateFormToggle(): void{
		this.isDateFormVisible = !this.isDateFormVisible;
	}

	setHideObservationTitle(value: boolean): void{
		this.hideObservationTitle = value;
	}

	setHideObservationForm(value: boolean): void{
		this.hideObservationForm = value;
	}

	updateObservation(): void{
		this.observation = this.formObservations.get('observation').value;
		this.appointmentFacade.updateObservation(this.params.appointmentData.appointmentId, this.formObservations.controls.observation.value).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.observations.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
		this.setHideObservationForm(true);
	}

	cancelObservation(): void{
		this.hideObservationForm = true;
		if(!this.observation)
			this.hideObservationTitle = true;
		else
			this.hideObservationTitle = false;
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
			if(this.coverageData.condition) {
				summaryInfo.condition = this.coverageData.condition;
			}
			if(this.coverageData.medicalCoverage.cuit) {
				summaryInfo.cuit = this.coverageData.medicalCoverage.cuit;
			}
			if(this.coverageData.medicalCoverage.type) {
				summaryInfo.type = this.coverageData.medicalCoverage.type;
			}
		}
		this.summaryCoverageData = summaryInfo;
	}

}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		fullName?: string
		identificationNumber?: string,
		identificationTypeId?: number,
		typeId: number,
		fullNameWithNameSelfDetermination?: string,
		genderId?: number,
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
}
