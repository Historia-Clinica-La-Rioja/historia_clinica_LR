import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';

@Component({
	selector: 'app-problem-concept-search-dialog',
	templateUrl: './problem-concept-search-dialog.component.html',
	styleUrls: ['./problem-concept-search-dialog.component.scss']
})
export class ProblemConceptSearchDialogComponent implements OnInit {
	enableSubmitButton = true;

	constructor(public dialogRef: MatDialogRef<ProblemConceptSearchDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: ProblemData) { }

	ngOnInit(): void {
	}

	addProblem(event): void {
		this.data.ambulatoryConsultationProblemsService.setConcept(event);
		this.data.ambulatoryConsultationProblemsService.addToList(false);
		this.dialogRef.close();
	}

	close(): void {
		this.data.ambulatoryConsultationProblemsService.resetForm();
		this.dialogRef.close()
	}

}

interface ProblemData {
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
	searchConceptsLocallyFF: boolean,
}
