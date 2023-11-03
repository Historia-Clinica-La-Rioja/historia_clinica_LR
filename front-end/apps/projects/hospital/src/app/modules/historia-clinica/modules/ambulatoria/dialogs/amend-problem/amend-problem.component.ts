import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import {  MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ErrorProblemDto, MasterDataDto } from '@api-rest/api-model';
import { ExternalClinicalHistoryService } from '@api-rest/services/external-clinical-history.service';
import { HceMasterdataService } from '@api-rest/services/hce-masterdata.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';

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
        @Inject(MAT_DIALOG_DATA) public data: {
			problemId: number,
            patientId: number,
		},
        public dialogRef: MatDialogRef<AmendProblemComponent>,
		private readonly formBuilder: UntypedFormBuilder,
        private readonly HCEMasterdataService: HceMasterdataService,
		private externalClinicalHistoryService: ExternalClinicalHistoryService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
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
        let observations = this.form.controls.observations.value;
        if (this.motive && observations){
            let errorData: ErrorData = {
                motiveId: this.motive.id, 
                observations: observations
            }
            this.markProblemAsError(errorData, this.data.problemId);
        }
    }

    private markProblemAsError(errorData: ErrorData, problemId: number){
		if(errorData){
			let errorProblem: ErrorProblemDto = {
				errorObservations: errorData.observations,
				errorReasonId: errorData.motiveId,
				id: problemId
			}
			this.externalClinicalHistoryService.markProblemAsError(this.data.patientId, errorProblem).subscribe(succedeed => {
				if(succedeed){
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({
						allergies: false,
						familyHistories: false,
						personalHistories: true,
						personalHistoriesByRole: true,
						riskFactors: false,
						medications: false,
						anthropometricData: false,
						problems: true
					});
                    this.dialogRef.close();
				}
			})
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
        this.form.controls.observations?.markAsTouched();
    }
}

export interface ErrorData {
    motiveId: number;
    observations: string;
}