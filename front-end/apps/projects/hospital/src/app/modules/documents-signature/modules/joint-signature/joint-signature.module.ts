import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JointSignatureRoutingModule } from './joint-signature-routing.module';
import { HomeComponent } from './routes/home/home.component';


@NgModule({
  declarations: [
  
    HomeComponent
  ],
  imports: [
    CommonModule,
    JointSignatureRoutingModule
  ]
})
export class JointSignatureModule { }
