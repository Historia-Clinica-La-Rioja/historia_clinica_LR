import { Component, Input, OnInit } from '@angular/core';
import { momentFormat } from '@core/utils/moment.utils';
import { DateFormat } from '@core/utils/moment.utils';
import { AmbulatoryConsultationProblemsService, SEVERITY_CODES } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { ProblemasService } from '@historia-clinica/services/problemas.service';

@Component({
  selector: 'app-problem-list',
  templateUrl: './problem-list.component.html',
  styleUrls: ['./problem-list.component.scss']
})

export class ProblemListComponent implements OnInit {

  @Input() problemsService: AmbulatoryConsultationProblemsService;
  @Input() diagnosesService: ProblemasService;
  DateFormat = DateFormat;
  SEVERITY_CODES = SEVERITY_CODES;
  activeService = null;

  momentFormat = momentFormat;

  ngOnInit(): void {
    if (this.problemsService) {
      this.activeService = this.problemsService;
      this.activeService.canEdit = true;
    }
    else {
      this.activeService = this.diagnosesService;
    }
  }

}
