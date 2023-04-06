import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatOption } from '@angular/material/core';
import { MatSelect, MatSelectChange } from '@angular/material/select';
import { EquipmentAppointmentListDto, EquipmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { mapDateWithHypenToDateWithSlash, timeToString } from '@api-rest/mapper/date-dto.mapper';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { WORKLIST_APPOINTMENT_STATES, APPOINTMENT_STATES_ID, AppointmentState } from '@turnos/constants/appointment';
import { Observable } from 'rxjs';

const stateColor = {
    [APPOINTMENT_STATES_ID.CONFIRMED]:  Color.YELLOW,
    [APPOINTMENT_STATES_ID.ABSENT]: Color.GREY,
    [APPOINTMENT_STATES_ID.SERVED]: Color.GREEN,
    [APPOINTMENT_STATES_ID.CANCELLED]: Color.RED,
    [APPOINTMENT_STATES_ID.ASSIGNED]: Color.BLUE
  }

@Component({
    selector: 'app-worklist',
    templateUrl: './worklist.component.html',
    styleUrls: ['./worklist.component.scss']
})
export class WorklistComponent implements OnInit {
    @ViewChild('select') select: MatSelect;
    equipments$: Observable<EquipmentDto[]>;
    detailedAppointments: detailedAppointment[] = [];
    appointments: EquipmentAppointmentListDto[] = [];

    nameSelfDeterminationFF = false;
	permission = false;

    readonly mssg = 'image-network.home.NO_PERMISSION';

    appointmentsStates = WORKLIST_APPOINTMENT_STATES;
    states = new FormControl('');
    selectedStates: string = '';
    allSelected=false;      

    constructor(private readonly equipmentService: EquipmentService,
		private readonly featureFlagService: FeatureFlagService,
        private readonly appointmentsService: AppointmentsService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});

        this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES).subscribe(isOn => {
            this.permission = isOn;
        })
	}

    ngOnInit(): void {
        this.equipments$ = this.equipmentService.getAll();
    }

    onEquipmentChange(equipment: MatSelectChange){
        let equipmentId = equipment.value.id;
        this.appointmentsService.getAppointmentsByEquipment(equipmentId).subscribe(appointments => {
            this.appointments = appointments;
            this.setDefaultSelection();
            this.checkSelection();
         })
    }

    private filterAppointments(stateFilters?: AppointmentState[]){
        let filteredAppointments = this.appointments.filter(appointment => stateFilters.find(a => a.id === appointment.appointmentStateId));
        this.detailedAppointments = this.mapAppointmentsToDetailedAppointments(filteredAppointments);
    }

    onStatusChange(states: MatSelectChange){
        this.setSelectionText(states);
        this.filterAppointments(states.value);
    }

    private setSelectionText(states: MatSelectChange){
        this.selectedStates = '';
        states.value.map(state => {
            this.selectedStates = this.selectedStates.concat(state.description.toString(), ", ")
        })
        this.selectedStates = this.selectedStates.trim().slice(0, -1);
    }

    toggleAllSelection() {
        if (this.allSelected) {
          this.select.options.forEach((item: MatOption) => item.select());
        } else {
          this.select.options.forEach((item: MatOption) => item.deselect());
        }
      }

    checkSelection() {
        let newStatus = true;
        this.select.options.forEach((item: MatOption) => {
          if (!item.selected) {
            newStatus = false;
          }
        });
        this.allSelected = newStatus;
    }

    private setDefaultSelection(){
        this.select?.options.forEach((item: MatOption) => {
            item.deselect();
            if ((item.value.id === APPOINTMENT_STATES_ID.ASSIGNED || item.value.id === APPOINTMENT_STATES_ID.CONFIRMED) && !item.selected) {
                item._selectViaInteraction();
            }
        });
    }

    private getAppointmentStateColor(appointmentStateId: number): string {
        return stateColor[appointmentStateId];
    }

    private getAppointmentDescription(appointmentStateId: number): string {
        return WORKLIST_APPOINTMENT_STATES.find(a => a.id == appointmentStateId).description
    }

    private mapAppointmentsToDetailedAppointments(appointments): detailedAppointment[]{
        return appointments.map(a => {
            return {
                data: a, 
                color: this.getAppointmentStateColor(a.appointmentStateId),
                description: this.getAppointmentDescription(a.appointmentStateId),
                date: mapDateWithHypenToDateWithSlash(a.date),
                time: timeToString(a.hour),
                firstName: this.capitalizeWords(a.patient.person.firstName),
                lastName: this.capitalizeWords(a.patient.person.lastName),
                nameSelfDetermination: this.capitalizeWords(a.patient.person.nameSelfDetermination)
            }
        })
    }

    private capitalizeWords(sentence: string) {
        return sentence ? sentence
          .toLowerCase()
          .split(' ')
          .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
          .join(' ') : "";
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
    nameSelfDetermination: string
}