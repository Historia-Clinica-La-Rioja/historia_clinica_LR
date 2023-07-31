import { Component, OnInit, ViewChild } from '@angular/core';
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
import { Color } from '@presentation/colored-label/colored-label.component';
import {
	WORKLIST_APPOINTMENT_STATES,
	APPOINTMENT_STATES_ID,
	AppointmentState,
} from '@turnos/constants/appointment';
import { REPORT_STATES, ReportState, REPORT_STATES_ID } from '../../constants/report';
import { Observable } from 'rxjs';
import { FinishStudyComponent, StudyInfo } from "../../dialogs/finish-study/finish-study.component";
import { DeriveReportComponent } from '../../dialogs/derive-report/derive-report.component';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ModalityService } from '@api-rest/services/modality.service';
import { subDays } from 'date-fns';
import { Moment } from 'moment';
import { hasError } from '@core/utils/form.utils';
import * as moment from 'moment';
import { SearchFilters, WorklistFiltersComponent } from '../../components/worklist-filters/worklist-filters.component';

const PAGE_SIZE_OPTIONS = [10];
const PAGE_MIN_SIZE = 10;
const stateColor = {
    [APPOINTMENT_STATES_ID.CONFIRMED]:  Color.YELLOW,
    [APPOINTMENT_STATES_ID.ABSENT]: Color.GREY,
    [APPOINTMENT_STATES_ID.SERVED]: Color.GREEN,
    [APPOINTMENT_STATES_ID.CANCELLED]: Color.RED,
    [APPOINTMENT_STATES_ID.ASSIGNED]: Color.BLUE
}

@Component({
    selector: 'app-worklist-by-technical',
    templateUrl: './worklist-by-technical.component.html',
    styleUrls: ['./worklist-by-technical.component.scss']
})
export class WorklistByTechnicalComponent implements OnInit {
    @ViewChild('paginator') paginator: MatPaginator;
    @ViewChild(WorklistFiltersComponent) worklistFilters: WorklistFiltersComponent;
    equipments: EquipmentDto[] = [];
    modalities$: Observable<ModalityDto[]>;
    allEquipments: EquipmentDto[] = [];
    equipmentId: number;

    detailedAppointments: detailedAppointment[] = [];
    appointments: EquipmentAppointmentListDto[] = [];

    hasError = hasError;
    filtersForm: UntypedFormGroup;
    today: Moment = moment();
    minDate: Date = subDays(new Date(), 60)
    startDate: string = this.today.format('YYYY-MM-DD');
    endDate: string = this.today.format('YYYY-MM-DD');

    nameSelfDeterminationFF = false;
	permission = false;

    readonly mssg = 'image-network.home.NO_PERMISSION';
    readonly nothingToShowMssg = 'messages.NO_DATA';
    
    appointmentsStates = WORKLIST_APPOINTMENT_STATES;
    reportStates = REPORT_STATES_ID;
    defaultStates = [];
    searchFilters: SearchFilters;

    pageSizeOptions = PAGE_SIZE_OPTIONS;
    pageSlice = [];
	selectedAppointment: EquipmentAppointmentListDto;

    panelOpenState = true;

    readonly appointmentStatesId = APPOINTMENT_STATES_ID;

