import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-iconed-titled-section',
    templateUrl: './iconed-titled-section.component.html',
    styleUrls: ['./iconed-titled-section.component.scss']
})
export class IconedTitledSectionComponent {
    @Input() icon: string;
    @Input() title: string;
}
