import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { HealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { NewAgendaService } from '../../services/new-agenda.service';
import { MatDialog } from '@angular/material/dialog';
import { Location } from '@angular/common';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup;
	profesionales: HealthcareProfessionalDto[] = [];
	profesionalesFiltered$: Observable<HealthcareProfessionalDto[]>;
	profesionalSelected: HealthcareProfessionalDto;
	newAgendaService: NewAgendaService;

	constructor(
		private readonly router: Router,
		private readonly formBuilder: FormBuilder,
		private readonly healthCareProfessionalService: HealthcareProfessionalService,
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly location: Location,
		private readonly route: ActivatedRoute,
	) {
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			profesional: [null, Validators.required]
		});

		this.route.paramMap
			.subscribe((params: ParamMap) => {
				const idProfesional = Number(params.get('idProfesional'));
				if (idProfesional) {
					this.healthCareProfessionalService.getOne(idProfesional)
						.pipe(map(toHealthcareProfessionalDto))
						.subscribe(profesional => {
							if (profesional) {
								this.profesionalSelected = profesional;
								this.form.controls.profesional.setValue(this.getFullNameLicence(profesional));
							}
						});
				}
			});

		this.newAgendaService.setAppointmentDuration(60); // todo reemplazar por obtenido del BE

		this.healthCareProfessionalService.getAllDoctors().subscribe(doctors => {
			this.profesionales = doctors;

			this.profesionalesFiltered$ = this.form.controls.profesional.valueChanges.pipe(
				startWith(''),
				map(value => this._filter(value))
			);
		});

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
	}


	search(): void {
		delete this.profesionalSelected;
		if (this.form.valid) {
			const result = this._getResult();
			if (result) {
				this.form.controls.profesional.setValue(this.getFullNameLicence(result));
				console.log('result', result);
				this.location.replaceState(`${this.router.url}/profesional/${result.id}`);
				// todo load calendar
			} else {
				// todo show error no encontrado
			}
		}
	}

	getFullName(profesional: HealthcareProfessionalDto): string {
		return `${profesional.person.lastName}, ${profesional.person.firstName}`;
	}

	getFullNameLicence(profesional: HealthcareProfessionalDto): string {
		return `${this.getFullName(profesional)} - ${profesional.licenceNumber}`;
	}

	select(value): void {
		this.profesionalSelected = value;
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

	goToNewAgenda(): void {
		this.router.navigate([`${this.router.url}/nueva-agenda/`]);
	}

}
