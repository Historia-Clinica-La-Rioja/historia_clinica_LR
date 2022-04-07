import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-medical-coverage-summary-view',
  templateUrl: './medical-coverage-summary-view.component.html',
  styleUrls: ['./medical-coverage-summary-view.component.scss']
})
export class MedicalCoverageSummaryViewComponent {

  @Input() coverageInfo: SummaryCoverageInformation;
  @Input() title = "ambulatoria.medical-coverage-summary-view.TITLE";

  thereIsCoverageInfo(): boolean {
    if (!this.coverageInfo)
      return false;
    return (Object.keys(this.coverageInfo).length >= 1);
  }
}

export interface SummaryCoverageInformation {
  name?: string;
  affiliateNumber?: string;
  plan?: string;
}
