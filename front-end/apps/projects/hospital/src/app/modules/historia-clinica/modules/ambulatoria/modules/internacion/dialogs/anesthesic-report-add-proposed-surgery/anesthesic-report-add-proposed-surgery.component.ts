import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AnestheticReportProposedSurgeryService } from '../../services/anesthetic-report-proposed-surgery.service';

@Component({
    selector: 'app-anesthesic-report-add-proposed-surgery',
    templateUrl: './anesthesic-report-add-proposed-surgery.component.html',
    styleUrls: ['./anesthesic-report-add-proposed-surgery.component.scss']
})
export class AnesthesicReportAddProposedSurgeryComponent {

    constructor(
        public dialogRef: MatDialogRef<AnesthesicReportAddProposedSurgeryComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: ProposedSurgeryData,
    ) { }

    close(): void {
        this.dialogRef.close()
        this.data.proposedSurgeryService.resetForm();
    }

    addProposedSurgery(){
        this.data.proposedSurgeryService.addToList();
        this.dialogRef.close();
    }
}

interface ProposedSurgeryData {
    proposedSurgeryService: AnestheticReportProposedSurgeryService,
    searchConceptsLocallyFF: boolean,
}