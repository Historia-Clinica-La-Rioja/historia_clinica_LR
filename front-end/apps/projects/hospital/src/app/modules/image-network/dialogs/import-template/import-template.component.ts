import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';

@Component({
	selector: 'app-import-template',
	templateUrl: './import-template.component.html',
	styleUrls: ['./import-template.component.scss']
})
export class ImportTemplateComponent implements OnInit {

	importsFiles$: Observable<TemplateInfoDto[]> = of(mockTemplate)

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: TemplateInfoDto[],
		public dialogRef: MatDialogRef<ImportTemplateComponent>,
	) { }

	ngOnInit(): void {
	}

	importTemplate(event:TemplateInfoDto){
		this.dialogRef.close(event.evolutionNote)
	}

}

export interface TemplateInfoDto {
	fileName: string;
	evolutionNote: string;
}

export const mockTemplate: TemplateInfoDto[] = [
	{
		fileName: 'template radiografia ok',
		evolutionNote: '<ol><li>xxx</li><li>euraca</li><li>europa</li><li>eurostar</li><li>eurotrip</li></ol>'
	},
	{
		fileName: 'template contusion',
		evolutionNote: ''
	},
	{
		fileName: 'template fiebre',
		evolutionNote: ''
	},
	{
		fileName: 'template covid',
		evolutionNote: ''
	},
	{
		fileName: 'template covid',
		evolutionNote: ''
	},
	{
		fileName: 'template covid',
		evolutionNote: ''
	},
	{
		fileName: 'template covid',
		evolutionNote: ''
	}
]