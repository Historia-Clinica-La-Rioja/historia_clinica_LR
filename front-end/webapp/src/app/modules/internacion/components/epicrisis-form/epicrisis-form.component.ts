import { Component, OnInit } from '@angular/core';
import {
	AllergyConditionDto,
	DiagnosisDto, HealthHistoryConditionDto, InmunizationDto, EpicrisisDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto
} from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SelectionModel } from '@angular/cdk/collections';

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
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<any>(true, [])
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
		displayedColumns: [],
		selection: new SelectionModel<any>(true, [])
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
		displayedColumns: [],
		selection: new SelectionModel<any>(true, [])
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
		displayedColumns: [],
		selection: new SelectionModel<any>(true, [])
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
		displayedColumns: [],
		selection: new SelectionModel<any>(true, [])
	};

	constructor(
		private formBuilder: FormBuilder,
		private epicrisisService: EpicrisisService,
		private route: ActivatedRoute,
		private router: Router,
		private datePipe: DatePipe,
		private snackBarService: SnackBarService
	) {
		this.diagnosis.displayedColumns = this.diagnosis.columns?.map(c => c.def).concat(['select']);
		this.familyHistories.displayedColumns = this.familyHistories.columns?.map(c => c.def).concat(['select']);
		this.personalHistories.displayedColumns = this.personalHistories.columns?.map(c => c.def).concat(['select']);
		this.allergies.displayedColumns = this.allergies.columns?.map(c => c.def).concat(['select']);
		this.inmunizations.displayedColumns = this.inmunizations.columns?.map(c => c.def).concat(['select']);

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
		});
	}

	save(): void {
		console.log('selected ', this.diagnosis.selection._selected);
		console.log('selected ', this.personalHistories.selection._selected);
		console.log('selected ', this.familyHistories.selection._selected);
		console.log('selected ', this.allergies.selection._selected);
		console.log('selected ', this.inmunizations.selection._selected);

		if (this.form.valid) {
			const epicrisis: EpicrisisDto = {
				confirmed: true,
				notes: this.form.value.observations,
				diagnosis: [],
				familyHistories: [],
				personalHistories: [],
				medications: [],
				inmunizations: [],
				allergies: []
			};
			this.epicrisisService.createDocument(epicrisis, this.internmentEpisodeId)
				.subscribe((epicrisisResponse: ResponseEpicrisisDto) => {
					this.epicrisisService.getPDF(epicrisisResponse.id, this.internmentEpisodeId).subscribe(
						_ => this.goToInternmentSummary(), _ => this.goToInternmentSummary()
					);
					this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
				}, _ => this.snackBarService.showError('internaciones.epicrisis.messages.ERROR'));
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
		}
	}

	back(): void {
		window.history.back();
	}

	private goToInternmentSummary(): void {
		const url = `internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
		this.router.navigate([url]);
	}


	isAllSelected(dataSource, selection: SelectionModel<any>) {
		const numSelected = selection.selected.length;
		const numRows = dataSource.length;
		return numSelected === numRows;
	}

	/** Selects all rows if they are not all selected; otherwise clear selection. */
	masterToggle(dataSource, selection: SelectionModel<any>) {
		this.isAllSelected(dataSource, selection) ?
			selection.clear() :
			dataSource.forEach(row => selection.select(row));
	}

}

interface TableData<T> {
	data: T[];
	columns: any[];
	displayedColumns: string[];
	selection;
}
