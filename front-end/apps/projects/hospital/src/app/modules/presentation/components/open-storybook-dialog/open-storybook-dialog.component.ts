import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ComponentType } from '@angular/cdk/overlay';
@Component({
    selector: 'app-open-storybook-dialog',
    templateUrl: './open-storybook-dialog.component.html',
    styleUrls: ['./open-storybook-dialog.component.scss']
})
export class OpenStorybookDialogComponent<T> {

    @Input() dialogData: any;
    @Input() component: ComponentType<T>

    constructor(private _dialog: MatDialog) {}

    public launch(): void {
        this._dialog.open(this.component, {
            data: this.dialogData
        });
    }
}
