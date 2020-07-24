import { SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';

export interface MotivoConsulta {
	snomed: SnomedDto;
}

export class MotivoNuevaConsultaService {

	private motivoConsulta: MotivoConsulta;
	private form: FormGroup;

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService
	) {
		this.form = this.formBuilder.group({
			snomed: [null]
		});
	}

	resetForm(): void {
		delete this.motivoConsulta;
		this.form.reset();
	}

	setConcept(selectedConcept: SnomedDto): void {
		if (selectedConcept) {
			this.form.controls.snomed.setValue(selectedConcept.pt);
			this.motivoConsulta = {
				snomed: selectedConcept
			};
		}
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.consultationReason
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getForm(): FormGroup {
		return this.form;
	}

	getMotivoConsulta(): MotivoConsulta {
		return this.motivoConsulta;
	}
}
