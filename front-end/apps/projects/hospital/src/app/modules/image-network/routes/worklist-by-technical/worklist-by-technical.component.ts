import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelectChange } from '@angular/material/select';
import { EquipmentAppointmentListDto, EquipmentDto, InstitutionBasicInfoDto, ModalityDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { mapDateWithHypenToDateWithSlash, timeToString } from '@api-rest/mapper/date-dto.mapper';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PatientNameService } from "@core/services/patient-name.service";
import {
	WORKLIST_APPOINTMENT_STATES,
	APPOINTMENT_STATES_ID,
	AppointmentState,
	stateColor,
} from '@turnos/constants/appointment';
import { REPORT_STATES, ReportState, REPORT_STATES_ID } from '../../constants/report';
import { Observable } from 'rxjs';
import { FinishStudyComponent, StudyInfo } from "../../dialogs/finish-study/finish-study.component";
import { DeriveReportComponent } from '../../dialogs/derive-report/derive-report.component';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ModalityService } from '@api-rest/services/modality.service';
import { subDays } from 'date-fns';
import { hasError } from '@core/utils/form.utils';
import { SearchFilters, WorklistFiltersComponent } from '../../components/worklist-filters/worklist-filters.component';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { WorklistFacadeService } from '../../services/worklist-facade.service';
import { DownloadTranscribedOrderComponent } from '../../dialogs/download-transcribed-order/download-transcribed-order.component';
import { ViewPdfBo } from '@presentation/dialogs/view-pdf/view-pdf.service';
import { newDate } from '@core/utils/moment.utils';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { toStudyLabel } from '../../utils/study.utils';
import { WINDOW } from '../../constants/token';

const PAGE_SIZE_OPTIONS = [10];
const PAGE_MIN_SIZE = 10;

@Component({
	selector: 'app-worklist-by-technical',
	templateUrl: './worklist-by-technical.component.html',
	styleUrls: ['./worklist-by-technical.component.scss'],
	providers: [WorklistFacadeService]
})
export class WorklistByTechnicalComponent implements OnInit {
	@ViewChild('paginator') paginator: MatPaginator;
	@ViewChild(WorklistFiltersComponent) worklistFiltersComponent: WorklistFiltersComponent;

	equipments: EquipmentDto[] = [];
	modalities$: Observable<ModalityDto[]>;
	allEquipments: EquipmentDto[] = [];
	equipmentId: number;

	detailedAppointments: detailedAppointment[] = [];
	appointments: EquipmentAppointmentListDto[] = [];

	hasError = hasError;
	filtersForm: UntypedFormGroup;
	today = newDate();
	minDate: Date = subDays(new Date(), 60)
	startDate: string = toApiFormat(this.today);
	endDate: string = toApiFormat(this.today);

	nameSelfDeterminationFF = false;
	permission = false;

	readonly mssg = 'image-network.home.NO_PERMISSION';
	readonly nothingToShowMssg = 'messages.NO_DATA';

	appointmentsStates = WORKLIST_APPOINTMENT_STATES;
	reportStates = REPORT_STATES_ID;
	defaultStates = [];
	searchFilters: SearchFilters;

	pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSlice: detailedAppointment[] = [];
	startPage: number = 0;
	endPage: number = PAGE_MIN_SIZE;
	selectedAppointment: EquipmentAppointmentListDto;

	panelOpenState = true;
	fetchingData = false;

	readonly appointmentStatesId = APPOINTMENT_STATES_ID;


