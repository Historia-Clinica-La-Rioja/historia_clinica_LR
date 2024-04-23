import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-input-point',
  templateUrl: './input-point.component.html',
  styleUrls: ['./input-point.component.scss']
})
export class InputPointComponent {
  @Input() selectedOption: string;
  @Input() counterValue: number;

  @Output() optionChange = new EventEmitter<string>();
  @Output() counterChange = new EventEmitter<number>();
  selectedoptionE2: string;

  

  onOptionChange(): void {
    this.optionChange.emit(this.selectedOption);
  }

  onInputDesempenoChange2(): void {
    this.counterChange.emit(this.counterValue);
  }
}