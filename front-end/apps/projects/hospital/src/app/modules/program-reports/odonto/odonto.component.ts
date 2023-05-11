import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProgramReportsService } from '@api-rest/services/program-reports.service';
import { hasError } from '@core/utils/form.utils';
import { ODONTO_REPORT_TYPES } from '../constants/report-types';
import { OdontoReportService } from '@api-rest/services/odonto.reports.service';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';
import { MIN_DATE } from '@core/utils/date.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';


@Component({
  selector: 'app-odonto',
  templateUrl: './odonto.component.html',
  styleUrls: ['./odonto.component.scss']
})
export class OdontoComponent implements OnInit {

  form: FormGroup;
  public submitted = false;

  public hasError = hasError;

  ODONTO_REPORT_TYPES = ODONTO_REPORT_TYPES;

  minDate = MIN_DATE;
  professionalsTypeahead: TypeaheadOption<ProfessionalDto>[];
	professionalInitValue: TypeaheadOption<ProfessionalDto>;
	professionals: ProfessionalDto[] = [];

	specialtiesTypeahead: TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[];
	specialtiesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	idProfessional: number;
	idSpecialty: number;



  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly odontoService: OdontoReportService,
    private readonly healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService
    
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      programReportType: [null, Validators.required],
      startDate: [this.firstDayOfThisMonth(), Validators.required],
			endDate: [this.lastDayOfThisMonth(), Validators.required],

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


  generateProgramReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {
        startDate: this.form.controls.startDate.value,
				endDate: this.form.controls.endDate.value,
        specialtyId: this.form.controls.specialtyId.value,
		    professionalId: this.form.controls.professionalId.value
      }
      const programReportId = this.form.controls.programReportType.value;
      switch (programReportId) {
        case 1:
          this.odontoService.getMonthlyPromocionReport(params, `${this.ODONTO_REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.odontoService.getMonthlyPrevencionReport(params, `${this.ODONTO_REPORT_TYPES[1].description}.xls`).subscribe();
        break;
        case 3:
          this.odontoService.getMonthlyPrevencionGrupalReport(params, `${this.ODONTO_REPORT_TYPES[2].description}.xls`).subscribe();
        break;
        case 4:
          this.odontoService.getMonthlyOperatoriaReport(params, `${this.ODONTO_REPORT_TYPES[3].description}.xls`).subscribe();
        break;
        case 5:
          this.odontoService.getMonthlyEndodonciaReport(params, `${this.ODONTO_REPORT_TYPES[4].description}.xls`).subscribe();
        break;
        case 6:
          this.odontoService.getMonthlyRecuperoReport(params, `${this.ODONTO_REPORT_TYPES[5].description}.xls`).subscribe();
        break;
        default:
      }
    }
  }
}

