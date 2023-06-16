import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';

@Component({
	selector: 'app-cubejs-card',
	templateUrl: './cubejs-card.component.html',
	styleUrls: ['./cubejs-card.component.scss']
})
export class CubejsCardComponent {

	@Input() header: SummaryHeader;
	@Input() content: UIComponentDto;
	@Input() listOnTab: string = null;
	@Output() close = new EventEmitter();

	constructor() { }
}
