import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-ui-component',
	templateUrl: './ui-component.component.html',
	styleUrls: ['./ui-component.component.scss']
})
export class UiComponentComponent {
	@Input() uiComponent: UIComponentDto;
	constructor(
		private sanitizer: DomSanitizer,
	) { }

	get valueAsHtml() {
		return this.sanitizer.bypassSecurityTrustHtml(this.uiComponent.args.value);
	}
}
