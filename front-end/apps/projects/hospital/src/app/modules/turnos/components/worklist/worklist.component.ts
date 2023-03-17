import { Component, OnInit } from '@angular/core';
import { DateDto, EquipmentDto, TimeDto } from '@api-rest/api-model';
import { EquipmentService } from '@api-rest/services/equipment.service';
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
    appointments: mockedAppointment[] = [{
        'appointmentStateId': 1,
        'date': { 'day': 1, 'month': 1, 'year': 2023 },
        'hour': { 'hours': 10, 'minutes': 15 },
        'patientId': 1000,
        'patientName': 'Lionel Andres Messi',
        'identificationNumber': 39555887
    },
    {
        'appointmentStateId': 2,
        'date': { 'day': 2, 'month': 2, 'year': 2023 },
        'hour': { 'hours': 10, 'minutes': 30 },
        'patientId': 2000,
        'patientName': 'Julian "Araña" Alvarez',
        'identificationNumber': 38555887
    },
    {
        'appointmentStateId': 3,
        'date': { 'day': 3, 'month': 3, 'year': 2023 },
        'hour': { 'hours': 10, 'minutes': 45 },
        'patientId': 3000,
        'patientName': 'Enzo Fernandez',
        'identificationNumber': 37555887
    },
    {
        'appointmentStateId': 4,
        'date': { 'day': 4, 'month': 4, 'year': 2023 },
        'hour': { 'hours': 11, 'minutes': 30 },
        'patientId': 4000,
        'patientName': 'Cristian "Cuti" Romero',
        'identificationNumber': 36555887
    },
    {
        'appointmentStateId': 5,
        'date': { 'day': 5, 'month': 5, 'year': 2023 },
        'hour': { 'hours': 11, 'minutes': 30 },
        'patientId': 5000,
        'patientName': 'Emiliano "Dibu" Martinez',
        'identificationNumber': 35555887
    },
    ]

    constructor(private readonly equipmentService: EquipmentService) { }

    ngOnInit(): void {
        this.equipmentService.getAll().subscribe((equipments: EquipmentDto[]) => {
            this.equipments = equipments;
        });
        this.mapAppointmentsToDetailedAppointments()
    }

    private getAppointmentStateColor(appointment: mockedAppointment): string {
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

    private getAppointmentDescription(appointment: mockedAppointment): string {
        return this.mapAppointmentDescription(APPOINTMENT_STATES.filter(a => a.id == appointment.appointmentStateId)[0].description)
    }

    private mapAppointmentsToDetailedAppointments(){
        this.appointments.map(a => {
            this.detailedAppointments.push({
                data: a, 
                color: this.getAppointmentStateColor(a),
                description: this.getAppointmentDescription(a),
            })
        })
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
    data: mockedAppointment,
    color: string,
    description: string
}

export interface mockedAppointment {
    appointmentStateId: number,
    date: DateDto,
    hour: TimeDto,
    patientId: number,
    patientName: string,
    identificationNumber: number,
}