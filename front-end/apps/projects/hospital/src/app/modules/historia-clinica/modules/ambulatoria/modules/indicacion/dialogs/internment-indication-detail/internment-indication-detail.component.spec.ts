import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternmentIndicationDetailComponent } from './internment-indication-detail.component';

describe('InternmentIndicationDetailComponent', () => {
  let component: InternmentIndicationDetailComponent;
  let fixture: ComponentFixture<InternmentIndicationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InternmentIndicationDetailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InternmentIndicationDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
