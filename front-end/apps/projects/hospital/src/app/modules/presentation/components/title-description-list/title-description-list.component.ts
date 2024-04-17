import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '../description-item/description-item.component';

@Component({
    selector: 'app-title-description-list',
    templateUrl: './title-description-list.component.html',
    styleUrls: ['./title-description-list.component.scss']
})
export class TitleDescriptionListComponent {

    @Input() title: string;
    @Input() descriptionData?: DescriptionItemData[];
}
