import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-description-item',
    templateUrl: './description-item.component.html',
    styleUrls: ['./description-item.component.scss']
})
export class DescriptionItemComponent {
    @Input() descriptionData: DescriptionItemData[];

    constructor() { }
}

export interface DescriptionItemData {
    description: string
    dateToShow?: DateToShow
}

export interface DateToShow {
    date: Date,
    format: DateFormat,
}
   
export enum DateFormat {
    DATE = 'date',
    TIME = 'time',
    DATE_TIME = 'datetime',
}