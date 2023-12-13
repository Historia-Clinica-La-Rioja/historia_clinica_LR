import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InProgressCallComponent } from './in-progress-call.component';

describe('InProgressCallComponent', () => {
  let component: InProgressCallComponent;
  let fixture: ComponentFixture<InProgressCallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InProgressCallComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InProgressCallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
