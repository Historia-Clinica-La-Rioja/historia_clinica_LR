import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-include-previous-data-question',
  templateUrl: './include-previous-data-question.component.html',
  styleUrls: ['./include-previous-data-question.component.scss']
})
export class IncludePreviousDataQuestionComponent {

  @Input() dataName: string;
  @Input() date: string;
  @Output() response = new EventEmitter<boolean>();

}
