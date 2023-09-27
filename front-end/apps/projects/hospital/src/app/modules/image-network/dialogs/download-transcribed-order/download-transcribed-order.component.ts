import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ViewPdfBo } from '@presentation/dialogs/view-pdf/view-pdf.service';

@Component({
    selector: 'app-download-transcribed-order',
    templateUrl: './download-transcribed-order.component.html',
    styleUrls: ['./download-transcribed-order.component.scss']
})
export class DownloadTranscribedOrderComponent implements OnInit {

    constructor(
        private dialogRef: MatDialogRef<DownloadTranscribedOrderComponent>,
        @Inject(MAT_DIALOG_DATA)
        public data: ViewPdfBo[]) {
    }

    ngOnInit(): void {
    }

    closeModal(): void {
		this.dialogRef.close();
	}

    downloadOrder(document: ViewPdfBo) {
        window.open(document.url, '_self');
    }
}
