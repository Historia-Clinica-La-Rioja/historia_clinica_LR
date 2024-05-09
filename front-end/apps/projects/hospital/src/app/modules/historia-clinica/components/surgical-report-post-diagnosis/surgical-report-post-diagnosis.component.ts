import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProblemTypeEnum, SurgicalReportDto } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';

@Component({
  selector: 'app-surgical-report-post-diagnosis',
  templateUrl: './surgical-report-post-diagnosis.component.html',
  styleUrls: ['./surgical-report-post-diagnosis.component.scss']
})
export class SurgicalReportPostDiagnosisComponent {
	@Input() surgicalReport: SurgicalReportDto;

  constructor(private readonly dialog: MatDialog) { }

	addDiagnosis(): void {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: false
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				diagnosis.id = null;
				diagnosis.type = ProblemTypeEnum.POSTOPERATIVE_DIAGNOSIS;
				this.surgicalReport.postoperativeDiagnosis = pushIfNotExists(this.surgicalReport.postoperativeDiagnosis, diagnosis, this.compare);
			}
		});
	}

  
	deleteDiagnosis(index: number): void {
		this.surgicalReport.postoperativeDiagnosis = removeFrom(this.surgicalReport.postoperativeDiagnosis, index);
	}

  isEmpty(): boolean{
    return 	!this.surgicalReport.postoperativeDiagnosis?.length;
  }

  private compare(first, second): boolean {
		return first.snomed.sctid === second.snomed.sctid;
	}
}
