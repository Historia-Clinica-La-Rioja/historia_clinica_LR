import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
	selector: 'app-document-action-reason',
	templateUrl: './document-action-reason.component.html',
	styleUrls: ['./document-action-reason.component.scss']
})
export class DocumentActionReasonComponent implements OnInit {

	form: UntypedFormGroup
	scssTitle: string;
	constructor(
		private readonly formB: UntypedFormBuilder,
		private dialogRef: MatDialogRef<DocumentActionReasonComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { title: string, titleColor?: string, subtitle: string },
	) { }

	ngOnInit(): void {
		this.scssTitle = this.data.titleColor ? this.data.titleColor : 'black';
		this.form = this.formB.group({
			reason: [null, [Validators.required]],
		})
	}

	save() {
		if (this.form.valid)
			this.dialogRef.close(this.form.value.reason)
	}
}
