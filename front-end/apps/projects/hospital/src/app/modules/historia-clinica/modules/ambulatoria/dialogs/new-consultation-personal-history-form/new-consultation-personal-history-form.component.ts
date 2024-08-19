import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NewConsultationPersonalHistoriesService } from '../../services/new-consultation-personal-histories.service';
import { MIN_DATE } from '../../modules/internacion/routes/new-internment/new-internment.component';
import { FormGroup } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { HceMasterdataService } from '@api-rest/services/hce-masterdata.service';
import { MasterDataDto } from '@api-rest/api-model';

@Component({
    selector: 'app-new-consultation-personal-history-form',
    templateUrl: './new-consultation-personal-history-form.component.html',
    styleUrls: ['./new-consultation-personal-history-form.component.scss']
})

export class NewConsultationPersonalHistoryFormComponent {
    
    form: FormGroup;
    minDate = MIN_DATE;
	hasError = hasError;
    historyTypeList: MasterDataDto[];
    markAsTouched = false;

    constructor(
        public dialogRef: MatDialogRef<NewConsultationPersonalHistoryFormComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: PersonalHistoryData,
        private readonly HCEMasterDataService: HceMasterdataService) { }

    ngOnInit() {
        this.form = this.data.personalHistoryService.getForm();

        this.HCEMasterDataService.getPersonalHistoryTypes().subscribe(types => {
            this.historyTypeList = types;
        });
    }

    addPersonalHistory(): void {
        this.markAsTouched = true;
        if (this.data.personalHistoryService.addToList()) {
            this.dialogRef.close();
        }
    }

    close(): void {
        this.data.personalHistoryService.resetForm();
        this.dialogRef.close()
    }

    startDateChanged(date: Date) {
        this.data.personalHistoryService.getForm().controls.startDate.setValue(date);
    }
    endDateChanged(date: Date) {
        this.data.personalHistoryService.getForm().controls.endDate.setValue(date);
    }
}

interface PersonalHistoryData {
    personalHistoryService: NewConsultationPersonalHistoriesService,
    searchConceptsLocallyFF: boolean,
}