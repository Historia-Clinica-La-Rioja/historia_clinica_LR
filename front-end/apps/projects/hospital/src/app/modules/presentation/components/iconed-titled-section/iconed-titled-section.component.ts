import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-iconed-titled-section',
    templateUrl: './iconed-titled-section.component.html',
    styleUrls: ['./iconed-titled-section.component.scss']
})
export class IconedTitledSectionComponent implements OnInit {
    @Input() icon: string;
    @Input() title: string;

    constructor() { }

    ngOnInit(): void {
    }

}
