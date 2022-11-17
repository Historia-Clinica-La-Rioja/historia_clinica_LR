import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-antecedentes-personales',
	templateUrl: './antecedentes-personales.component.html',
	styleUrls: ['./antecedentes-personales.component.scss']
})
export class AntecedentesPersonalesComponent implements OnInit {

	private personalHistoriesValue: HealthHistoryConditionDto[];

	@Output() personalHistoriesChange = new EventEmitter();

	@Input()
	set personalHistories(personalHistories: HealthHistoryConditionDto[]) {
		this.personalHistoriesValue = personalHistories;
		this.personalHistoriesChange.emit(this.personalHistoriesValue);
	}

	get personalHistories(): HealthHistoryConditionDto[] {
		return this.personalHistoriesValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;

	// Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PERSONAL_HISTORY',
			text: ap => ap.snomed.pt
		}
	];
	displayedColumns: string[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private snomedService: SnomedService
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const antecedentePersonal: HealthHistoryConditionDto = {
				startDate: null,
				note: null,
				snomed: this.snomedConcept
			};
			this.add(antecedentePersonal);
			this.resetForm();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	add(ap: HealthHistoryConditionDto): void {
		this.personalHistories = pushTo<HealthHistoryConditionDto>(this.personalHistories, ap);
	}

	remove(index: number): void {
		this.personalHistories = removeFrom<HealthHistoryConditionDto>(this.personalHistories, index);
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.PERSONAL_RECORD
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}


}
