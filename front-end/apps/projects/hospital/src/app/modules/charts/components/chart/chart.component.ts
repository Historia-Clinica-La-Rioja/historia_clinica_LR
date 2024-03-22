import { Component, Input } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent {

  @Input() chart: Chart;

  constructor() { }

}

export interface Chart {
  labels: string[];
  type: ChartType;
  dataSets: DataSet[];
  options: ChartOptions;
}

export interface DataSet {
  label: string;
  data: number | Intersection[];
  borderColor?: string;
  borderWidth?: number;
  pointBackgroundColor?: string,
  pointRadius?: number;
  fill?: boolean;
}

export interface Intersection {
  x: string;
  y: string;
}