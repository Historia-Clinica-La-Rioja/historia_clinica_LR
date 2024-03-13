import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartComponent } from './components/chart/chart.component';
import { NgChartsModule } from 'ng2-charts';


@NgModule({
  declarations: [
    ChartComponent,
  ],
  imports: [
    CommonModule,
    //deps
    NgChartsModule,
  ],
  exports: [
    ChartComponent,
  ]
})
export class ChartsModule { }
