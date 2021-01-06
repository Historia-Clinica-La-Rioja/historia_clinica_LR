import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PediatricTriageComponent } from './pediatric-triage.component';

describe('PediatricTriageComponent', () => {
  let component: PediatricTriageComponent;
  let fixture: ComponentFixture<PediatricTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PediatricTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PediatricTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
