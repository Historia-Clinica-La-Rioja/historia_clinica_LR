import { Component, Input, Output, EventEmitter } from '@angular/core';
 

@Component({
  selector: 'app-input-desempeno',
  templateUrl: './input-desempeno.component.html',
  styleUrls: ['./input-desempeno.component.scss']
})
export class InputDesempenoComponent {
  @Input() selectedOption: string;
  @Input() counterValue: number;

  @Output() optionChange = new EventEmitter<string>();
  @Output() counterChange = new EventEmitter<number>();
  selectedoptionA: string;

  

  onOptionChange(): void {
    this.optionChange.emit(this.selectedOption);
  }

  onCounterChange(): void {
    this.counterChange.emit(this.counterValue);
  }
}

