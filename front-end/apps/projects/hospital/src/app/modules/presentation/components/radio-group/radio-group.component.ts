import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MatRadioChange } from '@angular/material/radio';

const DEFAULT_RADIO_GROUP = [
    {value: 1, description: "presentation.radio-group.YES"},
    {value: 2, description: "presentation.radio-group.NO"}
]

@Component({
    selector: 'app-radio-group',
    templateUrl: './radio-group.component.html',
    styleUrls: ['./radio-group.component.scss']
})

export class RadioGroupComponent {

    @Input() title: string;
    @Input() position = Position.ROW;
    @Input() optionsPosition = Position.ROW;
    @Input() data: RadioGroupData[] = DEFAULT_RADIO_GROUP;
	@Input() color?: ThemePalette = 'primary';
    @Output() onOptionChange: EventEmitter<RadioGroupData> = new EventEmitter<RadioGroupData>();

    constructor() { }

    onValueChange($event: MatRadioChange){
        this.onOptionChange.emit($event.value)
    }
}

export enum Position {
	ROW = 'row',
	COLUMN = 'column'
}

export interface RadioGroupData {
    value: number,
    description: string
}