import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { filter, map, startWith } from 'rxjs/operators';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { Location } from '@angular/common';
import { DiariesService } from '@api-rest/services/diaries.service';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ContextService } from '@core/services/context.service';
import { MatOptionSelectionChange } from '@angular/material/core';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup;
	profesionales: ProfessionalDto[] = [];
	profesionalesFiltered: ProfessionalDto[];
	profesionalSelected: ProfessionalDto;

	especialidades: ProfessionalsByClinicalSpecialtyDto[] = [];
	especialidadesFiltered: ProfessionalsByClinicalSpecialtyDto[];
	especialidadSelected: ProfessionalsByClinicalSpecialtyDto;

	routePrefix: string;

	constructor(
		private readonly router: Router,
		private readonly formBuilder: FormBuilder,
		private readonly healthCareProfessionalService: HealthcareProfessionalService,
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly location: Location,
		public readonly route: ActivatedRoute,
		private readonly diariesService: DiariesService,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly contextService: ContextService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			especialidad: [null],
			profesional: [null, Validators.required],
		});

		this.healthCareProfessionalService.getAll()
			.subscribe(doctors => {
				this.clinicalSpecialtyService.getClinicalSpecialties(doctors.map(d => d.id))
					.subscribe(professionalsBySpecialties => {
						this.especialidades = professionalsBySpecialties;
						this.form.controls.especialidad.valueChanges
							.pipe(
								startWith(''),
								map(value => this._filterEspecialidad(value)))
							.subscribe(especialidadesFiltered => {
								this.especialidadesFiltered = especialidadesFiltered;
								if (this.form.value.especialidad && this.especialidadSelected &&
									this.form.value.especialidad !== this.especialidadSelected.clinicalSpecialty.name) {
									delete this.especialidadSelected;
									this.profesionalesFiltered = this.profesionales;
								}
							});

					});

				this.profesionales = doctors;
				this.form.controls.profesional.valueChanges
					.pipe(
						startWith(''),
						map(value => this._filterProfesional(value)))
					.subscribe(profesionalesFiltered => {
						this.profesionalesFiltered = profesionalesFiltered;
						if (this.form.value.profesional && this.profesionalSelected &&
							this.form.value.profesional !== this.getFullNameLicence(this.profesionalSelected)) {
							delete this.profesionalSelected;
						}
					});

				if (this.route.firstChild) {
					this.route.firstChild.params.subscribe(params => {
						const idProfesional = Number(params.idProfesional);
						if (idProfesional) {
							this.healthCareProfessionalService.getOne(idProfesional)
								.subscribe((profesional: ProfessionalDto) => {
									if (profesional) {
										this.form.controls.profesional.setValue(this.getFullNameLicence(profesional));
										this.profesionalSelected = profesional;
									}
								});
						}
					});
				} else {
					if (this.profesionales.length === 1) {
						this.form.controls.profesional.setValue(this.getFullNameLicence(this.profesionales[0]));
						this.profesionalSelected = this.profesionales[0];
						this.search();
					}
				}

			});

	}

	search(): void {
		if (this.form.valid) {
			const result = this._getResult();
			if (result) {
				this.router.navigate([`profesional/${result.id}`], {relativeTo: this.route});
				this.form.controls.profesional.setValue(this.getFullNameLicence(result));
				this.profesionalSelected = result;
			}
		}
	}

	searchEspecialidad() {
		if (this.form.value.especialidad) {
			const result = this.getEspecialidad();
			if (result) {
				this.form.controls.especialidad.setValue(result.clinicalSpecialty.name);
				this.especialidadSelected = result;
				this.resetProfessional(result);
			}

		}
	}

	private getEspecialidad() {
		const results = this._filterEspecialidad(this.form.value.especialidad);
		return results.length === 1 ? results[0] : null;
	}

	getFullName(profesional: ProfessionalDto): string {
		return `${profesional.lastName}, ${profesional.firstName}`;
	}

	getFullNameLicence(profesional: ProfessionalDto): string {
		return `${this.getFullName(profesional)} - ${profesional.licenceNumber}`;
	}

	selectProfesional(event: MatOptionSelectionChange, profesional: ProfessionalDto): void {
		if (event.isUserInput) {
			this.profesionalSelected = profesional;
		}
	}

	selectEspecialidad(event: MatOptionSelectionChange, especialidad: ProfessionalsByClinicalSpecialtyDto): void {
		if (event.isUserInput) {
			this.especialidadSelected = especialidad;
			this.resetProfessional(especialidad);
		}
	}

	private resetProfessional(especialidad: ProfessionalsByClinicalSpecialtyDto) {
		delete this.profesionalSelected;
		this.form.controls.profesional.reset();
		this.profesionalesFiltered = this.getProfesionalesFilteredBy(especialidad);
	}

	private getProfesionalesFilteredBy(especialidad: ProfessionalsByClinicalSpecialtyDto): ProfessionalDto[] {
		return this.profesionales.filter(p => especialidad.professionalsIds.find(e => e === p.id));
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda/`]);
	}

	private _getResult(): ProfessionalDto {
		if (this.profesionalSelected) {
			return this.profesionalSelected;
		}
		const results = this._filterProfesional(this.form.value.profesional);
		return results.length === 1 ? results[0] : null;
	}

	private _filterProfesional(value: string): ProfessionalDto[] {
		const filterValue = value?.toLowerCase();
		const profesionalesDominio = this.especialidadSelected ? this.getProfesionalesFilteredBy(this.especialidadSelected) :
			this.profesionales;

		return profesionalesDominio.filter((profesional: ProfessionalDto) => {
			const fullName = this.getFullNameLicence(profesional);
			return fullName.toLowerCase().includes(filterValue);
		});
	}

	private _filterEspecialidad(value: string): ProfessionalsByClinicalSpecialtyDto[] {
		const filterValue = value?.toLowerCase();
		return this.especialidades.filter((especialidad: ProfessionalsByClinicalSpecialtyDto) => {
			return especialidad.clinicalSpecialty.name.toLowerCase().includes(filterValue);
		});
	}

}

