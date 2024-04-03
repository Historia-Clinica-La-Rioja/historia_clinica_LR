import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { HeaderAttentionDetailComponent } from '../../../hsi-components/header-attention-detail/header-attention-detail.component';
import { JointSignatureRoutingModule } from './joint-signature-routing.module';
import { PresentationModule } from '@presentation/presentation.module';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    HeaderAttentionDetailComponent,
    JointSignatureRoutingModule,
    PresentationModule,
  ]
})
export class JointSignatureModule { }
