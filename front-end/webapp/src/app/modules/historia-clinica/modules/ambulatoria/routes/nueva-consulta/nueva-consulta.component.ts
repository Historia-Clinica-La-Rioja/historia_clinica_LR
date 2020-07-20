import { Component, OnInit, EventEmitter } from '@angular/core';
import { HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { SEMANTICS_CONFIG } from 'src/app/modules/historia-clinica/constants/snomed-semantics';

@Component({
	selector: 'app-nueva-consulta',
	templateUrl: './nueva-consulta.component.html',
	styleUrls: ['./nueva-consulta.component.scss']
})
export class NuevaConsultaComponent implements OnInit {

	motivoConsulta: HealthConditionDto;
	form: FormGroup;
	private motivoConsultaError: string = '';

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(private readonly formBuilder: FormBuilder,
		private snomedService: SnomedService
	) { }

	ngOnInit(): void {
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

}
