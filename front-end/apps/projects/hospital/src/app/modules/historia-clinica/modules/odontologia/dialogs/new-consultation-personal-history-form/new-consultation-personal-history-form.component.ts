import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { PersonalHistoriesNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service';

@Component({
  selector: 'app-new-consultation-personal-history-form',
  templateUrl: './new-consultation-personal-history-form.component.html',
  styleUrls: ['./new-consultation-personal-history-form.component.scss']
})
export class NewConsultationPersonalHistoryFormComponent {

  readonly minDate = MIN_DATE;

  constructor(
    public dialogRef: MatDialogRef<NewConsultationPersonalHistoryFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: PersonalHistoryData,
  ) { }

  addPersonalHistory(): void {
    if (this.data.personalHistoryService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.personalHistoryService.resetForm();
    this.dialogRef.close()
  }

}

interface PersonalHistoryData {
  personalHistoryService: PersonalHistoriesNewConsultationService,
  searchConceptsLocallyFF: boolean,
}
