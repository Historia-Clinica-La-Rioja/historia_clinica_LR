import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Moment } from 'moment';

import { dateToMoment, newMoment } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { MIN_DATE } from '@core/utils/date.utils';

import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ReportsService } from '@api-rest/services/reports.service';

import { REPORT_TYPES } from '../../constants/report-types';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup;
	public submitted = false;

	public hasError = hasError;

	professionalsTypeahead: TypeaheadOption<ProfessionalDto>[];
	professionalInitValue: TypeaheadOption<ProfessionalDto>;
	professionals: ProfessionalDto[] = [];

	specialtiesTypeahead: TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[];
	specialtiesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	idProfessional: number;
	idSpecialty: number;

	REPORT_TYPES = REPORT_TYPES;

	minDate = MIN_DATE;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly reportsService: ReportsService,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			reportType: [null, Validators.required],
			startDate: [this.firstDayOfThisMonth(), Validators.required],
			endDate: [this.lastDayOfThisMonth(), Validators.required],
			specialtyId: [null],
			professionalId: [null],
		});
		this.healthcareProfessionalService.getAll().subscribe(professionals => {
			this.professionals = professionals;
			this.specialtiesTypeaheadOptions$ = this.getSpecialtiesTypeaheadOptions$(professionals);
			this.professionalsTypeahead = professionals.map(d => this.toProfessionalTypeahead(d));
		});
	}

	private firstDayOfThisMonth(): Moment {
		const today = newMoment();
		return dateToMoment(new Date(today.year(), today.month(), 1));
	}

	private lastDayOfThisMonth(): Moment {
		const today = newMoment();
		return dateToMoment(new Date(today.year(), today.month() + 1, 0));
	}

	maxStartDate(endDate) {
		const today = newMoment();
		if (endDate) {
			return (today.isBefore(endDate)) ? today : endDate;
		}
		return today;
	}

	private getSpecialtiesTypeaheadOptions$(doctors: ProfessionalDto[]) {
		return this.clinicalSpecialtyService.getClinicalSpecialties(doctors.map(d => d.id))
			.pipe(map(toTypeaheadOptionList));

		function toTypeaheadOptionList(prosBySpecialtyList: ProfessionalsByClinicalSpecialtyDto[]):
			TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[] {
			return prosBySpecialtyList.map(toTypeaheadOption);

			function toTypeaheadOption(s: ProfessionalsByClinicalSpecialtyDto): TypeaheadOption<ProfessionalsByClinicalSpecialtyDto> {
				return {
					compareValue: s.clinicalSpecialty.name,
					value: s
				};
			}
		}
	}

	setSpecialty(professionalsByClinicalSpecialtyDto: ProfessionalsByClinicalSpecialtyDto) {
		this.professionalInitValue = null;
		this.idSpecialty = professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id;
		this.form.controls.specialtyId.setValue(professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id);

		const professionalsFilteredBy = this.getProfessionalsFilteredBy(professionalsByClinicalSpecialtyDto);
		this.professionalsTypeahead = professionalsFilteredBy.map(d => this.toProfessionalTypeahead(d));
	}

	setProfessional(professional: ProfessionalDto) {
		this.idProfessional = professional?.id;
		this.form.controls.professionalId.setValue(professional?.id);
	}

	private getProfessionalsFilteredBy(specialty: ProfessionalsByClinicalSpecialtyDto): ProfessionalDto[] {
		if (specialty?.professionalsIds) {
			return this.professionals.filter(p => specialty.professionalsIds.find(e => e === p.id));
		}
		return this.professionals;
	}

	private toProfessionalTypeahead(professionalDto: ProfessionalDto): TypeaheadOption<ProfessionalDto> {
		return {
			compareValue: this.getFullNameLicence(professionalDto),
			value: professionalDto
		};
	}

	getFullNameLicence(professional: ProfessionalDto): string {
		return `${this.getFullName(professional)} - ${professional.licenceNumber}`;
	}

	getFullName(professional: ProfessionalDto): string {
		return `${professional.lastName}, ${professional.firstName}`;
	}

	checkValidDates() {
		// if both are present, check that the end date is not after the start date
		if (this.form.value.startDate && this.form.value.endDate) {
			const endDate: Moment = this.form.value.endDate;
			if (endDate.isBefore(this.form.value.startDate)) {
				this.form.controls.endDate.setErrors({ min: true });
				this.form.controls.startDate.setErrors({ max: true });
			} else {
				this.form.controls.endDate.setErrors(null);
				this.checkStartDateIsSameOrBeforeToday();
			}
		} else if (this.form.value.startDate) {
			this.checkStartDateIsSameOrBeforeToday();
		} else if (this.form.value.endDate) {
			this.form.controls.endDate.setErrors(null);
		}
	}

	private checkStartDateIsSameOrBeforeToday() {
		const today = newMoment();
		(today.isSameOrAfter(this.form.value.startDate))
			? this.form.controls.startDate.setErrors(null)
			: this.form.controls.startDate.setErrors({ afterToday: true });
	}

	generateReport() {
		this.submitted = true;
		if (this.form.valid) {
			const params = {
				startDate: this.form.controls.startDate.value,
				endDate: this.form.controls.endDate.value,
				specialtyId: this.form.controls.specialtyId.value,
				professionalId: this.form.controls.professionalId.value
			}
			const reportId = this.form.controls.reportType.value;
			switch (reportId) {
				case 1:
					this.reportsService.getMonthlyReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
					break;
				case 2:
					this.reportsService.getOutpatientSummaryReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
					break;
				default:
			}
		}
	}

}
