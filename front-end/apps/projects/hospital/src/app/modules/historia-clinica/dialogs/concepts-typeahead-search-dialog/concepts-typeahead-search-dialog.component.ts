import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { SnomedDto } from "@api-rest/api-model";
import { SnomedECL } from "@api-rest/api-model";

@Component({
  selector: 'app-concepts-typeahead-search-dialog',
  templateUrl: './concepts-typeahead-search-dialog.component.html',
  styleUrls: ['./concepts-typeahead-search-dialog.component.scss']
})
export class ConceptsTypeaheadSearchDialogComponent {

	constructor(
		public dialogRef: MatDialogRef<ConceptsTypeaheadSearchDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			ecl: SnomedECL,
			title: string,
			placeholder: string
		}
	) {}

	setConcept(selectedConcept: SnomedDto): void {
		this.dialogRef.close({ selectedConcept });
	}

}
