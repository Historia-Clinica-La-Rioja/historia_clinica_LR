import { Component, Input } from '@angular/core';
import { EPatientMedicalCoverageCondition } from "@api-rest/api-model";
import { EMedicalCoverageType } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";

@Component({
  selector: 'app-medical-coverage-summary-view',
  templateUrl: './medical-coverage-summary-view.component.html',
  styleUrls: ['./medical-coverage-summary-view.component.scss']
})
export class MedicalCoverageSummaryViewComponent {

  @Input() coverageInfo: SummaryCoverageInformation;
  @Input() title = "ambulatoria.medical-coverage-summary-view.TITLE";
  typeART = 3;

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
  condition?: EPatientMedicalCoverageCondition;
  type?: EMedicalCoverageType;
  cuit?: string;
}
