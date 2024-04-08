import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-title-description-list',
    templateUrl: './title-description-list.component.html',
    styleUrls: ['./title-description-list.component.scss']
})
export class TitleDescriptionListComponent {

    @Input() title: string;
    @Input() description: string[];
    @Input() colonsOnTitle?: boolean;
}
