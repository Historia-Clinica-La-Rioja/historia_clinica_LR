import { Component, Input, OnInit } from '@angular/core';
import { HCEErrorProblemDto } from '@api-rest/api-model';

@Component({
  selector: 'app-amended-problems-information',
  templateUrl: './amended-problems-information.component.html',
  styleUrls: ['./amended-problems-information.component.scss']
})
export class AmendedProblemsInformationComponent implements OnInit {

  @Input() errorProblem: HCEErrorProblemDto;
  
  constructor() { }

  ngOnInit(): void {
  }

}
