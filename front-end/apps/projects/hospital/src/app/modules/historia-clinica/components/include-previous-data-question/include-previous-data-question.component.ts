import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-include-previous-data-question',
  templateUrl: './include-previous-data-question.component.html',
  styleUrls: ['./include-previous-data-question.component.scss']
})
export class IncludePreviousDataQuestionComponent {
  @Input() question: string;
  @Input() message: string;
  @Input() addButtonLabel?: string;
  @Input() discardButtonLabel?: string;
  @Input() viewError?: boolean;

  @Output() response = new EventEmitter<boolean>();

}
