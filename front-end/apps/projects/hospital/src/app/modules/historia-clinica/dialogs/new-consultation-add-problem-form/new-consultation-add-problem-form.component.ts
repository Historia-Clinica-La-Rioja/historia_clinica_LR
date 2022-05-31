import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';

@Component({
  selector: 'app-new-consultation-add-problem-form',
  templateUrl: './new-consultation-add-problem-form.component.html',
  styleUrls: ['./new-consultation-add-problem-form.component.scss']
})
export class NewConsultationAddProblemFormComponent {

  today = new Date();
  minDate = MIN_DATE;

  constructor(
    public dialogRef: MatDialogRef<NewConsultationAddProblemFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      readOnlyProblem: boolean,
      ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
      severityTypes: any[],
      epidemiologicalReportFF: boolean,
      searchConceptsLocallyFF: boolean,
    },
  ) { }

  editProblema(): void {
    if (this.data.ambulatoryConsultationProblemsService.editProblem()) {
      this.data.readOnlyProblem = false;
    }
    this.dialogRef.close();
  }

  addProblem(): void {
    this.data.ambulatoryConsultationProblemsService.addToList(this.data.epidemiologicalReportFF);
    this.dialogRef.close();
  }

  close(): void {
    if (!this.data.readOnlyProblem) {
      this.data.ambulatoryConsultationProblemsService.resetForm()
    }
    this.dialogRef.close()
  }

}