	constructor(private readonly equipmentService: EquipmentService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly appointmentsService: AppointmentsService,
		private readonly translateService: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly modalityService: ModalityService,
		private readonly prescripcionesService: PrescripcionesService,
		public dialog: MatDialog,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly worklistFacadeService: WorklistFacadeService,
		private readonly patientNameService: PatientNameService,
		@Inject(WINDOW) private window: Window,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(isOn => {
			this.permission = isOn;
		})
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn;
		})
	}

	ngOnInit(): void {
		this.filtersForm = this.formBuilder.group({
			modality: [null],
			equipment: [null],
			datePicker: this.formBuilder.group({
				start: [this.today, Validators.required],
				end: [this.today, Validators.required],
			})
		});

		this.setDefaultStates();

		this.modalities$ = this.modalityService.getAll();
		this.equipmentService.getAll().subscribe(equipments => {
			this.equipments = equipments;
			this.allEquipments = equipments;
		});

		this.worklistFacadeService.appointments$.subscribe(appointments => {
			this.appointments = appointments;
			if (!this.searchFilters) {
				this.detailedAppointments = this.mapAppointmentsToDetailedAppointments(this.appointments);
				this.pageSlice = this.detailedAppointments.slice(this.startPage, this.endPage);
			}
			else {
				this.pageSlice = this.filterAppointments(this.searchFilters.appointmentStates, this.searchFilters.patientName, this.searchFilters.patientIdentification);
			}
			if (this.fetchingData) {
				this.manageStatusCheckboxes();
				this.fetchingData = false;
			}
			this.enableInputs();
		});
	}

	private setDefaultStates() {
		this.defaultStates = [];
		this.defaultStates.push(this.appointmentsStates.find(appointment => appointment.id === APPOINTMENT_STATES_ID.ASSIGNED))
		this.defaultStates.push(this.appointmentsStates.find(appointment => appointment.id === APPOINTMENT_STATES_ID.CONFIRMED))
	}

	private setPreviousStates() {
		this.defaultStates = this.searchFilters?.appointmentStates;
	}

	private manageStatusCheckboxes() {
		this.worklistFiltersComponent?.manageStatusCheckboxes();
	}

	private disableInputs() {
		this.filtersForm.get('equipment').disable();
		this.filtersForm.get('datePicker').get('start').disable();
		this.filtersForm.get('datePicker').get('end').disable();
	}

	private enableInputs() {
		this.filtersForm.get('equipment').enable();
		this.filtersForm.get('datePicker').get('start').enable();
		this.filtersForm.get('datePicker').get('end').enable();
	}

	onModalityChange() {
		this.equipments = [];
		let modalityId = this.filtersForm.controls.modality.value?.id
		this.filtersForm.controls.equipment.setValue(null)
		this.worklistFiltersComponent.clearInputs();
		this.manageStatusCheckboxes();
		this.resetAppointmentsData();
		if (modalityId) {
			this.equipmentService.getEquipmentByModality(modalityId).subscribe(equipments => {
				this.equipments = equipments
			});
		} else {
			this.equipments = this.allEquipments;
		}
		this.equipmentId = null;
		this.worklistFacadeService.clearEquipmentId();
	}

	onEquipmentChange(equipment: MatSelectChange) {
		this.startPage = 0;
		this.resetDate();
		this.equipmentId = equipment.value.id;
		this.setDefaultStates();
		this.resetAppointmentsData();
		this.getAppointments(this.equipmentId);
	}

	setSelectedDate() {
		this.startPage = 0;
		const startDate = this.filtersForm.get('datePicker').get('start').value;
		this.startDate = startDate ? toApiFormat(startDate) : null;
		const endDate = this.filtersForm.get('datePicker').get('end').value
		this.endDate = endDate ? toApiFormat(endDate) : null
		if (this.startDate && this.endDate && this.equipmentId) {
			this.getAppointments(this.equipmentId);
		}
	}

	setPanelState(): void {
		this.panelOpenState = !this.panelOpenState;
		this.setDefaultStates()
	}

	search(searchFilters: SearchFilters) {
		this.searchFilters = searchFilters;
		this.setPreviousStates();
		this.pageSlice = this.filterAppointments(searchFilters.appointmentStates, searchFilters.patientName, searchFilters.patientIdentification);
		this.paginator?.firstPage();
	}

	private resetDate() {
		this.startDate = toApiFormat(this.today);
		this.endDate = toApiFormat(this.today);
		this.filtersForm.get('datePicker').get('start').setValue(this.startDate);
		this.filtersForm.get('datePicker').get('end').setValue(this.endDate);
	}

	private getAppointments(equipmentId: number) {
		this.fetchingData = true;
		this.worklistFiltersComponent.clearInputs();
		this.disableInputs();
		this.worklistFacadeService.changeTechnicalFilters(equipmentId, this.startDate, this.endDate);
	}

	private resetAppointmentsData() {
		this.appointments = [];
		this.detailedAppointments = [];
		this.pageSlice = [];
	}

	private filterAppointments(stateFilters?: AppointmentState[], patientName?: string, patientIdentification?: string): detailedAppointment[] {
		this.detailedAppointments = this.filterData(stateFilters, patientName, patientIdentification);
		return this.detailedAppointments.slice(this.startPage, this.endPage);
	}

	private filterData(stateFilters?: AppointmentState[], patientName?: string, patientIdentification?: string): detailedAppointment[] {
		let filteredAppointments = this.appointments.filter(appointment =>
			stateFilters.find(a => a.id === appointment.appointmentStateId) &&
			this.checkPatientNameFilter(patientName, appointment) &&
			this.checkPatientIdentificationFilter(patientIdentification, appointment)
		);
		return this.mapAppointmentsToDetailedAppointments(filteredAppointments);
	}

	private checkPatientNameFilter(searchName: string, appointment: EquipmentAppointmentListDto) {
		let patientName = searchName?.toLowerCase();
		let nameSelfDetermination = appointment.patient.person.nameSelfDetermination?.toLowerCase();
		let name = appointment.patient.person.firstName?.toLowerCase();
		let lastName = appointment.patient.person.lastName?.toLowerCase();

		if (patientName) {
			if (this.nameSelfDeterminationFF) {
				if (nameSelfDetermination) {
					return (nameSelfDetermination.includes(patientName) || lastName?.includes(patientName))
				}
			}
			return (name?.includes(patientName) || lastName?.includes(patientName))
		}
		return true
	}

	private checkPatientIdentificationFilter(patientIdentification: string, appointment: EquipmentAppointmentListDto) {
		if (patientIdentification) {
			return appointment.patient.person.identificationNumber?.includes(patientIdentification);
		}
		return true
	}

	cleanModalityInput() {
		this.filtersForm.controls.modality.setValue(null);
		this.onModalityChange();
	}

	private getAppointmentStateColor(appointmentStateId: number): string {
		return stateColor[appointmentStateId];
	}

	private getAppointmentDescription(appointmentStateId: number): string {
		return WORKLIST_APPOINTMENT_STATES.find(a => a.id == appointmentStateId).description
	}

	private mapAppointmentsToDetailedAppointments(appointments: EquipmentAppointmentListDto[]) {
		return appointments.map(appointment => {
			return {
				data: appointment,
				color: this.getAppointmentStateColor(appointment.appointmentStateId),
				description: this.getAppointmentDescription(appointment.appointmentStateId),
				date: mapDateWithHypenToDateWithSlash(appointment.date),
				time: timeToString(appointment.hour),
				canBeFinished: appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED,
				derive: appointment.derivedTo.id ? appointment.derivedTo : null,
				reportStatus: this.getReportStatus(appointment.reportStatusId),
				patientFullName: this.patientNameService.completeName(appointment.patient.person.firstName, appointment.patient.person.nameSelfDetermination, appointment.patient.person.lastName, appointment.patient.person.middleNames, appointment.patient.person.otherLastNames),
				canBeDerived: appointment.reportStatusId === this.reportStates.PENDING,
				studiesFullName: toStudyLabel(appointment.studies.join(','))
			}
		})
	}

	private getReportStatus(reportStatusId): ReportState {
		return REPORT_STATES.find(state => state.id == reportStatusId)
	}

	onPageChange($event: any) {
		const page = $event;
		this.startPage = page.pageIndex * page.pageSize;
		this.endPage = $event.pageSize + this.startPage
		this.pageSlice = this.detailedAppointments.slice(this.startPage, this.endPage);
	}

	finishStudy(appointment: EquipmentAppointmentListDto, patientFullName: string) {
		this.selectedAppointment = appointment;
		this.openFinishStudyDialog(appointment, patientFullName);
	}

	private openFinishStudyDialog(appointment: EquipmentAppointmentListDto, patientFullName: string) {
		const data: StudyInfo = {
			appointmentId: this.selectedAppointment.id,
			patientId: this.selectedAppointment.patient.id,
			studyName: appointment.studyName,
			isTranscribed: !appointment.serviceRequestId && !!appointment.studyName,
			hasOrder: !!appointment.serviceRequestId,
			patient: patientFullName,
			studies: appointment.studies.join(', ')
		}
		const dialogRef = this.dialog.open(FinishStudyComponent, {
			width: '38%',
			autoFocus: false,
			data
		});

		dialogRef.afterClosed().subscribe(result => {
			if (result?.updateState) {
				this.selectedAppointment.appointmentStateId = result.updateState;
				this.updateSelectedAppointmentReportState(result?.reportRequired);
				this.pageSlice = this.filterAppointments(this.searchFilters.appointmentStates);
			}
			this.selectedAppointment = null;
		});
	}

	private updateSelectedAppointmentReportState(reportRequired: boolean) {
		let statusToSet = reportRequired ? REPORT_STATES_ID.PENDING : REPORT_STATES_ID.NOT_REQUIRED;
		this.appointments = this.appointments.map(app => (app.id === this.selectedAppointment.id ? { ...app, reportStatusId: statusToSet } : app));
	}

	downloadOrderHandler(appointment: EquipmentAppointmentListDto): void {
		let patientId = appointment.patient.id;
		if (appointment.serviceRequestId) {
			this.prescripcionesService.downloadPrescriptionPdf(patientId, [appointment.serviceRequestId], PrescriptionTypes.STUDY);
		} else {
			this.downloadTranscribedOrder(patientId, appointment);
		}
	}

	downloadTranscribedOrder(patientId: number, appointment: EquipmentAppointmentListDto) {
		let attachedFiles = [];
		appointment.transcribedOrderAttachedFiles.forEach(file => {
			attachedFiles.push({ url: this.prescripcionesService.getTranscribedAttachedFileUrl(patientId, file.id), filename: file.name })
		})

		if (attachedFiles.length > 1) {
			this.dialog.open(DownloadTranscribedOrderComponent, {
				minWidth: '300px',
				minHeight: '150px',
				autoFocus: false,
				data: attachedFiles,
			});
		} else {
			this.downloadUniqueOrder(attachedFiles[0]);
		}
	}

	downloadUniqueOrder(file: ViewPdfBo) {
		const anchor = document.createElement("a");
		anchor.href = file.url.toString();
		anchor.download = file.filename;

		document.body.appendChild(anchor);
		anchor.click();
		document.body.removeChild(anchor);
	}

	requestReport(appointment: detailedAppointment) {
		this.appointmentsService.requireReport(appointment.data.id).subscribe(() => {
			this.snackBarService.showSuccess(this.translateService.instant("image-network.worklist.REPORT_REQUIRED"))
			appointment.reportStatus = REPORT_STATES.find(state => state.id == REPORT_STATES_ID.PENDING);

			let appointmentFromAppointments = this.appointments.find(app => app.id === appointment.data.id);
			appointmentFromAppointments.reportStatusId = REPORT_STATES_ID.PENDING;

			let appointmentFromDetailedAppointments = this.detailedAppointments.find(app => app.data.id === appointment.data.id);
			appointmentFromDetailedAppointments.canBeDerived = true;
		})
	}

	deriveReport(appointmentId: number) {
		const dialogRef = this.dialog.open(DeriveReportComponent, {
			width: '35%',
			autoFocus: false,
			data: {
				appointmentId
			}
		});

		dialogRef.afterClosed().subscribe(destinationInstitution => {
			if (destinationInstitution) {
				let appointmentFromDetailedAppointments = this.detailedAppointments.find(appointment => appointment.data.id === appointmentId);
				appointmentFromDetailedAppointments.derive = destinationInstitution;
				appointmentFromDetailedAppointments.reportStatus = REPORT_STATES.find(state => state.id == REPORT_STATES_ID.DERIVED);
				appointmentFromDetailedAppointments.canBeDerived = false;

				let appointmentFromAppointments = this.appointments.find(appointment => appointment.id === appointmentId);
				appointmentFromAppointments.derivedTo = destinationInstitution;
				appointmentFromAppointments.reportStatusId = REPORT_STATES_ID.DERIVED;
			}
		});
	}

    goUrlLocalStudy(studyLocalUrl: string) {
		this.window.open(studyLocalUrl, "_blank")
	}
}

export interface detailedAppointment {
	data: EquipmentAppointmentListDto,
	color: string,
	description: string,
	date: string,
	time: string,
	canBeFinished: boolean,
	derive: InstitutionBasicInfoDto,
	reportStatus: ReportState,
	patientFullName: string,
	canBeDerived: boolean,
	studiesFullName: string,
}
