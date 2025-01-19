import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'showAppointmentOverTurn'
})
export class ShowAppointmentOverTurnPipe implements PipeTransform {

	transform(overTurnCount: number): string {
		if (overTurnCount <= 0)
            return 'turnos.appointment-event.NO_OVERTURN';
		if (overTurnCount === 1)
			return 'turnos.appointment-event.OVERTURN';
		if (overTurnCount > 1) 
            return 'turnos.appointment-event.OVERTURNS';
	}

}
