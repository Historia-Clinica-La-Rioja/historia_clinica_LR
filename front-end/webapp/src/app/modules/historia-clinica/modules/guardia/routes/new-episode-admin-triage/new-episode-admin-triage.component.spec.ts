import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEpisodeAdminTriageComponent } from './new-episode-admin-triage.component';

describe('NewEpisodeAdminTriageComponent', () => {
  let component: NewEpisodeAdminTriageComponent;
  let fixture: ComponentFixture<NewEpisodeAdminTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewEpisodeAdminTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewEpisodeAdminTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
