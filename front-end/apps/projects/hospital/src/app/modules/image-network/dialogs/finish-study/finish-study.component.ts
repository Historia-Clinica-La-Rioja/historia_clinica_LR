import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent implements OnInit {

	constructor(
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService) {
	}

	ngOnInit(): void {
	}

	closeDialog() {
		this.dialogRef.close()
	}
}
