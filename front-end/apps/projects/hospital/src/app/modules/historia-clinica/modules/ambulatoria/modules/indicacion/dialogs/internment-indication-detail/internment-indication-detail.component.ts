import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-internment-indication-detail',
  templateUrl: './internment-indication-detail.component.html',
  styleUrls: ['./internment-indication-detail.component.scss']
})
export class InternmentIndicationDetailComponent implements OnInit {

  indication: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { indication: string},
  ) { }

  ngOnInit(): void {
    this.indication = this.data.indication;
  }

}
