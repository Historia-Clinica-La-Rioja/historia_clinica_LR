import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { SnomedSemanticSearch, SnomedService } from '../../../../services/snomed.service';

@Component({
	selector: 'app-antecedentes-familiares',
	templateUrl: './antecedentes-familiares.component.html',
	styleUrls: ['./antecedentes-familiares.component.scss']
})
export class AntecedentesFamiliaresComponent implements OnInit {

	private familyHistoriesValue: HealthHistoryConditionDto[];

	@Output() familyHistoriesChange = new EventEmitter();

	@Input()
	set familyHistories(familyHistories: HealthHistoryConditionDto[]) {
		this.familyHistoriesValue = familyHistories;
		this.familyHistoriesChange.emit(this.familyHistoriesValue);
	}

	get familyHistories(): HealthHistoryConditionDto[] {
		return this.familyHistoriesValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	//Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
			text: af => af.snomed.pt
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
			const antecedenteFamiliar: HealthHistoryConditionDto = {
				date: null,
				note: null,
				snomed: this.snomedConcept
			};
			this.add(antecedenteFamiliar);
			this.resetForm();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	add(af: HealthHistoryConditionDto): void {
		this.familyHistories = pushTo<HealthHistoryConditionDto>(this.familyHistories, af);
	}

	remove(index: number): void {
		this.familyHistories = removeFrom<HealthHistoryConditionDto>(this.familyHistories, index);
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.familyRecord
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

}
