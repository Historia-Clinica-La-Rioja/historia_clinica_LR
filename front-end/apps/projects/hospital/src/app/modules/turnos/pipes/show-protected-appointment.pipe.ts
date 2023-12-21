import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
	name: 'showProtectedAppointment'
})
export class ShowProtectedAppointmentPipe implements PipeTransform {

    constructor(private translate: TranslateService) {
    }

	transform(protectedAppointments: boolean[]): string {
        let typesOfProtectedAppointments: string[] = [];

		if (protectedAppointments[0])
            typesOfProtectedAppointments.push(this.translate.instant('turnos.search-appointments-in-care-network.CARELINE'));

		if (protectedAppointments[1])
            typesOfProtectedAppointments.push(this.translate.instant('turnos.appointment-event.PROTECTED_APPOINTMENT_WEB'));

        if(!protectedAppointments[0] && !protectedAppointments[1])
            typesOfProtectedAppointments.push(this.translate.instant('turnos.appointment-event.NO_PROTECTED_APPOINTMENT'));
        
        return typesOfProtectedAppointments.join(', ');
	}

}