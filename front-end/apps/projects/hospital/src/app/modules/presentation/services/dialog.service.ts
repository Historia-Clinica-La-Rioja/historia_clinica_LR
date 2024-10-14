import { ComponentType } from '@angular/cdk/overlay';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Injectable({
    providedIn: 'root'
})
export class DialogService<T> {

    constructor( private dialog: MatDialog ) { }

    open(component: ComponentType<T>, configuration: DialogConfiguration, componentData: any): MatDialogRef<T> {
        return this.dialog.open(component, {
            width: configuration.dialogWidth,
            maxHeight: '90vh',
            autoFocus: false,
            disableClose: configuration.blockCloseClickingOut? configuration.blockCloseClickingOut : true,
            data: componentData
        });
    }

}

export interface DialogConfiguration {
    dialogWidth: DialogWidth,
    blockCloseClickingOut?: boolean,
}

export enum DialogWidth {
    SMALL = '520px',
    MEDIUM = '760px',
    LARGE = '960px'
}