import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-box-message',
  templateUrl: './box-message.component.html',
  styleUrls: ['./box-message.component.scss']
})
export class BoxMessageComponent {

  @Input() boxMessageInfo: BoxMessageInformation;
  @Output() response = new EventEmitter<boolean>();

}

export interface BoxMessageInformation {
  title?: string,
  question?: string,
  message?: string,
  addButtonLabel?: string,
  discardButtonLabel?: string,
  viewError?: boolean,
  showButtons: boolean
}
