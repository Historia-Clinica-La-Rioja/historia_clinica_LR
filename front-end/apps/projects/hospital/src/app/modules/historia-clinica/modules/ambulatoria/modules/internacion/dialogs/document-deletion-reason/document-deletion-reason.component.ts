import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
	selector: 'app-document-deletion-reason',
	templateUrl: './document-deletion-reason.component.html',
	styleUrls: ['./document-deletion-reason.component.scss']
})
export class DocumentDeletionReasonComponent implements OnInit {

	form: FormGroup
	constructor(
		private readonly formB: FormBuilder,
		private dialogRef: MatDialogRef<DocumentDeletionReasonComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { title: string },
	) { }

	ngOnInit(): void {
		this.form = this.formB.group({
			reason: [null, [Validators.required]],
		})
	}

	save() {
		if (this.form.valid)
			this.dialogRef.close(this.form.value.reason)
	}
}
