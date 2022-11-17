import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
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
		cubejs_chart: 'cubejs-chart',
		cubejs_dashboard: 'cubejs-dashboard',
		divider: 'divider',
		html: 'html',
		json: 'json',
		link: 'link',
		tabs: 'tabs',
		typography: 'typography',
	};
	@Input() uiComponent: UIComponentDto;
	@Input() listOnTab: string = null;
	constructor(
		private sanitizer: DomSanitizer,
	) { }

	get valueAsHtml() {
		return this.sanitizer.bypassSecurityTrustHtml(this.uiComponent.args.value);
	}

}
