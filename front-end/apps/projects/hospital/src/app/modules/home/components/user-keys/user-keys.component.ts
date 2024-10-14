import { Component, OnInit } from '@angular/core';
import { UserKeysService } from './user-keys.service';
import { GenerateApiKeyFormComponent } from "../../dialogs/generate-api-key-form/generate-api-key-form.component";
import { MatDialog } from "@angular/material/dialog";

@Component({
    selector: 'app-user-keys',
    templateUrl: './user-keys.component.html',
    styleUrls: ['./user-keys.component.scss']
})
export class UserKeysComponent implements OnInit {
    userKeys$;


    constructor(
        private userKeysService: UserKeysService,
        public dialog: MatDialog
    ) {
    }

    ngOnInit(): void {
        this.userKeys$ = this.userKeysService.list$.asObservable();
        this.userKeysService.fetch();
    }

    generate() {
        this.openGenerateApiKeyDialog();
    }

	delete(apiKeyName: string) {
		this.userKeysService.delete(apiKeyName);
	}

    private openGenerateApiKeyDialog() {
        this.dialog.open(GenerateApiKeyFormComponent, {
            width: '35%',
            autoFocus: false
        });
    }
}
