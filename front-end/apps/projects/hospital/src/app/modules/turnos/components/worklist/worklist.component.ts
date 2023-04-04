import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { EquipmentAppointmentListDto, EquipmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { mapDateWithHypenToDateWithSlash, timeToString } from '@api-rest/mapper/date-dto.mapper';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { APPOINTMENT_STATES, APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';

@Component({
    selector: 'app-worklist',
    templateUrl: './worklist.component.html',
    styleUrls: ['./worklist.component.scss']
})
export class WorklistComponent implements OnInit {
    equipments: EquipmentDto[] = [];
    detailedAppointments: detailedAppointment[] = [];
    appointments: EquipmentAppointmentListDto[] = [];
    isFetchingData = false;
    nameSelfDeterminationFF = false;
	permission = false;

    readonly mssg = 'image-network.home.NO_PERMISSION';

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
        this.equipmentService.getAll().subscribe((equipments: EquipmentDto[]) => {
            this.equipments = equipments;
        });
    }

    filterAppointments(equipment: MatSelectChange){
        this.isFetchingData = true;
        this.detailedAppointments = [];
        this.appointmentsService.getAppointmentsByEquipment(equipment.value.id).subscribe(appointments => {
           this.mapAppointmentsToDetailedAppointments(appointments);
           this.isFetchingData = false;
        })
    }

    private getAppointmentStateColor(appointment: EquipmentAppointmentListDto): string {
        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) {
            return Color.YELLOW;
        }

        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT) {
            return Color.GREY;
        }

        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.SERVED) {
            return Color.GREEN;
        }

        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.CANCELLED) {
            return Color.RED;
        }

        return Color.BLUE;
    }

    private getAppointmentDescription(appointment: EquipmentAppointmentListDto): string {
        return this.mapAppointmentDescription(APPOINTMENT_STATES.filter(a => a.id == appointment.appointmentStateId)[0].description)
    }

    private mapAppointmentsToDetailedAppointments(appointments){
        appointments.map(a => {
            this.detailedAppointments.push({
                data: a, 
                color: this.getAppointmentStateColor(a),
                description: this.getAppointmentDescription(a),
                date: mapDateWithHypenToDateWithSlash(a.date),
                time: timeToString(a.hour),
                firstName: this.capitalizeWords(a.patient.person.firstName),
                lastName: this.capitalizeWords(a.patient.person.lastName),
                nameSelfDetermination: this.capitalizeWords(a.patient.person.nameSelfDetermination)
            })
        })
    }

    private capitalizeWords(sentence: string) {
        return sentence ? sentence
          .toLowerCase()
          .split(' ')
          .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
          .join(' ') : "";
      }

    private mapAppointmentDescription(description: string): string {
        if (description === 'Confirmado') {
            return 'En sala'
        }
        if (description === 'Cancelado') {
            return 'Rechazado'
        }
        return description
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