    constructor(private readonly equipmentService: EquipmentService,
		private readonly featureFlagService: FeatureFlagService,
        private readonly appointmentsService: AppointmentsService,
        private readonly translateService: TranslateService,
        private readonly snackBarService: SnackBarService,
        private readonly modalityService: ModalityService,
	    public dialog: MatDialog,
		private readonly formBuilder: UntypedFormBuilder,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});

        this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(isOn => {
            this.permission = isOn;
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
        this.worklistFilters.manageStatusCheckboxes();
    }

    onModalityChange() {
        this.equipments = [];
        let modalityId = this.filtersForm.controls.modality.value?.id
        this.filtersForm.controls.equipment.setValue(null)
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
    }

    onEquipmentChange(equipment: MatSelectChange){
        this.equipmentId = equipment.value.id;
        this.setDefaultStates();
        this.resetAppointmentsData();
        this.getAppointments(this.equipmentId, this.startDate, this.endDate);
    }

    setSelectedDate(){
        this.startDate = this.filtersForm.get('datePicker').get('start').value?.format('YYYY-MM-DD');
        this.endDate = this.filtersForm.get('datePicker').get('end').value?.format('YYYY-MM-DD');
        if (this.startDate && this.endDate && this.equipmentId) {
            this.setPreviousStates();
            this.getAppointments(this.equipmentId, this.startDate, this.endDate);
        }
    }

    setPanelState(): void {
		this.panelOpenState = !this.panelOpenState;
        this.setDefaultStates()
	}

    search(searchFilters: SearchFilters) {
        this.searchFilters = searchFilters;
        this.setPreviousStates();
        this.filterAppointments(searchFilters.appointmentStates, searchFilters.patientName, searchFilters.patientIdentification);
        this.paginator?.firstPage();
    }

    private getAppointments(equipmentId: number, from?: string, to?: string){
        this.appointmentsService.getAppointmentsByEquipment(equipmentId, from, to).subscribe(appointments => {
            this.appointments = appointments;
            this.manageStatusCheckboxes();
        })
    }

    private resetAppointmentsData() {
        this.appointments = [];
        this.detailedAppointments = [];
        this.pageSlice = [];
    }

    private filterAppointments(stateFilters?: AppointmentState[], patientName?: string, patientIdentification?: string){
        let filteredAppointments = this.appointments.filter(appointment => 
            stateFilters.find(a => a.id === appointment.appointmentStateId) && 
            this.checkPatientNameFilter(patientName, appointment) && 
            this.checkPatientIdentificationFilter(patientIdentification, appointment)
        );
        this.detailedAppointments = this.mapAppointmentsToDetailedAppointments(filteredAppointments);
        this.pageSlice = this.detailedAppointments.slice(0, PAGE_MIN_SIZE);
    }

    private checkPatientNameFilter(searchName: string, appointment: EquipmentAppointmentListDto) {
        let patientName = searchName?.toLowerCase();
        let nameSelfDetermination = appointment.patient.person.nameSelfDetermination?.toLowerCase();
        let name = appointment.patient.person.firstName.toLowerCase();
        let lastName = appointment.patient.person.lastName.toLowerCase();

        if (patientName) {
            if (this.nameSelfDeterminationFF) {
                if (nameSelfDetermination){
                    return (nameSelfDetermination.includes(patientName) || lastName.includes(patientName))
                }
            }
            return (name.includes(patientName) || lastName.includes(patientName))
        }
        return true
    }

    private checkPatientIdentificationFilter(patientIdentification: string, appointment: EquipmentAppointmentListDto) {
        if (patientIdentification) {
            return appointment.patient.person.identificationNumber.includes(patientIdentification);
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

    private mapAppointmentsToDetailedAppointments(appointments){
        return appointments.map(appointment => {
            return {
                data: appointment,
                color: this.getAppointmentStateColor(appointment.appointmentStateId),
                description: this.getAppointmentDescription(appointment.appointmentStateId),
                date: mapDateWithHypenToDateWithSlash(appointment.date),
                time: timeToString(appointment.hour),
                firstName: this.capitalizeWords(appointment.patient.person.firstName),
                lastName: this.capitalizeWords(appointment.patient.person.lastName),
                nameSelfDetermination: this.capitalizeWords(appointment.patient.person.nameSelfDetermination),
                canBeFinished: appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED,
                derive: appointment.derivedTo.id ? appointment.derivedTo : null,
                reportStatus: this.getReportStatus(appointment.reportStatusId)
            }
        })
    }

    private getReportStatus(reportStatusId): ReportState{
        return REPORT_STATES.find(state => state.id == reportStatusId)
    }

    private capitalizeWords(sentence: string) {
        return sentence ? sentence
          .toLowerCase()
          .split(' ')
          .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
          .join(' ') : "";
    }

    onPageChange($event: any) {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.detailedAppointments.slice(startPage, $event.pageSize + startPage);
	}

	finishStudy(appointment) {
		this.selectedAppointment = appointment;
		this.openFinishStudyDialog();
	}

	private openFinishStudyDialog() {
        const data: StudyInfo = {
            appointmentId: this.selectedAppointment.id,
            patientId: this.selectedAppointment.patient.id,
        }
		const dialogRef = this.dialog.open(FinishStudyComponent, {
			width: '35%',
			autoFocus: false,
			data
		});

		dialogRef.afterClosed().subscribe(result => {
            if (result?.updateState) {
				this.selectedAppointment.appointmentStateId = result.updateState;
                this.updateSelectedAppointmentReportState(result?.reportRequired);
				this.filterAppointments(this.searchFilters.appointmentStates);
			}
			this.selectedAppointment = null;
		});
	}

    private updateSelectedAppointmentReportState(reportRequired: boolean) {
        let statusToSet = reportRequired ? REPORT_STATES_ID.PENDING : REPORT_STATES_ID.NOT_REQUIRED ;
        this.appointments = this.appointments.map(app => (app.id === this.selectedAppointment.id ? { ...app, reportStatusId: statusToSet } : app));
    }

    requestReport(appointment: detailedAppointment) {
        this.appointmentsService.requireReport(appointment.data.id).subscribe(() => {
            this.snackBarService.showSuccess(this.translateService.instant("image-network.worklist.REPORT_REQUIRED"))
            appointment.reportStatus = REPORT_STATES.find(state => state.id == REPORT_STATES_ID.PENDING);
            let appointmentFromAppointments = this.appointments.find(app => app.id === appointment.data.id);
            appointmentFromAppointments.reportStatusId = REPORT_STATES_ID.PENDING;
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
            if (destinationInstitution){
                let appointmentFromDetailedAppointments = this.detailedAppointments.find(appointment => appointment.data.id === appointmentId);
                appointmentFromDetailedAppointments.derive  = destinationInstitution;
                appointmentFromDetailedAppointments.reportStatus = REPORT_STATES.find(state => state.id == REPORT_STATES_ID.DERIVED);
                
                let appointmentFromAppointments = this.appointments.find(appointment => appointment.id === appointmentId);
                appointmentFromAppointments.derivedTo = destinationInstitution;
                appointmentFromAppointments.reportStatusId = REPORT_STATES_ID.DERIVED;
            }
        });
    }

}

export interface detailedAppointment {
    data: EquipmentAppointmentListDto,
    color: string,
    description: string,
    date: string,
    time: string,
    firstName: string,
    lastName: string,
    nameSelfDetermination: string,
	canBeFinished: boolean,
    derive: InstitutionBasicInfoDto,
    reportStatus: ReportState,
}
