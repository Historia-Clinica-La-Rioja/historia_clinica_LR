import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SnomedDto, DiagnosisDto } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent implements OnInit {

	private diagnosisValue: DiagnosisDto[];

	@Output() diagnosisChange = new EventEmitter();

	@Input()
	set diagnosis(newDiagnosis: DiagnosisDto[]) {
		this.diagnosisValue = newDiagnosis;
		this.diagnosisChange.emit(this.diagnosisValue);
	}

	get diagnosis(): DiagnosisDto[] {
		return this.diagnosisValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;

	//Mat table
	columns = [
		{
			def: 'diagnosis',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.DIAGNOSIS',
			text: ap => ap.snomed.pt
		},
		{
			def: 'status',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.STATUS',
			text: ap => ap.presumptive ? 'Presuntivo' : 'Confirmado'
		}
	];
	displayedColumns: string[] = [];

	constructor(
		private formBuilder: FormBuilder,
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			presumptive: [false]
		});
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			let diagnostico: DiagnosisDto = {
				statusId: this.form.value.statusId,
				presumptive: this.form.value.presumptive,
				snomed: this.snomedConcept
			};
			this.add(diagnostico);
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

	add(diagnostico: DiagnosisDto): void {
		this.diagnosis = pushTo<DiagnosisDto>(this.diagnosis, diagnostico);
	}

	remove(index: number): void {
		this.diagnosis = removeFrom<DiagnosisDto>(this.diagnosis, index);
	}

}
