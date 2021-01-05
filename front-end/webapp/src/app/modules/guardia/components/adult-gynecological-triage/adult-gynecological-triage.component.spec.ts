import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdultGynecologicalTriageComponent } from './adult-gynecological-triage.component';

describe('AdultGynecologicalTriageComponent', () => {
  let component: AdultGynecologicalTriageComponent;
  let fixture: ComponentFixture<AdultGynecologicalTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdultGynecologicalTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdultGynecologicalTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
