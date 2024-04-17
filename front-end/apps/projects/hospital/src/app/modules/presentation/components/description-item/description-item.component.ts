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
    dateOrTime?: {
        date?: Date,
        time?: Date,
        dateTime?: Date,
    }
}