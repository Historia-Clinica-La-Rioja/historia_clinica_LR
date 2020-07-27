import { SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { Observable, Subject } from 'rxjs';

export interface MotivoConsulta {
	snomed: SnomedDto;
}

export class MotivoNuevaConsultaService {

	private errorSource = new Subject<string>();
	private _error$: Observable<string>;

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

	get error$(): Observable<string> {
		if (!this._error$) {
			this._error$ = this.errorSource.asObservable();
		}
		return this._error$;
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
			this.errorSource.next();
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

	setError(errorMsg: string): void {
		this.errorSource.next(errorMsg);
	}

	getMotivosConsulta(): MotivoConsulta[] {
		return this.motivoConsulta ? [this.motivoConsulta] : null;
	}
}
