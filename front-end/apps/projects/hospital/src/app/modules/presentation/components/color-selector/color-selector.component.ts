import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { COLOR } from '@turnos/constants/appointment';

@Component({
  selector: 'app-color-selector',
  templateUrl: './color-selector.component.html',
  styleUrls: ['./color-selector.component.scss']
})
export class ColorSelectorComponent implements OnInit {

	@Input() colorList: COLOR[];
	@Input() currentColor: COLOR;
	@Input() addButtonText: string;
	@Output() newColor: EventEmitter<COLOR> = new EventEmitter();

  	constructor() { }

	ngOnInit() {
		if (this.currentColor && this.addButtonText) 
			this.addButtonText = undefined;
		
	}

	selectColor(newColor: COLOR) {
		this.currentColor = newColor;
		this.newColor.emit(this.currentColor);
	}
}