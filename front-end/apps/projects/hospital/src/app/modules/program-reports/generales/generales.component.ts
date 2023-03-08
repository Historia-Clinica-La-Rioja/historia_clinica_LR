import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { GeneralReportService } from '@api-rest/services/general.reports.service';
import { GENERAL_REPORT_TYPES } from '../constants/report-types';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';

import { MIN_DATE } from '@core/utils/date.utils';


@Component({
  selector: 'app-generales',
  templateUrl: './generales.component.html',
  styleUrls: ['./generales.component.scss']
})
export class GeneralesComponent implements OnInit {

  form: FormGroup;
  public submitted = false;

  public hasError = hasError;

  GENERAL_REPORT_TYPES = GENERAL_REPORT_TYPES;

  minDate = MIN_DATE;


  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly generalService: GeneralReportService,
    
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      programReportType: [null, Validators.required],
      startDate: [this.firstDayOfThisMonth(), Validators.required],
			endDate: [this.lastDayOfThisMonth(), Validators.required],
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

      }
      const params2={

      }
      const programReportId = this.form.controls.programReportType.value;
      switch (programReportId) {
        case 1:
          this.generalService.getDaiylyEmergencyReport(params, `${this.GENERAL_REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.generalService.getDiabeticsReport(params, `${this.GENERAL_REPORT_TYPES[1].description}.xls`).subscribe();
        break;
        case 3:
          this.generalService.getHypertensiveReport(params, `${this.GENERAL_REPORT_TYPES[2].description}.xls`).subscribe();
        break;
        case 4:
          this.generalService.getPatientEmergenciesReport(params, `${this.GENERAL_REPORT_TYPES[3].description}.xls`).subscribe();
        break;
        case 5:
          this.generalService.getOutpatientNursingReport(params, `${this.GENERAL_REPORT_TYPES[4].description}.xls`).subscribe();
        break;
        case 6:
          this.generalService.getNursingInternmentReport(params, `${this.GENERAL_REPORT_TYPES[5].description}.xls`).subscribe();
        break;
        case 7:
          this.generalService.getComplementaryStudiesReport(params, `${this.GENERAL_REPORT_TYPES[6].description}.xls`).subscribe();
        break;
        case 8:
          this.generalService.getTotalNursingRecoveryReport(params, `${this.GENERAL_REPORT_TYPES[7].description}.xls`).subscribe();
        break;
        case 9:
          this.generalService.getOutPatientOlderAdults(params, `${this.GENERAL_REPORT_TYPES[8].description}.xls`).subscribe();
        break;
        case 10:
          this.generalService.getHospitalizationOlderAdults(params, `${this.GENERAL_REPORT_TYPES[9].description}.xls`).subscribe();
        break;
        default:
      }
    }
  }
}

