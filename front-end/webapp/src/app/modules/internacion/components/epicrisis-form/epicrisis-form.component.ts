import { Component, OnInit } from '@angular/core';
import {
	AllergyConditionDto,
	DiagnosisDto, HealthHistoryConditionDto, InmunizationDto, MedicationDto, NewEpicrisisDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto
} from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { DatePipe } from '@angular/common';

@Component({
	selector: 'app-epicrisis-form',
	templateUrl: './epicrisis-form.component.html',
	styleUrls: ['./epicrisis-form.component.scss']
})
export class EpicrisisFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;

	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;

	diagnosis: TableData<DiagnosisDto> = {
		data: [],
		columns: [
			{
				def: 'diagnosis',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.DIAGNOSIS',
				text: ap => ap.snomed.pt
			},
			{
				def: 'status',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.STATUS',
				text: ap => ap.presumptive ? 'Presuntivo' : 'Confirmado'
			}
		],
		displayedColumns: []
	};
	personalHistories: TableData<HealthHistoryConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
				text: af => af.snomed.pt
			}
		],
		displayedColumns: []
	};
	familyHistories: TableData<HealthHistoryConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
				text: af => af.snomed.pt
			}
		],
		displayedColumns: []
	};
	allergies: TableData<AllergyConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.alergias.table.columns.ALLERGY',
				text: a => a.snomed.pt
			}
		],
		displayedColumns: []
	};
	inmunizations: TableData<InmunizationDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.vacunas.table.columns.INMUNIZATION',
				text: v => v.snomed.pt
			},
			{
				def: 'date',
				header: 'internaciones.epicrisis.vacunas.table.columns.REGISTRY_DATE',
				text: v => this.datePipe.transform(v.administrationDate, 'dd/MM/yyyy')
			},
		],
		displayedColumns: []
	};
	medications: TableData<MedicationDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.medicacion.table.columns.MEDICATION',
				text: v => v.snomed.pt
			},
			{
				def: 'note',
				header: 'internaciones.epicrisis.medicacion.table.columns.NOTE',
				text: v => v.note
			},
		],
		displayedColumns: []
	};

	constructor(
		private formBuilder: FormBuilder,
		private epicrisisService: EpicrisisService,
		private route: ActivatedRoute,
		private router: Router,
		private datePipe: DatePipe,
	) {
		this.diagnosis.displayedColumns = this.diagnosis.columns?.map(c => c.def);
		this.familyHistories.displayedColumns = this.familyHistories.columns?.map(c => c.def);
		this.personalHistories.displayedColumns = this.personalHistories.columns?.map(c => c.def);
		this.allergies.displayedColumns = this.allergies.columns?.map(c => c.def);
		this.inmunizations.displayedColumns = this.inmunizations.columns?.map(c => c.def);
		this.medications.displayedColumns = this.medications.columns?.map(c => c.def);

	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {
				this.internmentEpisodeId = Number(params.get('idInternacion'));
				this.patientId = Number(params.get('idPaciente'));
			}
		);


		this.form = this.formBuilder.group({
			observations: this.formBuilder.group ({
				evolutionNote: [null, Validators.required],
				indicationsNote: [null, Validators.required],
				otherNote: [null],
				physicalExamNote: [null, Validators.required],
				studiesSummaryNote: [null, Validators.required]
			}),
		});


		const epicrisis$ = this.epicrisisService.getInternmentGeneralState(this.internmentEpisodeId);
		epicrisis$.subscribe(response => {
			this.diagnosis.data = response.diagnosis;
			this.personalHistories.data = response.personalHistories;
			this.familyHistories.data = response.familyHistories;
			this.allergies.data = response.allergies;
			this.inmunizations.data = response.inmunizations;
			this.medications.data = response.medications;
		});
	}

	save(): void {
		if (this.form.valid) {
			const newEpicrisis: NewEpicrisisDto = {
				confirmed: true,
				notes: this.form.value.observations,
			};
			this.epicrisisService.createDocument(newEpicrisis, this.internmentEpisodeId)
				.subscribe((epicrisisResponse: ResponseEpicrisisDto) => {
					const url = `internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
					this.router.navigate([url]);
				});
		}
	}

	back(): void {
		window.history.back();
	}

}

interface TableData<T> {
	data: T[];
	columns: any[];
	displayedColumns: string[];
}
