import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonShortDescriptionComponent } from './person-short-description.component';

describe('PersonShortDescriptionComponent', () => {
  let component: PersonShortDescriptionComponent;
  let fixture: ComponentFixture<PersonShortDescriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PersonShortDescriptionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PersonShortDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
