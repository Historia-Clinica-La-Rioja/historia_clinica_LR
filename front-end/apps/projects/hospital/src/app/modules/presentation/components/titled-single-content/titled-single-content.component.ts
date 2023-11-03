import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-titled-single-content',
    templateUrl: './titled-single-content.component.html',
    styleUrls: ['./titled-single-content.component.scss']
})
export class TitledSingleContentComponent implements OnInit {

    @Input() icon: string;
    @Input() title: string;
    @Input() content: string[];

    constructor() { }

    ngOnInit(): void {
    }

}
