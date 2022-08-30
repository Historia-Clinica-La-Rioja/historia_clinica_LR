import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProgramReportsService } from '@api-rest/services/program-reports.service';
import { hasError } from '@core/utils/form.utils';
import { PROGRAM_REPORT_TYPES } from '../../constants/report-types';

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

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly reportsService: ProgramReportsService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      programReportType: [null, Validators.required],
    });
  }

  generateProgramReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {

      }
      const programReportId = this.form.controls.programReportType.value;
      switch (programReportId) {
        case 1:
          this.reportsService.getMonthlyEpiIReport(params, `${this.PROGRAM_REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.reportsService.getMonthlyEpiIIReport(params, `${this.PROGRAM_REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }
}
