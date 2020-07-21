import { HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';

export class MotivoNuevaConsultaService {

	private motivoConsulta: HealthConditionDto;
	private form: FormGroup;
	private motivoConsultaError: string = '';

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
		let nuevoMotivoConsulta: HealthConditionDto;
		if (selectedConcept) {
			this.form.controls.snomed.setValue(selectedConcept.pt);
			nuevoMotivoConsulta = {
				id: null,
				verificationId: null,
				statusId: null,
				snomed: selectedConcept
			};
		}
		this.onChangeMotivoConsulta(nuevoMotivoConsulta);
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

	onChangeMotivoConsulta(motivoConsulta: HealthConditionDto) {
		this.motivoConsulta = motivoConsulta;
		if (motivoConsulta) {
			delete this.motivoConsultaError;
		}
	}

	getForm(): FormGroup {
		return this.form;
	}

	getMotivoConsulta(): HealthConditionDto {
		return this.motivoConsulta;
	}
}
