import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SnomedDto, DiagnosisDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
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

	private snomedConcept: SnomedDto;

	form: FormGroup;

	//Mat table
	columns = [
		{
			def: 'diagnosis',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.DIAGNOSIS',
			text: ap => ap.snomed.fsn
		}
	];
	displayedColumns: string[] = [];
	dataSource: MatTableDataSource<DiagnosisDto>;

	constructor(
		private formBuilder: FormBuilder,
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.dataSource = new MatTableDataSource<DiagnosisDto>(this.diagnosis);
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			presumptive: [false]
		});
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			let diagnostico: DiagnosisDto = {
				id: null,
				verificationId: null,
				statusId: this.form.value.statusId,
				presumptive: this.form.value.presumptive,
				snomed: this.snomedConcept
			};
			this.add(diagnostico);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(diagnostico: DiagnosisDto): void {
		this.dataSource.data = pushTo<DiagnosisDto>(this.dataSource.data, diagnostico);
		this.diagnosis.push(diagnostico);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<DiagnosisDto>(this.dataSource.data, index);
			this.diagnosis = this.diagnosis.filter(item => toRemove !== item);
		}
	}

}
