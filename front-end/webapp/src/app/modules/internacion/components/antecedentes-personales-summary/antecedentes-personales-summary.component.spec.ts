import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntecedentesPersonalesSummaryComponent } from './antecedentes-personales-summary.component';

describe('AntecedentesPersonalesSummaryComponent', () => {
  let component: AntecedentesPersonalesSummaryComponent;
  let fixture: ComponentFixture<AntecedentesPersonalesSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntecedentesPersonalesSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntecedentesPersonalesSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
