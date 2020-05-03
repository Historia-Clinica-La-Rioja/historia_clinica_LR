import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntropometricosSummaryComponent } from './antropometricos-summary.component';

describe('AntropometricosSummaryComponent', () => {
  let component: AntropometricosSummaryComponent;
  let fixture: ComponentFixture<AntropometricosSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntropometricosSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntropometricosSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
