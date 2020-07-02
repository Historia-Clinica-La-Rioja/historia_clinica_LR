import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignosVitalesSummaryComponent } from './signos-vitales-summary.component';

describe('SignosVitalesSummaryComponent', () => {
  let component: SignosVitalesSummaryComponent;
  let fixture: ComponentFixture<SignosVitalesSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignosVitalesSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignosVitalesSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
