import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { PROGRAM_REPORT_TYPES } from './constants/program-report-types';
import { MIN_DATE } from '@core/utils/date.utils';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ProgramReportsService } from '@api-rest/services/program-reports.service';
import { isSameOrAfter, newDate } from '@core/utils/moment.utils';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { isBefore } from 'date-fns';
import { fixDate } from '@core/utils/date/format';

@Component({
  selector: 'app-reportes-programas',
  templateUrl: './reportes-programas.component.html',
  styleUrls: ['./reportes-programas.component.scss']
})
export class ReportesProgramasComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;

  public hasError = hasError;

  professionalsTypeahead: TypeaheadOption<ProfessionalDto>[];
	professionalInitValue: TypeaheadOption<ProfessionalDto>;
	professionals: ProfessionalDto[] = [];

  specialtiesTypeahead: TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[];
	specialtiesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

  idProfessional: number;
	idSpecialty: number;

  REPORT_TYPES = PROGRAM_REPORT_TYPES;

  minDate = MIN_DATE;

  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
    private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
    private readonly programReportsService: ProgramReportsService,
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

  private firstDayOfThisMonth(): Date {
    const today = newDate();
    return new Date(today.getUTCFullYear(), today.getUTCMonth(), 1);
  }

  private lastDayOfThisMonth(): Date {
    const today = newDate();
    return new Date(today.getUTCFullYear(), today.getUTCMonth() + 1, 0);
  }

  maxStartDate(endDate) {
    const today = newDate();
    return !endDate ? today : isBefore(today, endDate) ? today : endDate;
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
		return professional.licenceNumber ? `${this.getFullName(professional)} - ${professional.licenceNumber}` : `${this.getFullName(professional)}`;
	}

	getFullName(professional: ProfessionalDto): string {
		return `${professional.lastName}, ${professional.firstName}`;
	}

  checkValidDates() {
    const fixStartDate = fixDate(this.form.value.startDate);
    const fixEndDate = fixDate(this.form.value.endDate);

    this.form.controls.startDate.setValue(fixStartDate);
    this.form.controls.endDate.setValue(fixEndDate);
		// if both are present, check that the end date is not after the start date
		if (this.form.value.startDate && this.form.value.endDate) {
      if (isBefore(fixEndDate, fixStartDate)) {
        this.form.controls.endDate.setErrors({ min: true });
        this.form.controls.startDate.setErrors({ max: true });
      } else {
        this.form.controls.endDate.setErrors(null);
        this.checkStartDateIsSameOrBeforeToday();
      }
		} else if (fixStartDate) {
			this.checkStartDateIsSameOrBeforeToday();
		} else if (fixEndDate) {
			this.form.controls.endDate.setErrors(null);
		}
	}

  private checkStartDateIsSameOrBeforeToday() {
		const today = newDate();
    const startDate = this.form.value.startDate;
		(isSameOrAfter(today, startDate))
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
      const programReportId = this.form.controls.reportType.value;
      switch (programReportId) {
        case 1:
          this.programReportsService.getEpidemiologyOneReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.programReportsService.getEpidemiologyTwoReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        case 3:
          this.programReportsService.getRecuperoGeneralReport(params, `${this.REPORT_TYPES[2].description}.xls`).subscribe();
          break;
        case 4:
          this.programReportsService.getRecuperoOdontologicoReport(params, `${this.REPORT_TYPES[3].description}.xls`).subscribe();
          break;
        case 5:
          this.programReportsService.getSumarGeneralExcelReport(params, `${this.REPORT_TYPES[4].description}.xls`).subscribe();
          break;
        case 6:
          this.programReportsService.getSumarOdontologicoReport(params, `${this.REPORT_TYPES[5].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
