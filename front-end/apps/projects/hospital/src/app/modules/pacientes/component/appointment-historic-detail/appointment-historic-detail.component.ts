import { Component, Input, OnInit } from '@angular/core';
import { PatientAppointmentHistoryDto } from '@api-rest/api-model';
import { APPOINTMENT_STATES, stateColor } from '@turnos/constants/appointment';

@Component({
    selector: 'app-appointment-historic-detail',
    templateUrl: './appointment-historic-detail.component.html',
    styleUrls: ['./appointment-historic-detail.component.scss']
})
export class AppointmentHistoricDetailComponent implements OnInit {

    @Input() appointment: PatientAppointmentHistoryDto;
    appointmentState: appointmentStateData;

    constructor() { }

    ngOnInit(): void {
        this.appointmentState = this.mapAppointmentStateData();
    }

    private mapAppointmentStateData(): appointmentStateData{
        return {
            description: APPOINTMENT_STATES.find(app => app.id == this.appointment.statusId).description,
            color: this.getAppointmentStateColor(this.appointment.statusId),
        }
    }

    private getAppointmentStateColor(appointmentStateId: number): string {
        return stateColor[appointmentStateId];
    }
}

interface appointmentStateData {
    description: string;
    color: string;
}
