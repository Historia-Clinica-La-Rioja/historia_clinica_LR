import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';

@Component({
    selector: 'app-derive-report',
    templateUrl: './derive-report.component.html',
    styleUrls: ['./derive-report.component.scss']
})
export class DeriveReportComponent implements OnInit {

    constructor(public dialogRef: MatDialogRef<DeriveReportComponent>,
        public dialog: MatDialog) { }

    ngOnInit(): void {
    }

    closeDialog() {
        this.dialogRef.close()
    }

    openDeriveStatusPopUp() {
        this.closeDialog()
        const dialogRef = this.dialog.open(StudyStatusPopupComponent, {
            width: '30%',
            autoFocus: false,
            data: {
                icon: 'subdirectory_arrow_right',
                iconColor: 'lightgrey',
                popUpMessage: 'Clinica Chacabuco',
                popUpMessageTranslate: 'image-network.worklist.REPORT_REFERRED',
                acceptBtn: true,
                iconCircle: true
            }
        });
        dialogRef.afterClosed().subscribe();
    }
}
