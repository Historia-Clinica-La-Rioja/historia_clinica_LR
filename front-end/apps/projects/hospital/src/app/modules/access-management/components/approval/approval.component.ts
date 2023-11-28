import { getIconState } from '@access-management/constants/approval';
import { Component, Input } from '@angular/core';
import { ReferenceRegulationDto } from '@api-rest/api-model';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-approval',
	templateUrl: './approval.component.html',
	styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent {

	regulationState: ColoredLabel;
	referenceRegulationDto: ReferenceRegulationDto;

	@Input() set approval(value: ReferenceRegulationDto) {
		this.referenceRegulationDto = value;
		this.regulationState = getIconState[value.state];
	};

	constructor() { }

}