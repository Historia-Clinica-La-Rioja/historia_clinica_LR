import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-dialog-presentation',
    templateUrl: './dialog-presentation.component.html',
    styleUrls: ['./dialog-presentation.component.scss']
})
export class DialogPresentationComponent {

    DIALOG_WIDTH = DialogWidth;
    @Input() dialogWidth: DialogWidth;

    constructor() { }

}

export enum DialogWidth {
    SMALL = '520px',
    MEDIUM = '760px',
    LARGE = '960px'
}