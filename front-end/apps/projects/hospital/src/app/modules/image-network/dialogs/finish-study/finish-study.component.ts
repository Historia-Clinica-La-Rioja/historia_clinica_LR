import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent implements OnInit {

	constructor(
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService,
		public dialog: MatDialog) {
	}

	ngOnInit(): void {
	}

	openStatusDialog() {
		const dialogRef = this.dialog.open(StudyStatusPopupComponent, {
			width: '30%',
			autoFocus: false,
			data: {
				status: true
			}
		});

		dialogRef.afterClosed().subscribe( () => this.closeDialog());
	}

	closeDialog() {
		this.dialogRef.close()
	}
}
