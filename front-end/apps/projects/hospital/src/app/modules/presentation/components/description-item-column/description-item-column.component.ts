import { Component, Input } from '@angular/core';
import { DescriptionItemDataInfo } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-description-item-column',
    templateUrl: './description-item-column.component.html',
    styleUrls: ['./description-item-column.component.scss']
})
export class DescriptionItemColumnComponent {

    @Input() itemsList: DescriptionItemDataInfo[];
    constructor() { }
}