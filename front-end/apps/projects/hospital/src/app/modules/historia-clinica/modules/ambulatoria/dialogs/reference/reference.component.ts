import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, HCEPersonalHistoryDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { Observable, of } from 'rxjs';
@Component({
	selector: 'app-reference',
	templateUrl: './reference.component.html',
	styleUrls: ['./reference.component.scss']
})
export class ReferenceComponent implements OnInit {

	formReference: FormGroup;
	problemsList$: Observable<any[]>;
	problemsList: any[];
	specialties$: Observable<ClinicalSpecialtyDto[]>;
	careLines$: Observable<CareLineDto[]>;
	careLineId: number;
	specialtyId: number;
	problemsReference: any[];
	institutions$: Observable<InstitutionBasicInfoDto[]>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialtyCareLine: ClinicalSpecialtyCareLineService,
		private readonly dialogRef: MatDialogRef<ReferenceComponent>,
		private readonly institutionService: InstitutionService,
	) { }

	ngOnInit(): void {
		this.formReference = this.formBuilder.group({
			problems: [null, [Validators.required]],
			consultation: [null],
			procedure: [null],
			careLine: [null, [Validators.required]],
			clinicalSpecialtyId: [null, [Validators.required]],
			institution: [null],
			summary: [null],
		});
		this.setProblems();

		this.formReference.controls.clinicalSpecialtyId.disable();
		this.formReference.controls.procedure.disable();

		this.careLines$ = this.careLineService.getCareLines();

		this.institutions$ = this.institutionService.getAllInstitutions();

		this.problemsReference = [];
	}

	setProblems() {
		this.hceGeneralStateService.getActiveProblems(this.data.idPatient).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			const activeProblemsList = this.mapProblems(activeProblems);

			this.hceGeneralStateService.getChronicConditions(this.data.idPatient).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
				const chronicProblemsList = this.mapProblems(chronicProblems);

				const newConsultationProblems = this.mapProblems(this.data.newConsultationProblems);
				this.problemsList = activeProblemsList.concat(newConsultationProblems, chronicProblemsList);
				this.problemsList$ = of(this.problemsList);
			});
		});
	}

	private mapProblems(problems: any[]): any {
		return problems.map(problem =>
		({
			pt: problem.snomed.pt,
			sctid: problem.snomed.sctid,
		}));
	}

	get associatedProblemsControls(): FormControl {
		return this.formReference.get('problems') as FormControl;
	}

	setSpecialtyCareLine(description: string): void {
		if (this.formReference.value.careLine) {
			this.formReference.controls.clinicalSpecialtyId.enable();
			this.formReference.controls.clinicalSpecialtyId.setValidators([Validators.required]);
			this.formReference.updateValueAndValidity();
			this.careLines$.subscribe(
				(careLineArray: CareLineDto[]) => {
					this.careLineId = careLineArray.find(careLine => careLine.description === description).id;
					this.specialties$ = this.clinicalSpecialtyCareLine.getSpecialtyCareLine(this.careLineId);
				}
			);
		}
	}

	setSpecialtyId(specialtyName: string) {
		this.specialties$.subscribe(specialties => {
			this.specialtyId = specialties.find(specialty => specialty.name === specialtyName).id;
		})
	}

	setProblemsReference(problemsArray: string[]) {
		this.problemsReference = problemsArray.map(problemPt => ({
			pt: problemPt,
			sctid: this.problemsList.find(problem => problem.pt === problemPt).sctid,
		}));
	}

	save(): void {
		if (this.formReference.valid) {
			const reference = this.buildReference();
			this.dialogRef.close(reference);
		}
	}

	private buildReference(): Reference {
		return {
			problems: this.problemsReference,
			consultation: true,
			procedure: false,
			careLineId: this.careLineId,
			clinicalSpecialtyId: this.specialtyId,
			note: this.formReference.value.summary
		}
	}
}

export interface Reference {
	problems: any[];
	consultation: boolean;
	procedure: boolean;
	careLineId: number,
	clinicalSpecialtyId: number;
	note: string;
}