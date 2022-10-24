import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProgramReportsService } from '@api-rest/services/program-reports.service';
import { hasError } from '@core/utils/form.utils';
import { ODONTO_REPORT_TYPES } from '../constants/report-types';
import { OdontoReportService } from '@api-rest/services/odonto.reports.service';

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


  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly odontoService: OdontoReportService,
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
          this.odontoService.getMonthlyEndodonciaReport(params, `${this.ODONTO_REPORT_TYPES[3].description}.xls`).subscribe();
        break;
        default:
      }
    }
  }
}

