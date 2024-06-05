import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogV2Component, DialogConfiguration } from '@presentation/dialogs/confirm-dialog-v2/confirm-dialog-v2.component';

@Component({
    selector: 'app-open-storybook-dialog',
    templateUrl: './open-storybook-dialog.component.html',
    styleUrls: ['./open-storybook-dialog.component.scss']
})
export class OpenStorybookDialogComponent {

    @Input() dialogData: DialogConfiguration;

    constructor(private _dialog: MatDialog) {}

    public launch(): void {
        this._dialog.open(ConfirmDialogV2Component, {
            data: this.dialogData
        });
    }
}
