import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddressDto, CareLineDto, ClinicalSpecialtyDto, DateDto, HCEPersonalHistoryDto, InstitutionBasicInfoDto, ReferenceDto, ReferenceProblemDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { forkJoin, Observable, of } from 'rxjs';


const COUNTRY = 14;

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
	careLines: CareLineDto[];
	departments$: Observable<any[]>;
	provinces$: Observable<any[]>;
	referenceProblemDto: ReferenceProblemDto[] = [];
	institutions$: Observable<InstitutionBasicInfoDto[]>;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	destinationInstitutionId: number;
	DEFAULT_RADIO_OPTION = '0';

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly careLineService: CareLineService,
		private readonly dialogRef: MatDialogRef<ReferenceComponent>,
		private readonly institutionService: InstitutionService,
		private readonly clinicalSpecialty: ClinicalSpecialtyService,
		private readonly adressMasterData: AddressMasterDataService,
		private readonly contextService: ContextService
	) { }

	ngOnInit(): void {
		this.formReference = this.formBuilder.group({
			problems: [null, [Validators.required]],
			searchByCareLine: [this.DEFAULT_RADIO_OPTION],
			provinceId: [null, [Validators.required]],
			departmentId: [null, [Validators.required]],
			consultation: [null],
			procedure: [null],
			careLine: [null, [Validators.required]],
			clinicalSpecialtyId: [null, [Validators.required]],
			destinationInstitutionId: [null, [Validators.required]],
			summary: [null],
		});
		this.setProblems();
		this.formReference.controls.clinicalSpecialtyId.disable();
		this.formReference.controls.procedure.disable();
		this.formReference.controls.careLine.disable();
		this.formReference.controls.destinationInstitutionId.disable();

		this.subscribesToChangesInForm();

		this.provinces$ = this.adressMasterData.getByCountry(COUNTRY);

		const homeInstitutionId = this.contextService.institutionId;

		this.institutionService.getAddress(homeInstitutionId).subscribe((institutionInfo: AddressDto) => {
			const provinceId = institutionInfo?.provinceId;
			if (provinceId) {
				this.formReference.controls.provinceId.setValue(provinceId);
				this.setDepartmentsByProvince(provinceId);
			}
		});
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
				const existProblem = this.problemsList.find(consultationProblem => consultationProblem.hcePersonalHistoryDto.snomed.sctid === problem.hcePersonalHistoryDto.snomed.sctid);
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

	setSpecialtyCareLine(): void {
		const careLineId = this.formReference.value.careLine;
		if (careLineId) {
			this.formReference.controls.clinicalSpecialtyId.enable();
			this.formReference.controls.clinicalSpecialtyId.setValidators([Validators.required]);
			this.formReference.updateValueAndValidity();
			this.specialties$ = this.clinicalSpecialty.getAllByDestinationInstitution(careLineId, this.formReference.value.destinationInstitutionId);
		}
	}


	setProblemsReference(problemsArray: string[]) {
		if (problemsArray.length) {
			this.referenceProblemDto = problemsArray.map(problem => ({
				id: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.id,
				snomed: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.snomed,
			}));
		}
		else {
			this.referenceProblemDto = [];
		}
		this.setInformation();
	}

	save(): void {
		if (this.formReference.valid) {
			const reference = { data: this.buildReference(), files: this.selectedFiles, problems: this.getReferenceProblems() };
			this.dialogRef.close(reference);
		}
	}

	private buildReference(): ReferenceDto {
		return {
			careLineId: this.formReference.value.careLine,
			clinicalSpecialtyId: this.formReference.value.clinicalSpecialtyId,
			consultation: true,
			note: this.formReference.value.summary,
			problems: this.mapProblems(this.referenceProblemDto),
			procedure: false,
			fileIds: [],
			destinationInstitutionId: this.formReference.value.destinationInstitutionId
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

	setInformation() {
		this.clearInformation();
		if (this.formReference.value.searchByCareLine === this.DEFAULT_RADIO_OPTION)
			this.setCareLines();
		else
			this.setSpecialties();
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

	private setCareLines() {
		const problemSnomedIds: string[] = this.referenceProblemDto.map(problem => problem.snomed.sctid);
		const institutionId = this.formReference.value.destinationInstitutionId;
		if (!problemSnomedIds.length || !institutionId) {
			this.formReference.controls.careLine.disable();
		}
		if (problemSnomedIds.length && institutionId) {
			this.formReference.controls.careLine.enable();
			this.formReference.controls.careLine.updateValueAndValidity();
			this.careLineService.getByProblemSnomedIdsAndInstitutionId(institutionId, problemSnomedIds).subscribe(careLines => this.careLines = careLines);
		}
		this.formReference.controls.clinicalSpecialtyId.disable();
	}

	private setSpecialties() {
		const institutionId = this.formReference.value.destinationInstitutionId;
		if (institutionId) {
			this.formReference.controls.clinicalSpecialtyId.enable();
			this.specialties$ = this.clinicalSpecialty.getClinicalSpecialtyByInstitution(institutionId);
			this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
		}
	}

	private subscribesToChangesInForm() {
		this.formReference.controls.searchByCareLine.valueChanges.subscribe(option => {
			if (option === this.DEFAULT_RADIO_OPTION) {
				this.formReference.controls.careLine.setValidators([Validators.required]);
				this.formReference.controls.clinicalSpecialtyId.setValue(null);
				this.formReference.controls.clinicalSpecialtyId.disable();
				this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
				this.setCareLines();
			} else {
				this.formReference.controls.careLine.removeValidators([Validators.required]);
				this.formReference.controls.careLine.setValue(null);
				this.formReference.controls.careLine.disable();
				this.formReference.controls.careLine.updateValueAndValidity();
				this.setSpecialties();
			}
		});
	}

	setDepartmentsByProvince(province: number) {
		this.clearInformation();
		this.formReference.controls.destinationInstitutionId.setValue(null);
		this.formReference.controls.destinationInstitutionId.disable();
		this.departments$ = this.adressMasterData.getDepartmentsByProvince(province);
		this.formReference.controls.departmentId.updateValueAndValidity();
	}

	filterInstitutionsByDepartment(department: number) {
		this.formReference.controls.destinationInstitutionId.setValue(null);
		this.clearInformation();
		this.institutions$ = this.institutionService.findByDepartmentId(department);
		this.formReference.controls.destinationInstitutionId.enable();
		this.formReference.controls.destinationInstitutionId.updateValueAndValidity();
	}

	private clearInformation() {
		if (this.careLines?.length) {
			this.careLines = [];
			this.formReference.controls.careLine.setValue(null);
			this.formReference.controls.careLine.updateValueAndValidity();
		}
		this.specialties$?.subscribe(specialties => {
			if (specialties.length) {
				this.formReference.controls.clinicalSpecialtyId.setValue(null);
				this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
			}
		});
		this.disabledInputs();
	}

	private disabledInputs() {
		if (!this.formReference.value.destinationInstitutionId) {
			this.formReference.controls.clinicalSpecialtyId.disable();
		}
		if (!this.referenceProblemDto.length || !this.formReference.value.destinationInstitutionId) {
			this.formReference.controls.careLine.disable();
		}
	}

}

export interface HCEPersonalHistory {
	hcePersonalHistoryDto: HCEPersonalHistoryDto;
	chronic: boolean
}
