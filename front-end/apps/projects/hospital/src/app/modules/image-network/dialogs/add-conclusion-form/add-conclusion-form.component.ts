import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';

@Component({
    selector: 'app-add-conclusion-form',
    templateUrl: './add-conclusion-form.component.html',
    styleUrls: ['./add-conclusion-form.component.scss']
})
export class AddConclusionFormComponent implements OnInit {

    today = new Date();

    constructor(
        public dialogRef: MatDialogRef<AddConclusionFormComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: ConclusionData,
    ) { }

    ngOnInit(): void {
        if (!this.data.searchConceptsLocallyFF) {
            this.data.ambulatoryConsultationProblemsService.conclusions$.subscribe((snomedConcept) => {
                if (snomedConcept) {
                    this.dialogRef.close()
                    this.data.ambulatoryConsultationProblemsService.addToList(this.data.epidemiologicalReportFF, true);
                    this.data.ambulatoryConsultationProblemsService.resetConclusion()
                }
            })
        }
    }

    searchProblem(event): void {
        this.data.ambulatoryConsultationProblemsService.openConclusionSearchDialog(event);
    }

    resetForm(): void {
        this.data.ambulatoryConsultationProblemsService.resetForm();
    }

}

interface ConclusionData {
    ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
    searchConceptsLocallyFF: boolean,
    epidemiologicalReportFF?: boolean
}
