import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup} from '@angular/forms';
import {  MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ErrorProblemDto, MasterDataDto, ProblemInfoDto } from '@api-rest/api-model';
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

    form: FormGroup<AmendProblemForm>;
    motives: TypeaheadOption<MasterDataDto>[] = [];
    
    constructor(
        @Inject(MAT_DIALOG_DATA) public data: {
			amendProblemData: AmendProblemData,
		},
        public dialogRef: MatDialogRef<AmendProblemComponent>,
        private readonly HCEMasterdataService: HceMasterdataService,
		private readonly externalClinicalHistoryService: ExternalClinicalHistoryService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
    ) { 
    }

    ngOnInit(): void {
        this.form = new FormGroup<AmendProblemForm>({
            motive: new FormControl( null, {nonNullable: true}),
            observations: new FormControl('', {nonNullable: true}),
        });

        this.HCEMasterdataService.getMotives().subscribe(motives => {
            motives.map(motive => {
                this.motives.push(this.masterDataDtoToTypeahead(motive))
            })
        });
    }

    onMotiveSelectionChange(motive: MasterDataDto) {
        this.form.controls.motive.setValue(motive);
    }

    save() {
        this.markAsSaved();
        let observations = this.form.value.observations;
        let motive = this.form.value.motive;
        if (motive && observations){
            let errorData: ErrorData = {
                motiveId: motive.id, 
                observations: observations
            }
            this.markProblemAsError(errorData, this.data.amendProblemData.problemId);
        }
    }

    private markProblemAsError(errorData: ErrorData, problemId: number){
		if(errorData){
			let errorProblem: ErrorProblemDto = {
				errorObservations: errorData.observations,
				errorReasonId: errorData.motiveId,
				id: problemId,
                ...this.data.amendProblemData.problemInfo
			}
			this.externalClinicalHistoryService.markProblemAsError(this.data.amendProblemData.patientId, errorProblem).subscribe(succedeed => {
				if(succedeed){
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({
						allergies: false,
                        personalHistories: false,
						familyHistories: false,
						patientProblems: true,
						patientProblemsByRole: true,
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
        this.form.controls.motive?.markAsTouched();
        this.form.controls.observations?.markAsTouched();
    }
}

export interface ErrorData {
    motiveId: number;
    observations: string;
}

export interface AmendProblemData {
    problemId: number,
    patientId: number,
    problemInfo: ProblemInfoDto[],
}

export interface AmendProblemForm {
    motive: FormControl<MasterDataDto>;
    observations: FormControl<string>;
}