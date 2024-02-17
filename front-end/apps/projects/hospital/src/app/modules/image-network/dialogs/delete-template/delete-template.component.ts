import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TemplateManagementService } from '../../services/template-management.service';
import { Observable } from 'rxjs';
import { TemplateNamesDto } from '@api-rest/api-model';

@Component({
	selector: 'app-delete-template',
	templateUrl: './delete-template.component.html',
	styleUrls: ['./delete-template.component.scss']
})
export class DeleteTemplateComponent implements OnInit {

	importTemplates$: Observable<TemplateNamesDto[]>

	constructor(
		public dialogRef: MatDialogRef<DeleteTemplateComponent>,
		private readonly templateManagementService: TemplateManagementService,
	) { }

	ngOnInit(): void {
		this.importTemplates$ = this.templateManagementService.getTemplatesImport()
	}

	deleteTemplate(event:TemplateNamesDto): void {
		this.templateManagementService.deleteTemplate(event.id).subscribe(
			_ => this.dialogRef.close(true)
		)
	}

}
