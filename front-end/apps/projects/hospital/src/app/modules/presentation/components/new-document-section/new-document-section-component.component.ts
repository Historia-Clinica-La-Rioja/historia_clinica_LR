import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-new-document-section',
	templateUrl: './new-document-section-component.component.html',
	styleUrls: ['./new-document-section-component.component.scss']
})
export class NewDocumentSectionComponent {

	@Input() sectionTitle: string;
	@Input() sectionImportance: string;

	constructor() {}

}
