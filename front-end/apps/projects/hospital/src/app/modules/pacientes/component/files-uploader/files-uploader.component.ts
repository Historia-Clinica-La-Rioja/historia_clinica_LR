import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-files-uploader',
  templateUrl: './files-uploader.component.html',
  styleUrls: ['./files-uploader.component.scss']
})
export class FilesUploaderComponent implements OnInit {
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];

  constructor() { }

  ngOnInit() {
  }

  onSelectFileFormData($event) {
	Array.from($event.target.files).forEach((file: File) => {
		this.selectedFiles.push(file);
		this.selectedFilesShow.push(file.name);
	});
	}

	removeSelectedFile(index) {
	this.selectedFiles.splice(index, 1);
	this.selectedFilesShow.splice(index, 1);
	}

}
