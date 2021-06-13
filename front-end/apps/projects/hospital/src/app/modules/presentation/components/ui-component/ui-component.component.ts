import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

export class UIComponent {
	type: string;
	args: { [index: string]: any };
	children?: UIComponent[];
	actions?: UIComponent[];
}

@Component({
	selector: 'app-ui-component',
	templateUrl: './ui-component.component.html',
	styleUrls: ['./ui-component.component.scss']
})
export class UiComponentComponent {
	@Input() uiComponent: UIComponent;
	constructor(
		private sanitizer: DomSanitizer,
	) { }

	get valueAsHtml() {
		return this.sanitizer.bypassSecurityTrustHtml(this.uiComponent.args.value);
	}
}
