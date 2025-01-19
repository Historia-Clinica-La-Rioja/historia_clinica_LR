import { ChangeDetectionStrategy, Component, Inject, Input, OnInit } from '@angular/core';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { DiagnosticWithTypeReportInfoDto } from '../../model/ImageModel';
import { WINDOW } from 'projects/hospital/src/app/modules/image-network/constants/token';
import { REPORT_STATES, REPORT_STATES_ID, ReportState } from 'projects/hospital/src/app/modules/image-network/constants/report';

@Component({
  selector: 'app-study-images-reports',
  templateUrl: './study-images-reports.component.html',
  styleUrls: ['./study-images-reports.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudyImagesReportsComponent implements OnInit {

  STUDY_STATUS = STUDY_STATUS;
  reportStates = REPORT_STATES_ID;

  @Input() studyInfo: DiagnosticWithTypeReportInfoDto
  @Input() appointmentId: number
  showAccessToFiles = false
  isImageOrderCasesIncomplete = false
  reportStatusDescription: ReportState = null

  constructor(
    @Inject(WINDOW) private window: Window,
  ) { }

  ngOnInit(): void {
    this.isImageOrderCasesIncomplete = this.studyInfo.typeOrder !== 'completa' && !this.studyInfo.infoOrderInstances.status
    this.showAccessToFiles = this.studyInfo.typeOrder === 'completa' && this.studyInfo.statusId === STUDY_STATUS.FINAL.id
    || this.studyInfo.typeOrder !== 'completa' && this.studyInfo.infoOrderInstances.status
    this.reportStatusDescription =  REPORT_STATES.find(state => state.id == this.studyInfo.infoOrderInstances.reportStatus)
  }

  viewLocalStudy(studyLocalUrl: string){
    this.window.open(studyLocalUrl, "_blank")
  }

}