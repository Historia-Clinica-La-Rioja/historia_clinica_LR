import { Component, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogWidth } from '@presentation/components/dialog-presentation/dialog-presentation.component';

@Component({
    selector: 'app-confirm-dialog-v2',
    templateUrl: './confirm-dialog-v2.component.html',
    styleUrls: ['./confirm-dialog-v2.component.scss']
})
export class ConfirmDialogV2Component implements OnInit {

    DIALOG_WIDTH = DialogWidth;
    themePalette: ThemePalette;

    constructor( @Inject(MAT_DIALOG_DATA) public data: DialogConfiguration ) { }

    ngOnInit(): void {
        this.themePalette = this.data.color? this.data.color : 'primary';
    }

}

export interface DialogConfiguration {
    dialogWidth: DialogWidth,
    title?: string,
    hasIcon: boolean,
    content: string,
    contentBold?: string,
    okButtonLabel?: string,
    cancelButtonLabel?: string,
    color?: ThemePalette,
    buttonClose?: boolean,
}