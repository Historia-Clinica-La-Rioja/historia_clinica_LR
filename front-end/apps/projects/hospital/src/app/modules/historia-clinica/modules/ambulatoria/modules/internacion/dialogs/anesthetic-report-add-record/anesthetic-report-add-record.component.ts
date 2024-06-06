import { Component, Inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnestheticReportRecordService } from '../../services/anesthetic-report-record.service';

@Component({
    selector: 'app-anesthetic-report-add-record',
    templateUrl: './anesthetic-report-add-record.component.html',
    styleUrls: ['./anesthetic-report-add-record.component.scss']
})
export class AnestheticReportAddRecordComponent{

    form: FormGroup;

    constructor(
        public dialogRef: MatDialogRef<AnestheticReportAddRecordComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: ReportData,
    ) {
        this.form = this.data.anestheticReportRecordService.getForm();
    }

    close(): void {
        this.dialogRef.close()
    }

    addRecord() {
        this.data.anestheticReportRecordService.addToList();
        this.dialogRef.close();
    }
}

interface ReportData {
    anestheticReportRecordService: AnestheticReportRecordService,
    searchConceptsLocallyFF: boolean,
}