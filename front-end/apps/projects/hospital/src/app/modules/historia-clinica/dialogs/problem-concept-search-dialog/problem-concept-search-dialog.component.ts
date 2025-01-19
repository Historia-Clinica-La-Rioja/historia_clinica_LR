import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';

@Component({
	selector: 'app-problem-concept-search-dialog',
	templateUrl: './problem-concept-search-dialog.component.html',
	styleUrls: ['./problem-concept-search-dialog.component.scss']
})
export class ProblemConceptSearchDialogComponent {
	enableSubmitButton = true;

	constructor(public dialogRef: MatDialogRef<ProblemConceptSearchDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: ProblemData) { }

	addProblem(event) {
		if(event){
			this.data.ambulatoryConsultationProblemsService.setConcept(event);
		}
		this.data.ambulatoryConsultationProblemsService.addToList(false);
		this.dialogRef.close();
	}

	close() {
		this.data.ambulatoryConsultationProblemsService.resetForm();
		this.dialogRef.close()
	}

}

interface ProblemData {
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
	searchConceptsLocallyFF: boolean,
}
