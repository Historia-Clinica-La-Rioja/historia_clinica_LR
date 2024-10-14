
import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
	selector: 'app-html',
	templateUrl: './html.component.html',
	styleUrls: ['./html.component.scss']
})
export class HtmlComponent {

	valueAsHtml: SafeHtml = null;

	constructor(
		private sanitizer: DomSanitizer,
	) { }

	@Input() set content(value: string) {
		this.valueAsHtml = this.sanitizer.bypassSecurityTrustHtml(value);
	}

}
