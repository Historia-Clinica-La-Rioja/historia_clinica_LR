import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { HealthcareProfessionalDto } from '@api-rest/api-model';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup;
	profesionales: HealthcareProfessionalDto[] = [];
	profesionalesFiltered: HealthcareProfessionalDto[];
	profesionalSelected: HealthcareProfessionalDto;

	constructor(
		private readonly router: Router,
		private readonly formBuilder: FormBuilder,
		private readonly healthCareProfessionalService: HealthcareProfessionalService) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			profesional: [null, Validators.required]
		});

		this.healthCareProfessionalService.getAllDoctors()
			.subscribe(doctors => this.profesionales = doctors);

		this.form.controls.profesional.valueChanges.pipe(
			startWith(''),
			map(value => this._filter(value))
		).subscribe(filtered => {
			this.profesionalesFiltered = filtered;
		});
	}

	search(): void {
		delete this.profesionalSelected;
		if (this.form.valid) {
			const result = this._getResult();
			if (result) {
				this.form.controls.profesional.setValue(this.getFullNameLicence(result));
				// todo load calendar and add idProfessional to url
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

	private _getResult(): HealthcareProfessionalDto {
		if (this.profesionalSelected) {
			return this.profesionalSelected;
		}
		const results = this._filter(this.form.value.profesional);
		return results.length === 1 ? results[0] : null;
	}

	select(value): void {
		this.profesionalSelected = value;
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
