import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonType } from '@presentation/components/button/button.component';


@Component({
  selector: 'app-pending-task-item',
  templateUrl: './pending-task-item.component.html',
  styleUrls: ['./pending-task-item.component.scss']
})
export class PendingTaskItemComponent  {

  @Input() item: PendingTaskItem;
  @Output() clicked = new EventEmitter<boolean>();

  readonly buttonType = ButtonType.BASIC;
  
  constructor(  ) { }

  onClick() {
    this.clicked.emit(true);
  }

}

export interface PendingTaskItem {
  extensionName: string;
  emittedMessage: string;
}