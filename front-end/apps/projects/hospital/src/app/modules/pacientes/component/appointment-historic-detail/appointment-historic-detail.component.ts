import { Component, Input, OnInit } from '@angular/core';
import { PatientAppointmentHistoryDto, SnomedDto } from '@api-rest/api-model';
import { APPOINTMENT_STATES, stateColor } from '@turnos/constants/appointment';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { capitalize } from '@core/utils/core.utils';

@Component({
    selector: 'app-appointment-historic-detail',
    templateUrl: './appointment-historic-detail.component.html',
    styleUrls: ['./appointment-historic-detail.component.scss']
})
export class AppointmentHistoricDetailComponent implements OnInit {

    @Input() appointment: PatientAppointmentHistoryDto;
    appointmentState: AppointmentStateData;
    practicesList: string = '';
    identiferCases = IDENTIFIER_CASES;

    constructor() { }

    ngOnInit(): void {
        this.appointmentState = this.mapAppointmentStateData();
        this.practicesList = this.appointment.practices.length ? this.getPracticesList(this.appointment.practices) : '';
    }

    private getPracticesList(list: SnomedDto[]): string {
        const practicesArray: string[] = list.map(practice => practice.pt)
        return capitalize(practicesArray.join(', '));
    }

    private mapAppointmentStateData(): AppointmentStateData{
        return {
            description: APPOINTMENT_STATES.find(app => app.id == this.appointment.statusId).description,
            color: this.getAppointmentStateColor(this.appointment.statusId),
        }
    }

    private getAppointmentStateColor(appointmentStateId: number): string {
        return stateColor[appointmentStateId];
    }
}

interface AppointmentStateData {
    description: string;
    color: string;
}
