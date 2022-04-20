import { ChangeDetectorRef, Component, Input, OnChanges, OnInit } from '@angular/core';
import { DOCUMENTS, DOCUMENTS_SEARCH_FIELDS } from '../../constants/summaries';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Moment } from 'moment';
import {
	DocumentSearchFilterDto,
	EDocumentSearch,
	DocumentSearchDto,
	DocumentHistoricDto,
} from '@api-rest/api-model';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { pairwise, startWith } from 'rxjs/operators';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-documents-summary',
	templateUrl: './documents-summary.component.html',
	styleUrls: ['./documents-summary.component.scss']
})
export class DocumentsSummaryComponent implements OnInit, OnChanges {

	@Input() internmentEpisodeId: number;
	@Input() clinicalEvaluation: DocumentHistoricDto;

	public searchFields: SearchField[] = DOCUMENTS_SEARCH_FIELDS;
	public documentsToShow: DocumentSearchDto[];
	public readonly documentsSummary = DOCUMENTS;
	public today: Moment = newMoment();
	public form: FormGroup;
	public activeDocument;
	public documentHistoric: DocumentHistoricDto;
	public searchTriggered = false;
	public hasError = hasError;
	constructor(
		private formBuilder: FormBuilder,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private changeDetectorRef: ChangeDetectorRef,
	) {
		this.form = this.formBuilder.group({
			text: [''],
			date: [null],
			field: [null],
			mainDiagnosisOnly: [false],
			documentsWithoutDiagnosis: [false],
		}, {
			validators: this.filterFieldIsRequiredWhenInputIsSet()
		});

		this.documentHistoric = this.clinicalEvaluation;
		if (this.documentHistoric?.documents.length) {
			this.updateDocuments();
		}
		this.activeDocument = undefined;

	}

	ngOnChanges() {
		this.documentHistoric = this.clinicalEvaluation;
		if (this.documentHistoric?.documents.length) {
			this.updateDocuments();
		}
		this.activeDocument = undefined;
	}

	ngOnInit(): void {

		this.internmentSummaryFacadeService.initializeEvolutionNoteFilterResult(this.internmentEpisodeId);

		this.setInputResetBehaviour();
	}

	search(): void {
		if (this.form.valid) {
			this.searchTriggered = true;
			const searchFilter: DocumentSearchFilterDto = this.buildSearchFilter();
			this.internmentSummaryFacadeService.setSerchFilter(searchFilter);
		}
	}

	private buildSearchFilter(): DocumentSearchFilterDto {
		if (this.isDate(this.form.value.field) && !this.form.value.date) {
			return null;
		} else if (!this.isDate(this.form.value.field) && this.form.value.text === '') {
			return null;
		} else {
			return {
				plainText: this.isDate(this.form.value.field) ? momentFormat(this.form.value.date, DateFormat.API_DATE)
					: this.form.value.text,
				searchType: this.form.value.field,
			};
		}
	}

	private isDate(field): boolean {
		return field === 'CREATED_ON';
	}

	setActive(document) {
		this.activeDocument = document;
	}

	updateDocuments() {
		this.form.patchValue({documentsWithoutDiagnosis: false})
		this.activeDocument = null;
		this.documentsToShow = this.documentHistoric.documents.filter(document => {
			return this.form.value.mainDiagnosisOnly ? document.mainDiagnosis.length : true;
		});
		this.changeDetectorRef.detectChanges();
	}

	showDocumentsWithoutDiagnosis() {
		this.form.patchValue({mainDiagnosisOnly: false})
		this.activeDocument = null;
		this.documentsToShow = this.documentHistoric.documents.filter(document => {
			return this.form.value.documentsWithoutDiagnosis ? !document.diagnosis.length && !document.mainDiagnosis.length : true;
		});
		this.changeDetectorRef.detectChanges();
	}

	viewEvolutionNote(): boolean {
		return (this.activeDocument?.notes || this.activeDocument?.procedures.length > 0);
	}

	setFilterValueAndSearchIfEmptyForm(control: AbstractControl, value: string) {
		control.setValue(value);
		if (this.form.value.text === '' && !this.form.value.field) {
			this.search();
		}
	}

	filterFieldIsRequiredWhenInputIsSet(): ValidatorFn {
		return (control: AbstractControl): ValidationErrors | null => {
			const text = control.get('text');
			const field = control.get('field');
			const error = (text.value !== '') && (field.value === null);
			field.setErrors(error ? { filterFieldIsRequiredWhenInputIsSet: true } : null);
			return null;
		};
	}

	private setInputResetBehaviour() {
		this.form.controls.field.valueChanges.pipe(
			startWith(null as string),
			pairwise()
		).subscribe(
			([oldValue, newValue]) => {
				if (oldValue && newValue) {
					this.form.controls.text.setValue('');
					this.form.controls.date.setValue(null);
				}
			}
		);
	}
}

export interface SearchField {
	field: EDocumentSearch;
	label: string;
}


