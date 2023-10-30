import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'app-amend-problem',
    templateUrl: './amend-problem.component.html',
    styleUrls: ['./amend-problem.component.scss']
})
export class AmendProblemComponent implements OnInit {

    form: UntypedFormGroup;
    motive: string = '';
    submit = false;
    
    constructor(
        public dialogRef: MatDialogRef<AmendProblemComponent>,
		private readonly formBuilder: UntypedFormBuilder,
    ) { }

    ngOnInit(): void {
        this.form = this.formBuilder.group({
			observations: [null, [Validators.required]],
		});
    }

    onMotiveSelectionChange(motive: string) {
        this.motive = motive;
    }

    save() {
        this.markAsSaved();
        if (this.motive.length && this.form.get("observations").value){
            let error: ErrorMotive = {
                errorReasonId: 1,
                errorObservations: 'Problema erroneo'
            }
            this.dialogRef.close(error);
        }
    }

    private markAsSaved() {
        this.submit = true;
        this.form.get('observations')?.markAsTouched();
    }
}

export interface ErrorMotive {
    errorReasonId: number;
    errorObservations: string;
}