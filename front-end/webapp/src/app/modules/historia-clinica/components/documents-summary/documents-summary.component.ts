import { Component, Input, OnInit } from '@angular/core';
import { DOCUMENTS, DOCUMENTS_SEARCH_FIELDS } from '../../constants/summaries';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn} from '@angular/forms';
import { Moment } from 'moment';
import { DocumentSearchFilterDto, EDocumentSearch, DocumentSearchDto, DocumentHistoricDto } from '@api-rest/api-model';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { EvolutionNotesListenerService } from '../../modules/internacion/services/evolution-notes-listener.service';
import { hasError } from '@core/utils/form.utils';
import {pairwise, startWith} from 'rxjs/operators';

@Component({
	selector: 'app-documents-summary',
	templateUrl: './documents-summary.component.html',
	styleUrls: ['./documents-summary.component.scss']
})
export class DocumentsSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

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
		private evolutionNotesListenerService: EvolutionNotesListenerService,
	) {
		evolutionNotesListenerService.history$
			.subscribe(documents => {
				this.documentHistoric = documents;
				this.updateDocuments();
				this.activeDocument = undefined;
			});
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			text: [''],
			date: [null],
			field: [null],
			mainDiagnosisOnly: [false],
		}, {
			validators: this.filterFieldIsRequiredWhenInputIsSet()
		});

		this.evolutionNotesListenerService.initializeEvolutionNoteFilterResult(this.internmentEpisodeId);

		this.setInputResetBehaviour();
	}

	search(): void {
		if (this.form.valid) {
			this.searchTriggered = true;
			const searchFilter: DocumentSearchFilterDto = this.buildSearchFilter();
			this.evolutionNotesListenerService.setSerchFilter(searchFilter);
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
		this.documentsToShow = this.documentHistoric.documents.filter(document => {
			return this.form.value.mainDiagnosisOnly ? document.mainDiagnosis.length : true;
		});
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
			field.setErrors(error ? {filterFieldIsRequiredWhenInputIsSet: true} : null);
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


