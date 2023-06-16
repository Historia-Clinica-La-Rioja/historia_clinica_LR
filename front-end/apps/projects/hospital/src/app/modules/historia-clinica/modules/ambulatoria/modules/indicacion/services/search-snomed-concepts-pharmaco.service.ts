import { Injectable } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { TableModel } from '@presentation/components/table/table.component';

@Injectable({
	providedIn: 'root'
})
export class SearchSnomedConceptsPharmacoService {
	pharmacoForm: UntypedFormGroup;
	solventForm: UntypedFormGroup;
	searching = false;
	showToSearchSnomedConcept = false;
	showPharmacoTitle = false;
	showSolventTitle = false;
	snowstormServiceNotAvailable = false;
	conceptsResultsTable: TableModel<any>;
	pharmacoSnomedConcept: SnomedDto;
	solventSnomedConcept: SnomedDto;


	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
	) {
		this.pharmacoForm = this.formBuilder.group({
			pharmaco: [null, [Validators.required]]
		});
		this.solventForm = this.formBuilder.group({
			solvent: [null]
		});
	}

	resetAllForms(): void {
		delete this.pharmacoSnomedConcept;
		delete this.solventSnomedConcept;
		this.showPharmacoTitle = false;
		this.showSolventTitle = false;
		this.showToSearchSnomedConcept = false;
		this.pharmacoForm.reset();
		this.solventForm.reset();
	}

	resetSolventForm(): void {
		delete this.solventSnomedConcept;
		this.showSolventTitle = false;
		this.solventForm.reset();
	}

	setValidationSolventForm(): void {
		this.solventForm.controls.solvent.setValidators([Validators.required]);
		this.solventForm.controls.solvent.updateValueAndValidity();
	}

	removeValidationSolventForm(): void {
		this.solventForm.controls.solvent.setValidators(null);
		this.solventForm.controls.solvent.removeValidators([Validators.required]);
		this.solventForm.controls.solvent.updateValueAndValidity();
	}

	setSolventForm(selectedConcept: SnomedDto): void {
		if (selectedConcept) {
			this.solventSnomedConcept = selectedConcept;
			const pt = selectedConcept ? selectedConcept.pt : '';
			this.solventForm.controls.solvent.setValue(pt);
		}
	}

	setForm(selectedConcept: SnomedDto): void {
		if (selectedConcept) {
			this.pharmacoSnomedConcept = selectedConcept;
			const pt = selectedConcept ? selectedConcept.pt : '';
			this.pharmacoForm.controls.pharmaco.setValue(pt);
		}
	}


	searchPharmacoConcept(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.MEDICINE
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setForm(selectedConcept));
		}
	}

	searchSolventConcept(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.MEDICINE
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setSolventForm(selectedConcept));
		}
	}

	searchPharmacoSnomedConcept() {
		this.showSolventTitle = true;
		this.showPharmacoTitle = true;
		this.showToSearchSnomedConcept = true;
	}
}
