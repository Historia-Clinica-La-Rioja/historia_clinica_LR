import { getIconState } from '@access-management/constants/approval';
import { Component, Input } from '@angular/core';
import { EReferenceRegulationState } from '@api-rest/api-model';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-regulation-state',
	templateUrl: './regulation-state.component.html',
	styleUrls: ['./regulation-state.component.scss'],
})
export class RegulationStateComponent {

	coloredLabelState: ColoredLabel;

	@Input()
	set state(regulationState: EReferenceRegulationState) {
		this.coloredLabelState = getIconState[regulationState];
	}

	constructor() { }
}
