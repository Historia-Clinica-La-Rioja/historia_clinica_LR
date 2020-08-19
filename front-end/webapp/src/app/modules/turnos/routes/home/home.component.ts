import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { HealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { Location } from '@angular/common';
import { DiariesService } from '@api-rest/services/diaries.service';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ContextService } from '@core/services/context.service';
import { MatOptionSelectionChange } from '@angular/material/core';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	viewDate: Date = new Date();

	form: FormGroup;
	profesionales: HealthcareProfessionalDto[] = [];
	profesionalesFiltered: HealthcareProfessionalDto[];
	profesionalSelected: HealthcareProfessionalDto;
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
		private readonly contextService: ContextService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			profesional: [null, Validators.required],
		});

		this.healthCareProfessionalService.getAllDoctors()
			.subscribe(doctors => {

				this.profesionales = doctors;
				this.form.controls.profesional.valueChanges
					.pipe(
						startWith(''),
						map(value => this._filter(value)))
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
								.pipe(map(toHealthcareProfessionalDto))
								.subscribe((profesional: HealthcareProfessionalDto) => {
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

	getFullName(profesional: HealthcareProfessionalDto): string {
		return `${profesional.person.lastName}, ${profesional.person.firstName}`;
	}

	getFullNameLicence(profesional: HealthcareProfessionalDto): string {
		return `${this.getFullName(profesional)} - ${profesional.licenceNumber}`;
	}

	selectProfesional(event: MatOptionSelectionChange, profesional: HealthcareProfessionalDto): void {
		if (event.isUserInput) {
			this.profesionalSelected = profesional;
		}
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda/`]);
	}

	private _getResult(): HealthcareProfessionalDto {
		if (this.profesionalSelected) {
			return this.profesionalSelected;
		}
		const results = this._filter(this.form.value.profesional);
		return results.length === 1 ? results[0] : null;
	}

	private _filter(value: string): HealthcareProfessionalDto[] {
		const filterValue = value?.toLowerCase();
		return this.profesionales.filter((profesional: HealthcareProfessionalDto) => {
			const fullName = this.getFullNameLicence(profesional);
			return fullName.toLowerCase().includes(filterValue);
		});
	}

}

function toHealthcareProfessionalDto(profesional: ProfessionalDto): HealthcareProfessionalDto {
	return profesional ? {
		id: profesional.id,
		licenceNumber: profesional.licenceNumber,
		person: {
			birthDate: null,
			firstName: profesional.firstName,
			lastName: profesional.lastName
		}
	} : null;
}

