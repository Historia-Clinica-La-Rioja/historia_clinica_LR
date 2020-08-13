import { SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { Observable, Subject } from 'rxjs';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { pushTo } from '@core/utils/array.utils';

export interface MotivoConsulta {
	snomed: SnomedDto;
}

export class MotivoNuevaConsultaService {

	private errorSource = new Subject<string>();
	private _error$: Observable<string>;

	private motivoConsulta: MotivoConsulta[] = [];
	private form: FormGroup;
	private readonly columns: ColumnConfig[];
	private snomedConcept: SnomedDto;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService
	) {
		this.form = this.formBuilder.group({
			snomed: [null]
		});

		this.columns = [
			{
				def: 'motivo',
				header: 'ambulatoria.paciente.nueva-consulta.motivo.table.columns.MOTIVO',
				text: a => a.snomed.pt
			}
		];
	}

	get error$(): Observable<string> {
		if (!this._error$) {
			this._error$ = this.errorSource.asObservable();
		}
		return this._error$;
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
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

	setError(errorMsg: string): void {
		this.errorSource.next(errorMsg);
	}

	getMotivosConsulta(): MotivoConsulta[] {
		return this.motivoConsulta;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	add(motivo: MotivoConsulta): void {
		this.motivoConsulta = pushTo<MotivoConsulta>(this.motivoConsulta, motivo);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const motivo: MotivoConsulta = {
				snomed: this.snomedConcept
			};
			this.add(motivo);
			this.errorSource.next();
			this.resetForm();
		}
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}
}
