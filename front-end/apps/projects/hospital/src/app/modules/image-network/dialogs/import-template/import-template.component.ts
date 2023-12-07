import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TemplateNamesDto } from '@api-rest/api-model';
import { TemplateManagementService } from '../../services/template-management.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-import-template',
	templateUrl: './import-template.component.html',
	styleUrls: ['./import-template.component.scss']
})
export class ImportTemplateComponent implements OnInit {

	importTemplates$: Observable<TemplateNamesDto[]>

	constructor(
		public dialogRef: MatDialogRef<ImportTemplateComponent>,
		private readonly templateManagementService: TemplateManagementService,
	) { }

	ngOnInit(): void {
		this.importTemplates$ = this.templateManagementService.getTemplatesImport()
	}

	importTemplate(event:TemplateNamesDto){
		this.templateManagementService.getOneTemplateImports(event.id)
		.subscribe(template => {
			this.dialogRef.close(template.text)
		} )
	}

}

export interface TemplateInfoDto {
	fileName: string;
	evolutionNote: string;
}
