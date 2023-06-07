import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { SnomedDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-search-snomed-concept',
	templateUrl: './search-snomed-concept.component.html',
	styleUrls: ['./search-snomed-concept.component.scss']
})

export class SearchSnomedConceptComponent {
	searchConceptsLocallyFF = false;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: SearchSnomedConcept,
		private readonly featureFlagService: FeatureFlagService,
		private dialogRef: MatDialogRef<SearchSnomedConceptComponent>,
		private snomedService: SnomedService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFF = isOn);
	}

	setConcept(searchValue: any) {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.data.eclFilter
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.dialogRef.close(selectedConcept));
		}
	}

	setConceptTypeahead(searchValue: any) {
		this.dialogRef.close(searchValue);
	}

	close() {
		this.dialogRef.close()
	}
}

export interface SearchSnomedConcept {
	title?: string;
	eclFilter: string;
	searchValue?: string;
	label: string;
}
