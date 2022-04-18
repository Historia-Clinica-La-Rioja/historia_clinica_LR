import { Injectable } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray, FormControl } from "@angular/forms";
import { SnomedDto } from "@api-rest/api-model";
import { SnomedService, SnomedSemanticSearch } from "@historia-clinica/services/snomed.service";
import { SnomedECL } from "@api-rest/api-model";
import { TableModel, ActionDisplays } from "@presentation/components/table/table.component";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { SnowstormService } from "@api-rest/services/snowstorm.service";

@Injectable({
	providedIn: 'root'
})

export class SearchSnomedConceptsParenteralPlanService {

	salineForm: FormGroup;
	pharmacoForm: FormGroup;
	salineSnomedConcept: SnomedDto;
	searching = false;
	showToSearchSnomedConcept = false;
	showPharmacoTitle = false;
	snowstormServiceNotAvailable = false;
	conceptsResultsTable: TableModel<any>;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snowstormService: SnowstormService,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService
	) {
		this.salineForm = this.formBuilder.group({
			snomed: [null, [Validators.required]]
		});
		this.pharmacoForm = this.formBuilder.group({
			pharmaco: this.formBuilder.array([])
		});
	}

	resetAllForms(): void {
		delete this.salineSnomedConcept;
		this.salineForm.reset();
		this.showPharmacoTitle = false;
		this.showToSearchSnomedConcept = false;
		this.pharmacos.clear();
	}

	setSalineSnomedConcept(selectedConcept: SnomedDto): void {
		this.salineSnomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.salineForm.controls.snomed.setValue(pt);
	}

	private buildConceptsResultsTable(data: SnomedDto[]): TableModel<SnomedDto> {
		return {
			columns: [
				{
					columnDef: '1',
					header: 'Descripción SNOMED',
					text: concept => concept.pt
				},
				{
					columnDef: 'select',
					action: {
						displayType: ActionDisplays.BUTTON,
						display: 'Seleccionar',
						matColor: 'primary',
						do: concept => this.setSalineSnomedConcept(concept)
					}
				},
			],
			data,
			enablePagination: true
		};
	}

	searchSalineConcept(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: SnomedECL.MEDICINE })
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
						this.searching = false;
					},
					error => {
						this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
						this.snowstormServiceNotAvailable = true;
					}
				);
		}
	}

	searchPharmacoConcept(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.MEDICINE
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setPharmacoForm(selectedConcept));
		}
	}

	setPharmacoForm(selectedConcept: SnomedDto): void {
		if (selectedConcept) {
			const pharmacoFormArray = this.pharmacoForm.get('pharmaco') as FormArray;
			pharmacoFormArray.push(this.addPharmaco(selectedConcept));
			this.showToSearchSnomedConcept = false;
		}
	}

	private addPharmaco(selectedConcept: SnomedDto): FormGroup {
		const pt = selectedConcept ? selectedConcept.pt : '';
		const form = this.formBuilder.group({
			snomed: this.formBuilder.group({
				pt: [pt],
				sctid: [selectedConcept.sctid]
			}),
			dose: [null, [Validators.required]]
		});
		return form;
	}

	removePharmaco(i: number) {
		this.pharmacos.removeAt(i);
		if (!this.pharmacos.controls.length)
			this.showPharmacoTitle = false;
	}

	loadTootip(i: number): string {
		const pharmaco: FormGroup = <FormGroup>this.pharmacos.at(i);
		return pharmaco.controls.snomed.value.pt
	}

	get pharmacos(): FormArray {
		return this.pharmacoForm.get('pharmaco') as FormArray
	}

	getSnomed(i: number): FormGroup {
		return <FormGroup>this.pharmacos.at(i).get('snomed')
	}

	searchPharmacoSnomedConcept() {
		this.showPharmacoTitle = true;
		this.showToSearchSnomedConcept = true;
	}
}
