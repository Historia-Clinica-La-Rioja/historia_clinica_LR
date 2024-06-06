import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';

@Component({
  selector: 'app-study-list-transcribed',
  templateUrl: './study-list-transcribed.component.html',
  styleUrls: ['./study-list-transcribed.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudyListTranscribedComponent implements OnInit {

  @Input() studyList:SnomedDto
  @Output() removeEvent = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  executeRemove(value:number) {
    this.removeEvent.emit(value);
  }
}
