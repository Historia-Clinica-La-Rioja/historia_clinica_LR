import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'app-vital-signs-chart-popup',
    templateUrl: './vital-signs-chart-popup.component.html',
    styleUrls: ['./vital-signs-chart-popup.component.scss']
})
export class VitalSignsChartPopupComponent implements OnInit {

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: vitalSignsChartData,
		public dialogRef: MatDialogRef<VitalSignsChartPopupComponent>,
    ) { }

    ngOnInit(): void {
    }

    close() {
        this.dialogRef.close();
    }
}

export interface vitalSignsChartData {
    imgList: string[],
}