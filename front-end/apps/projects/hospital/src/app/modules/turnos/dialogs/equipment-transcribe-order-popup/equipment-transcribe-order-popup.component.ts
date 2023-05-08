import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnomedECL } from '@api-rest/api-model';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { hasError } from '@core/utils/form.utils';

@Component({
    selector: 'app-equipment-transcribe-order-popup',
    templateUrl: './equipment-transcribe-order-popup.component.html',
    styleUrls: ['./equipment-transcribe-order-popup.component.scss']
})
export class EquipmentTranscribeOrderPopupComponent implements OnInit {

    transcribeOrderForm: FormGroup;
    public readonly hasError = hasError;
    readonly studyECL = SnomedECL.PROCEDURE;
    readonly problemECL = SnomedECL.DIAGNOSIS;
    selectedStudy = null;
    selectedProblem = null;
    healthProblems = null;
    selectedFiles: File[] = [];
    selectedFilesShow: any[] = [];

    constructor(
        @Inject(MAT_DIALOG_DATA) public data,
        private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService
        ) { }

    ngOnInit(): void {
        this.transcribeOrderForm = this.formBuilder.group({
            study: [null, Validators.required],
            assosiatedProblem: [null, Validators.required],
            professional: [null, Validators.required],
            institution: [null]
        });

        this.getPatientHealthProblems();
    }

    private getPatientHealthProblems() {
        this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			const activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));

			this.hceGeneralStateService.getChronicConditions(this.data.patientId).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem => ({id: problem.id, description: problem.snomed.pt,  sctId: problem.snomed.sctid}));
				const healthProblems = activeProblemsList.concat(chronicProblemsList);
                this.healthProblems = healthProblems;
			});
		});
    }

    saveOrder() {
        
    }

    isFormValid(): boolean {
        if(this.selectedFiles.length)
            return this.transcribeOrderForm.valid;
        return false
    }

    onFileSelected($event){
        Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
    }

    removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

    handleStudySelected(study) {
		this.selectedStudy = study;
		this.transcribeOrderForm.controls.study.setValue(this.getStudyDisplayName());
	}

    handleProblemSelected(problem) {
		this.selectedProblem = problem;
		this.transcribeOrderForm.controls.assosiatedProblem.setValue(this.getProblemDisplayName());
	}

    getStudyDisplayName(): string {
		return this.selectedStudy?.pt;
	}

    getProblemDisplayName(): string {
		return this.selectedProblem?.pt;
	}

    resetStudySelector() {
        this.selectedStudy = null;
        this.transcribeOrderForm.controls.study.setValue(null);
    }

    resetProblemSelector() {
        this.selectedProblem = null;
        this.transcribeOrderForm.controls.assosiatedProblem.setValue(null);
    }
}
