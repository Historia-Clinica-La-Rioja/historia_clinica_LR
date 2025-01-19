import {Component, EventEmitter, Input, Output} from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-ui-component',
	templateUrl: './ui-component.component.html',
	styleUrls: ['./ui-component.component.scss']
})
export class UiComponentComponent {
	TYPE_DEFINITIONS = {
		card: 'card',
		code: 'code',
		columns: 'columns',
		cubejs_chart: 'cubejs-chart_old',
		ui_chart: 'cubejs-chart', //hsi-936
		cubejs_dashboard: 'cubejs-dashboard',
		cubejs_card: 'cubejs-card',
		divider: 'divider',
		html: 'html',
		json: 'json',
		link: 'link',
		tabs: 'tabs',
		typography: 'typography',
		multiselect_cubejs: 'multiselect-cubejs',
	};
	@Input() uiComponent: UIComponentDto;
	@Input() listOnTab: string = null;
	@Output() close = new EventEmitter();

	constructor() { }

}
