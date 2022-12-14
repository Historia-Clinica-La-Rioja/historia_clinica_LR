import { Component, Input, OnInit } from '@angular/core';
import { DateDto } from '@api-rest/api-model';

@Component({
  selector: 'app-interment-document-episode',
  templateUrl: './interment-document-episode.component.html',
  styleUrls: ['./interment-document-episode.component.scss']
})
export class IntermentDocumentEpisodeComponent implements OnInit {

  @Input() documents: Document;

  constructor() { }

  ngOnInit(): void {
  }

}

export interface Document {
	type: string;
	fileName: string;
	date: DateDto;
}