import { Component, Inject, OnInit, ChangeDetectorRef, AfterContentChecked } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddressDto, DateDto, DepartmentDto, HCEPersonalHistoryDto, InstitutionBasicInfoDto, ReferenceDto, ReferenceProblemDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { forkJoin, Observable, of } from 'rxjs';


const COUNTRY = 14;

@Component({
	selector: 'app-reference',
	templateUrl: './reference.component.html',
	styleUrls: ['./reference.component.scss']
})
export class ReferenceComponent implements OnInit, AfterContentChecked {

	formReference: FormGroup;
	problemsList$: Observable<any[]>;
	problemsList: any[] = [];
	referenceProblemDto: ReferenceProblemDto[] = [];
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	DEFAULT_RADIO_OPTION = '0';
	provinces: TypeaheadOption<any>[];
	submitForm = false;
	originInstitutionInfo: AddressDto;
	originDepartment: DepartmentDto;
	updateSpecialtesAndCarelineFields = false;
	clearCarelinesAndSpecialties = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly dialogRef: MatDialogRef<ReferenceComponent>,
		private readonly institutionService: InstitutionService,
		private readonly adressMasterData: AddressMasterDataService,
		private readonly contextService: ContextService,
		private changeDetector: ChangeDetectorRef
	) { }

	ngOnInit(): void {
		this.createReferenceForm();

		this.setProblems();

		this.disableInputs();

		this.adressMasterData.getByCountry(COUNTRY).subscribe(provinces => {
			this.provinces = this.toTypeaheadOptions(provinces, 'description');

			this.institutionService.getAddress(this.contextService.institutionId).subscribe((institutionInfo: AddressDto) => {
				this.originInstitutionInfo = institutionInfo;
				this.loadOriginInstitutionInfo();
			});
		})
	}

	ngAfterContentChecked(): void {
		this.changeDetector.detectChanges();
	}

	toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
		return response.map(r => {
			return {
				value: r.id,
				compareValue: r[attribute],
				viewValue: r[attribute]
			}
		})
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
		this.updateSpecialtesAndCarelineFields = true;
	}

	save(): void {
		this.submitForm = true;
		this.formReference.controls.careLine.markAllAsTouched();
		this.formReference.controls.clinicalSpecialtyId.markAllAsTouched();
		if (this.formReference.valid) {
			const reference = { data: this.buildReference(), files: this.selectedFiles, problems: this.getReferenceProblems() };
			this.dialogRef.close(reference);
		}
	}

	private buildReference(): ReferenceDto {
		return {
			careLineId: this.formReference.controls.careLine.value,
			clinicalSpecialtyId: this.formReference.controls.clinicalSpecialty.value,
			consultation: true,
			note: this.formReference.value.summary,
			problems: this.mapProblems(this.referenceProblemDto),
			procedure: false,
			fileIds: [],
			destinationInstitutionId: this.formReference.value.institutionDestinationId,
			phonePrefix: this.formReference.value.phonePrefix,
			phoneNumber: this.formReference.value.phoneNumber
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

	onProvinceSelectionChange(province: number) {
		this.formReference.controls.provinceId.setValue(province);
		this.clearCarelinesAndSpecialties = true;
	}

	onDepartmentSelectionChange(department: number) {
		this.formReference.controls.departmentId.setValue(department);
		if (department) {
			this.clearCarelinesAndSpecialties = true;
		}
	}

	onInstitutionSelectionChange(institutionId: number) {
		this.formReference.controls.institutionDestinationId.setValue(institutionId);
		this.updateSpecialtesAndCarelineFields = true;
	}

	resetControls(){
		this.updateSpecialtesAndCarelineFields = false;
		this.clearCarelinesAndSpecialties = false;
		this.changeDetector.detectChanges();
	}

	private loadOriginInstitutionInfo() {
		if (this.originInstitutionInfo.provinceId) {
			this.setOriginProvince(this.originInstitutionInfo.provinceId);
			this.adressMasterData.getDepartmentById(this.originInstitutionInfo.departmentId).subscribe((department: DepartmentDto) => {
				this.setOriginDepartment(department);
				this.institutionService.findByDepartmentId(department.id).subscribe((institutions: InstitutionBasicInfoDto[]) =>
					this.setOriginInstitution(institutions)
				);
			});
		}
		else {
			if (this.originInstitutionInfo.departmentId) {
				const information$: Observable<any>[] = [];
				information$.push(this.adressMasterData.getDepartmentById(this.originInstitutionInfo.departmentId));
				information$.push(this.institutionService.findByDepartmentId(this.originInstitutionInfo.departmentId));
				forkJoin(information$).subscribe(info => {
					this.setOriginProvince(info[0].provinceId);
					this.setOriginDepartment(info[0]);
					this.setOriginInstitution(info[1]);
				});
			}
		}
	}

	private setOriginDepartment(department: DepartmentDto) {
		this.formReference.controls.departmentOrigin.setValue(department.description);
		this.originDepartment = department;
	}

	private createReferenceForm() {
		this.formReference = this.formBuilder.group({
			problems: [null, [Validators.required]],
			searchByCareLine: [this.DEFAULT_RADIO_OPTION],
			provinceId: [null],
			departmentId: [null],
			consultation: [null],
			procedure: [null],
			careLine: [null, [Validators.required]],
			clinicalSpecialty: [null, [Validators.required]],
			institutionDestinationId: [null, [Validators.required]],
			summary: [null],
			provinceOrigin: [null],
			departmentOrigin: [null],
			institutionOrigin: [null],
			phoneNumber: [null, [Validators.required, Validators.maxLength(20)]],
			phonePrefix: [null, [Validators.required, Validators.maxLength(10)]]
		});
	}

	private disableInputs() {
		this.formReference.controls.clinicalSpecialtyId.disable();
		this.formReference.controls.procedure.disable();
		this.formReference.controls.careLine.disable();
		this.formReference.controls.provinceOrigin.disable();
		this.formReference.controls.departmentOrigin.disable();
		this.formReference.controls.institutionOrigin.disable();
	}

	private setOriginProvince(provinceId: number) {
		const provinceOrigin = this.provinces.find(p => p.value === provinceId);
		this.formReference.controls.provinceOrigin.setValue(provinceOrigin.viewValue);
	}

	private setOriginInstitution(institutions: InstitutionBasicInfoDto[]) {
		const institution = institutions.find((i: InstitutionBasicInfoDto) => i.id === this.contextService.institutionId);
		this.formReference.controls.institutionOrigin.setValue(institution.name);
	}

}

export interface HCEPersonalHistory {
	hcePersonalHistoryDto: HCEPersonalHistoryDto;
	chronic: boolean
}
