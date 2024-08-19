import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MatRadioChange } from '@angular/material/radio';

const DEFAULT_RADIO_GROUP = [
    {value: 1, description: "presentation.radio-group.YES"},
    {value: 0, description: "presentation.radio-group.NO"}
]

@Component({
    selector: 'app-radio-group',
    templateUrl: './radio-group.component.html',
    styleUrls: ['./radio-group.component.scss']
})

export class RadioGroupComponent {

    @Input() set radioGroupInputData(radioGroupInputData: RadioGroupInputData) {
        this._radioGroupInputData = {
            presentation: {
                title: radioGroupInputData.presentation.title,
                data: radioGroupInputData.presentation.data || DEFAULT_RADIO_GROUP,
                color: radioGroupInputData.presentation.color || 'primary',
				previousValueId: radioGroupInputData.presentation.previousValueId,
            },
            alignments: {
                position: radioGroupInputData.alignments.position || Position.ROW,
                optionsPosition: radioGroupInputData.alignments.optionsPosition || Position.ROW
            }
        }
    };
    _radioGroupInputData: RadioGroupInputData;
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
    description: string,
	checked?: boolean
}

export interface RadioGroupInputData {
    presentation: {
        title: string,
        data?: RadioGroupData[],
        color?: ThemePalette,
		previousValueId?: number
    },
    alignments: {
        position?: string,
        optionsPosition?: string
    }
}

export function generateRadioGroupInputData(title: string, data?: RadioGroupData[], color?: ThemePalette, previousValueId?: number, position?: string, optionsPosition?: string): RadioGroupInputData {
    return {
        presentation: {
            title,
            data,
            color,
			previousValueId,
        },
        alignments: {
            position,
            optionsPosition,
        }
    }
}
