import {Component, Inject, OnInit} from '@angular/core';
import {ClinicalSpecialtyDto, OutpatientImmunizationDto, SnomedDto} from '@api-rest/api-model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Moment} from 'moment';
import {SEMANTICS_CONFIG} from '../../../../constants/snomed-semantics';
import {DateFormat, momentFormat, momentParseDate, newMoment} from '@core/utils/moment.utils';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ActionDisplays, TableModel} from '@presentation/components/table/table.component';
import {SnowstormService} from '@api-rest/services/snowstorm.service';
import {SnackBarService} from '@presentation/services/snack-bar.service';
import {HceImmunizationService} from '@api-rest/services/hce-immunization.service';
import {TranslateService} from '@ngx-translate/core';
import {ConfirmDialogComponent} from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import {AppointmentsService} from '@api-rest/services/appointments.service';
import {ClinicalSpecialtyService} from '@api-rest/services/clinical-specialty.service';
import {VACUNAS} from 'src/app/modules/historia-clinica/constants/summaries';

@Component({
	selector: 'app-aplicar-vacuna',
	templateUrl: './aplicar-vacuna.component.html',
	styleUrls: ['./aplicar-vacuna.component.scss']
})
export class AplicarVacunaComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	loading = false;
	today: Moment = newMoment();
	searching = false;
	conceptsResultsTable: TableModel<any>;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	fixedSpecialty = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	specialties: ClinicalSpecialtyDto[];
	appliedVaccines: OutpatientImmunizationDto[];
	public readonly vacunasSummary = VACUNAS;
	public tableModel: TableModel<OutpatientImmunizationDto>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number },
		public dialogRef: MatDialogRef<AplicarVacunaComponent>,
		private readonly snowstormService: SnowstormService,
		private readonly hceImmunizationService: HceImmunizationService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly translator: TranslateService,
		private readonly appointmentsService: AppointmentsService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService
	) {
		this.appliedVaccines = [];
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [null, Validators.required],
			snomed: [null, Validators.required],
			note: [null, Validators.required],
			clinicalSpecialty: []
		});

		this.setLoggedProfessionalSpecialties();
	}

	setLoggedProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe( specialties => {
			this.specialties = specialties;
			this.fixedSpecialty = false;
			this.defaultSpecialty = specialties[0];
			this.form.get('clinicalSpecialty').setValue(this.defaultSpecialty);
		});
	}

	onSearch(searchValue: string): void {
		if (searchValue) {
			this.searching = true;
			this.snowstormService.getSNOMEDConcepts({term: searchValue, ecl: this.SEMANTICS_CONFIG.vaccine})
				.subscribe(
					results => {
						this.conceptsResultsTable = this.buildConceptsResultsTable(results.items);
						this.searching = false;
					}
				);
		}
	}

	private buildConceptsResultsTable(data: SnomedDto[]): TableModel<SnomedDto> {
		return {
			columns: [
				{
					columnDef: '1',
					header: 'Descripción SNOMED',
					text: concept => concept.pt
				},
				{
					columnDef: 'select',
					action: {
						displayType: ActionDisplays.BUTTON,
						display: 'Seleccionar',
						matColor: 'primary',
						do: concept => this.setConcept(concept)
					}
				},
			],
			data,
			enablePagination: true
		};
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		delete this.conceptsResultsTable;
	}

	applyVaccine() {
		if (this.form.valid && this.snomedConcept) {

			const vacuna: OutpatientImmunizationDto = {
				administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
				note: this.form.value.note,
				snomed: this.snomedConcept,
				clinicalSpecialtyId: this.defaultSpecialty.id
			};

			this.appliedVaccines.push(vacuna);
			this.resetForm();
			this.tableModel = this.buildVaccinesTable(this.appliedVaccines);
		}
	}

	chosenYearHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.year(newDate.year());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.month(newDate.month());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	setDefaultSpecialty() {
		this.defaultSpecialty = this.form.controls.clinicalSpecialty.value;
	}

	private buildVaccinesTable(data: OutpatientImmunizationDto[]): TableModel<OutpatientImmunizationDto> {
		return {
			columns: [
				{
					columnDef: 'vacuna',
					header: 'Vacuna',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'fecha',
					header: 'Fecha de vacunación',
					text: (row) => row.administrationDate ? momentFormat(momentParseDate(row.administrationDate), DateFormat.VIEW_DATE) : undefined
				}
			],
			data
		};
	}

	save() {
		if (this.appliedVaccines.length >= 1 ) {
			const finishAppointment = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Aplicar vacuna',
					content: '¿Confirma la aplicación de las vacunas?',
					okButtonLabel: 'buttons.YES',
					cancelButtonLabel: 'buttons.NO'
				}
			});

			finishAppointment.afterClosed().subscribe(accepted => {
				if (accepted) {
					this.loading = true;
					this.hceImmunizationService.gettingVaccine(this.appliedVaccines, this.data.patientId)
						.subscribe(() => {
							this.loading = false;
							this.dialogRef.close(this.appliedVaccines);
							this.snackBarService.showSuccess('ambulatoria.paciente.vacunas.aplicar.save.SUCCESS');
						}, _ => {
							this.snackBarService.showError('ambulatoria.paciente.vacunas.aplicar.save.ERROR');
							this.loading = false;
						});
					}
			});
		}
	}
}
