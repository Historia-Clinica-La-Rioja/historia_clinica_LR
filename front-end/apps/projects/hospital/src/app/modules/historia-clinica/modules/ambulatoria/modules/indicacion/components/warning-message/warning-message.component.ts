import { Component, Input } from '@angular/core';
import { ParenteralPlanDto } from '@api-rest/api-model';

@Component({
	selector: 'app-warning-message',
	templateUrl: './warning-message.component.html',
	styleUrls: ['./warning-message.component.scss']
})
export class WarningMessageComponent {

	@Input() parenteralPlan: ParenteralPlanDto;

}
