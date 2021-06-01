import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-document-section-copy',
	templateUrl: './new-document-section-component.component.html',
	styleUrls: ['./new-document-section-component.component.scss']
})
export class NewDocumentSectionComponent implements OnInit {

	@Input() sectionTitle: string;
	@Input() sectionImportance: string;

	constructor() {

	}

	ngOnInit(): void {
	}

}
