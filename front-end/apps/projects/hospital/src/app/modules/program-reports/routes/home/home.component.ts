import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProgramReportsService } from '@api-rest/services/program-reports.service';
import { hasError } from '@core/utils/form.utils';
import { PROGRAM_REPORT_TYPES } from '../../constants/report-types';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';
import { MIN_DATE } from '@core/utils/date.utils';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  form: FormGroup;
  public submitted = false;

  public hasError = hasError;

  PROGRAM_REPORT_TYPES = PROGRAM_REPORT_TYPES;
  minDate = MIN_DATE;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly reportsService: ProgramReportsService,
    
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
      const programReportId = this.form.controls.programReportType.value;
      switch (programReportId) {
        case 1:
          this.reportsService.getMonthlyEpiIReport(params, `${this.PROGRAM_REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.reportsService.getMonthlyEpiIIReport(params, `${this.PROGRAM_REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        case 3:
          this.reportsService.getMonthlyRecupero(params, `${this.PROGRAM_REPORT_TYPES[2].description}.xls`).subscribe();
        break;
        case 4:
          this.reportsService.getMonthlySumar(params, `${this.PROGRAM_REPORT_TYPES[3].description}.xls`).subscribe();
        break;
        case 5:
          this.reportsService.getMonthlyOdontologyReport(params, `${this.PROGRAM_REPORT_TYPES[4].description}.xls`).subscribe();          
        break;
        default:
      }
    }
  }
}
