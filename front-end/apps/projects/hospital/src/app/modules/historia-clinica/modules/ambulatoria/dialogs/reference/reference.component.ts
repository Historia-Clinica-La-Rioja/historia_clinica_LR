import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
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
	specialties$: Observable<any[]>;
	careLines$: Observable<any[]>;
	isAssociatedProblemSet = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
	) { }

	ngOnInit(): void {
		this.formReference = this.formBuilder.group({
			problems: [null],
			consultation: [null],
			procedure: [null],
			careLine: [null],
			clinicalSpecialtyId: [null],
			summary: [null],
		});
		this.setProblems();
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
		})
		);
	}

	get associatedProblemsControls(): FormControl {
		return this.formReference.get('problems') as FormControl;
	}
}
