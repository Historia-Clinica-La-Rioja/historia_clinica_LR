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

    downloadOrder(doc: ViewPdfBo) {
        const anchor = document.createElement("a");
        anchor.href = doc.url.toString();
        anchor.download = doc.filename;

        document.body.appendChild(anchor);
        anchor.click();
        document.body.removeChild(anchor);
    }
}
