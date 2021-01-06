import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEpisodeAdultGynecologicalTriageComponent } from './new-episode-adult-gynecological-triage.component';

describe('NewEpisodeAdultGynecologicalTriageComponent', () => {
  let component: NewEpisodeAdultGynecologicalTriageComponent;
  let fixture: ComponentFixture<NewEpisodeAdultGynecologicalTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewEpisodeAdultGynecologicalTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewEpisodeAdultGynecologicalTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
