import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { AbstractControl, UntypedFormGroup, Validators } from '@angular/forms';
import { CareLineDto, ClinicalSpecialtyDto, PracticeDto, ReferenceProblemDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ReferenceProblemsService } from '../../services/reference-problems.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { CareLineInstitutionPracticeService } from '@api-rest/services/care-line-institution-practice.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';
import { InstitutionsRulesService } from '../../services/institutions-rules.service';

@Component({
	selector: 'app-carelines-and-specialties-reference',
	templateUrl: './carelines-and-specialties-reference.component.html',
	styleUrls: ['./carelines-and-specialties-reference.component.scss']
})

export class CarelinesAndSpecialtiesReferenceComponent implements OnInit {
	studyCategories$: Observable<any[]>;
	practices = [];
	originalPractices = [];
	submit = false;
	practiceOrProcedureDisabled = true;
	defaultPractice: TypeaheadOption<any>;
	careLines: CareLineDto[] = [];
	allClinicalSpecialties: ClinicalSpecialtyDto[] = [];
	specialtiesSubject$ = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	allSpecialtiesSubject$ = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	specialties$: Observable<ClinicalSpecialtyDto[]>;
	DEFAULT_RADIO_OPTION = true;
	setProblems: any[] = [];

	@Input() set submitForm(submit: boolean) {
		this.submit = submit;
		if (submit && !this.formReference.value.consultation) {
			this.formReference.controls.studyCategory.markAllAsTouched();
			this.formReference.controls.practiceOrProcedure.markAllAsTouched();
		}
	};
	@Input() formReference: UntypedFormGroup;
	@Input() set updateFormFields(problems: ReferenceProblemDto[]) {
		this.setProblems = problems.map(problem => problem.snomed.sctid);
		this.setCareLines();
	}
	@Output() regulationRequiredChange = new EventEmitter<boolean>();

	constructor(
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialty: ClinicalSpecialtyService,
		private readonly referenceProblemsService: ReferenceProblemsService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly practicesService: PracticesService,
		private readonly careLineInstitutionPracticeService: CareLineInstitutionPracticeService,
		private readonly institutionsRulesService: InstitutionsRulesService
	) { }

	ngOnInit() {
		this.setupFormControls();
		this.subscribeToFormChanges();
		this.setInitialFormState();
	}

	setSpecialtyCareLine() {
		this.formReference.controls.clinicalSpecialties.reset();

		const careLine = this.formReference.value.careLine;
		if (careLine) {
			this.formReference.controls.clinicalSpecialties.enable();
			this.formReference.controls.studyCategory.enable();
			this.formReference.controls.practiceOrProcedure.enable();
			this.practiceOrProcedureDisabled = false;
			this.specialtiesSubject$.next(this.formReference.value.careLine.clinicalSpecialties);
		} else {
			this.formReference.controls.studyCategory.disable();
			this.practiceOrProcedureDisabled = true;
		}
		this.formReference.controls.studyCategory.updateValueAndValidity();
	}

	setSpecialty() {
		this.formReference.controls.practiceOrProcedure.setValue(null);
		this.formReference.controls.practiceOrProcedure.reset();
		this.formReference.controls.clinicalSpecialties.setValue(null);
		this.formReference.controls.clinicalSpecialties.reset();
	}

	clearFormFields() {
		this.specialtiesSubject$.next(null);
	}

	setCareLines() {
		this.formReference.controls.careLine.reset();

		if (this.formReference.value.searchByCareLine) {
			this.formReference.controls.clinicalSpecialties.reset();
			this.formReference.controls.clinicalSpecialties.disable();
		}

		if (this.setProblems.length > 0) {
			this.careLineService.getByProblemSnomedSctids(this.setProblems).subscribe((careLine: CareLineDto[]) => {
				this.careLines = careLine;
			});
		}

		if (this.setProblems.length > 0) {
			this.formReference.controls.careLine.enable();
		} else {
			this.formReference.controls.careLine.disable();
		}

	}

	selectedOption($event: SearchCriteria) {
		this.formReference.controls.consultation.setValue(!!!$event);
		if ($event === SearchCriteria.PRACTICES) {
			this.formReference.controls.clinicalSpecialties.removeValidators([Validators.required]);
			this.formReference.controls.clinicalSpecialties.setValue(null);
			this.formReference.controls.practiceOrProcedure.setValidators([Validators.required]);
			this.formReference.controls.studyCategory.setValidators([Validators.required]);
		}
		else {
			this.formReference.controls.practiceOrProcedure.removeValidators([Validators.required]);
			this.formReference.controls.practiceOrProcedure.setValue(null);
			this.formReference.controls.studyCategory.removeValidators([Validators.required]);
			this.formReference.controls.studyCategory.setValue(null);
			this.formReference.controls.clinicalSpecialties.setValue(null);
			this.formReference.controls.clinicalSpecialties.setValidators([Validators.required]);
		}

		this.formReference.controls.practiceOrProcedure.updateValueAndValidity();
		this.formReference.controls.clinicalSpecialties.updateValueAndValidity();
		this.formReference.controls.studyCategory.updateValueAndValidity();
	}

	onPracticeSelectionChange($event: any) {
		const practice = this.originalPractices.find((p: PracticeDto) => p.id === $event);
		this.formReference.controls.practiceOrProcedure.setValue(practice);
		this.regulationRequiredEmmiter();
	}

	onSpecialtySelectionChange() {
		if(this.formReference.value.consultation)
			this.regulationRequiredEmmiter();
	}

