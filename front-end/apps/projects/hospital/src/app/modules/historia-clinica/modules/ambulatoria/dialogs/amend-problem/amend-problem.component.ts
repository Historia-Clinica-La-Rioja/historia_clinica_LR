import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import {  MatDialogRef } from '@angular/material/dialog';
import { MasterDataDto } from '@api-rest/api-model';
import { HceMasterdataService } from '@api-rest/services/hce-masterdata.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
    selector: 'app-amend-problem',
    templateUrl: './amend-problem.component.html',
    styleUrls: ['./amend-problem.component.scss']
})
export class AmendProblemComponent implements OnInit {

    form: UntypedFormGroup;
    motives: TypeaheadOption<MasterDataDto>[] = [];
    motive: MasterDataDto;
    submit = false;
    
    constructor(
        public dialogRef: MatDialogRef<AmendProblemComponent>,
		private readonly formBuilder: UntypedFormBuilder,
        private readonly HCEMasterdataService: HceMasterdataService,
    ) { 
    }

    ngOnInit(): void {
        this.form = this.formBuilder.group({
			observations: [null, [Validators.required]],
		});

        this.HCEMasterdataService.getMotives().subscribe(motives => {
            motives.forEach(motive => {
                this.motives.push(this.masterDataDtoToTypeahead(motive))
            })
        });
    }

    onMotiveSelectionChange(motive: MasterDataDto) {
        this.motive = motive;
    }

    save() {
        this.markAsSaved();
        if (this.motive && this.form.get("observations").value){
            this.dialogRef.close(this.motive);
        }
    }

    private masterDataDtoToTypeahead(motive: MasterDataDto): TypeaheadOption<MasterDataDto> {
		return {
			compareValue: motive.description,
			value: motive
		};
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