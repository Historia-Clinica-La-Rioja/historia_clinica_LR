import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MasterDataDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { ExpiredAppointmentForm } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { getExpiredReasons } from '@turnos/utils/expired-appointment.utils';

@Component({
	selector: 'app-expired-appointment-motive-form',
	templateUrl: './expired-appointment-motive-form.component.html',
	styleUrls: ['./expired-appointment-motive-form.component.scss']
})
export class ExpiredAppointmentMotiveFormComponent {

	readonly hasError = hasError;
	expiredReasons: MasterDataDto[] = getExpiredReasons();
	@Input() expiredAppointmentForm: FormGroup<ExpiredAppointmentForm>;

	constructor() { }

}
