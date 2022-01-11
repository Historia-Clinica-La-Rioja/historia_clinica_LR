import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, DateDto, HCEPersonalHistoryDto, InstitutionBasicInfoDto, ReferenceDto, ReferenceProblemDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { forkJoin, Observable, of } from 'rxjs';
@Component({
	selector: 'app-reference',
	templateUrl: './reference.component.html',
	styleUrls: ['./reference.component.scss']
})
export class ReferenceComponent implements OnInit {

	formReference: FormGroup;
	problemsList$: Observable<any[]>;
	problemsList: any[] = [];
	specialties$: Observable<ClinicalSpecialtyDto[]>;
	careLines$: Observable<CareLineDto[]>;
	careLineId: number;
	specialtyId: number;
	referenceProblemDto: ReferenceProblemDto[] = [];
	institutions$: Observable<InstitutionBasicInfoDto[]>;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];

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
	}

	setProblems() {
		
		const consultationProblems = this.data.consultationProblems.map(consultationProblem => {
			return {
				hcePersonalHistoryDto: this.buildPersonalHistoryDto(consultationProblem),
				chronic: consultationProblem.cronico,
			}
		});

		consultationProblems.forEach(problem => this.problemsList.push(problem));

		const activeProblems$ = this.hceGeneralStateService.getActiveProblems(this.data.patientId);

		const chronicProblems$ = this.hceGeneralStateService.getChronicConditions(this.data.patientId);

		forkJoin([activeProblems$, chronicProblems$]).subscribe(([activeProblems, chronicProblems]) => {
			const chronicProblemsHCEPersonalHistory = chronicProblems.map(chronicProblem => {
				return {
					hcePersonalHistoryDto: chronicProblem,
					chronic: true,
				}
			});

			const activeProblemsHCEPersonalHistory = activeProblems.map(activeProblem => {
				return {
					hcePersonalHistoryDto: activeProblem,
					chronic: null,
				}
			});

			const problems = [...activeProblemsHCEPersonalHistory, ...chronicProblemsHCEPersonalHistory];
			problems.forEach((problem: HCEPersonalHistory) => {
				const existProblem = this.problemsList.find(consultationProblem => consultationProblem.snomed?.sctid === problem.hcePersonalHistoryDto.snomed.sctid);
				if (!existProblem) {
					this.problemsList.push(problem);
				}
			});
			this.problemsList$ = of(this.problemsList);
		});
	}

	private mapProblems(problems: any[]): ReferenceProblemDto[] {
		return problems.map(problem => ({
			id: problem.id,
			snomed: problem.snomed,
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
		this.referenceProblemDto = problemsArray.map(problem => ({
			id: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.id,
			snomed: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.snomed,
		}));
	}

	save(): void {
		if (this.formReference.valid) {
			const reference = { data: this.buildReference(), files: this.selectedFiles, problems: this.getReferenceProblems() };
			this.dialogRef.close(reference);
		}
	}

	private buildReference(): ReferenceDto {
		return {
			careLineId: this.careLineId,
			clinicalSpecialtyId: this.specialtyId,
			consultation: true,
			note: this.formReference.value.summary,
			problems: this.mapProblems(this.referenceProblemDto),
			procedure: false,
			fileIds: []
		}
	}

	onSelectFileFormData($event): void {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

	private getReferenceProblems(): HCEPersonalHistory[] {
		let referenceProblems: HCEPersonalHistory[] = [];
		this.referenceProblemDto.forEach(referenceProblemDto => {
			const problemToAdd = this.problemsList.find(problem => problem.hcePersonalHistoryDto.snomed.sctid === referenceProblemDto.snomed.sctid)
			referenceProblems.push(problemToAdd);

		});
		return referenceProblems;
	}

	private buildPersonalHistoryDto(problem): HCEPersonalHistoryDto {
		return {
			hasPendingReference: false,
			inactivationDate: null,
			severity: problem.codigoSeveridad,
			startDate: (problem.fechaInicio?.day) ? this.mapToString(problem.fechaInicio) : problem.fechaInicio,
			snomed: problem.snomed
		}
	}

	private mapToString(date: DateDto): string {
		return date.year.toString() + date.month.toString() + date.day.toString();

	}

}

export interface HCEPersonalHistory {
	hcePersonalHistoryDto: HCEPersonalHistoryDto;
	chronic: boolean
}