	private regulationRequiredEmmiter(){
		const specialtiesIds = this.getClinicalSpecialtiesIds();
		const practiceId = this.formReference.getRawValue().practiceOrProcedure?.id;
		const isConsultation = this.formReference.value.consultation;

		if ((isConsultation && specialtiesIds.length) || (!isConsultation && practiceId)){
			this.institutionsRulesService.validateRegulation(specialtiesIds,practiceId).subscribe(value =>{
				this.regulationRequiredChange.emit(value);
			});
		}
		else {
			this.regulationRequiredChange.emit(false);
		}
	}

	private getClinicalSpecialtiesIds(): number[] {
		return this.formReference.value.clinicalSpecialties?.map(specialty => specialty.id)
	}

	clear(control: AbstractControl) {
		control.reset();
		const controls = this.formReference.controls;
		controls.practiceOrProcedure.setValue(null);
		controls.practiceOrProcedure.updateValueAndValidity();
		this.defaultPractice = this.clearTypeahead();
	}

	private setAllSpecialties() {
		this.clinicalSpecialty.getClinicalSpecialtiesInAllInstitutions().subscribe((clinicalSpecialties: ClinicalSpecialtyDto[]) => {
			this.allClinicalSpecialties = clinicalSpecialties;
			this.allSpecialtiesSubject$.next(clinicalSpecialties);
		});
	}

	private setupFormControls() {
		const formControls = this.formReference.controls;

		formControls.searchByCareLine.valueChanges.subscribe((changes) => {
			if (!changes) {
				this.loadPracticesFromInstitutions();
			}
		});

		formControls.careLine.valueChanges.subscribe((changes) => {
			if (changes) {
				this.loadPracticesByCareLine(changes.id);
			}
		});

		formControls.problems.valueChanges.subscribe(() => {
			this.resetFormFields();
		});
	}

	private loadPracticesFromInstitutions() {
		this.practicesService.getPracticesFromInstitutions().subscribe(
			(practices) =>
				this.handlePracticesLoad(practices)

		);
	}

	private loadPracticesByCareLine(careLineId: number) {
		this.careLineInstitutionPracticeService.getPracticesByCareLine(careLineId).subscribe(
			(practices) =>
				this.handlePracticesLoad(practices)

		);
	}

	private handlePracticesLoad(practices: any[]) {
		this.originalPractices = practices;
		this.practices = this.toTypeaheadOptions(practices, 'pt');
	}

	private resetFormFields() {
		const formControls = this.formReference.controls;
		formControls.careLine.setValue(null);
		formControls.careLine.disable();
		formControls.clinicalSpecialties.setValue(null);
		formControls.clinicalSpecialties.disable();
		formControls.practiceOrProcedure.setValue(null);
		formControls.practiceOrProcedure.disable();
		this.practiceOrProcedureDisabled = !!formControls.searchByCareLine.value;
		this.defaultPractice = this.clearTypeahead();
	}

	private setInitialFormState() {
		const formControls = this.formReference.controls;
		formControls.studyCategory.disable();
		formControls.practiceOrProcedure.disable();
		this.setAllSpecialties();
	}

	private subscribeToFormChanges() {
		this.studyCategories$ = this.requestMasterDataService.categories();
		this.subscribesToChangesInForm();
		this.formReference.controls.studyCategory.disable();
		this.formReference.controls.practiceOrProcedure.disable();
		this.setAllSpecialties();
	}

	private clearTypeahead() {
		return { value: null, viewValue: null, compareValue: null }
	}

	private updateClinicalSpecialtyFormField() {
		this.formReference.controls.clinicalSpecialties.reset();
		this.formReference.controls.clinicalSpecialties.disable();
		this.formReference.controls.careLine.setValidators([Validators.required]);
		this.formReference.controls.clinicalSpecialties.updateValueAndValidity();
	}

	private updateCareLineFormField() {
		this.formReference.controls.careLine.removeValidators([Validators.required]);
		this.formReference.controls.careLine.setValue(null);
		this.formReference.controls.careLine.updateValueAndValidity();
	}

	private toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
		return response.map(r => {
			return {
				value: r.id,
				compareValue: r[attribute],
				viewValue: r[attribute]
			}
		})
	}

	private setSpecialties() {
		const institutionId = this.formReference.value.institutionDestinationId;
		if (institutionId) {
			this.formReference.controls.clinicalSpecialties.enable();
			this.specialties$ = this.clinicalSpecialty.getClinicalSpecialtyByInstitution(institutionId);
			this.formReference.controls.clinicalSpecialties.updateValueAndValidity();
		}
	}

	private subscribesToChangesInForm() {
		this.formReference.controls.searchByCareLine.valueChanges.subscribe(option => {
			if (option === this.DEFAULT_RADIO_OPTION) {
				disableInputs(this.formReference, this.referenceProblemsService.mapProblems());
				this.updateClinicalSpecialtyFormField();
				this.defaultPractice = this.clearTypeahead();
				if (!this.formReference.controls.careLine.value)
					this.practiceOrProcedureDisabled = true;
			} else {
				this.practiceOrProcedureDisabled = false;
				this.formReference.controls.clinicalSpecialties.enable();
				this.updateCareLineFormField();
				this.setSpecialties();
				this.formReference.controls.studyCategory.enable();
				this.formReference.controls.studyCategory.updateValueAndValidity();
				this.formReference.controls.practiceOrProcedure.enable();
			}
			this.formReference.controls.practiceOrProcedure.enable();
			this.formReference.controls.practiceOrProcedure.updateValueAndValidity();
			function disableInputs(formReference: UntypedFormGroup, referenceProblemDto: ReferenceProblemDto[]) {
				if (referenceProblemDto.length === 0) {
					formReference.controls.careLine.disable();
				}
			}
		});
	}
}
