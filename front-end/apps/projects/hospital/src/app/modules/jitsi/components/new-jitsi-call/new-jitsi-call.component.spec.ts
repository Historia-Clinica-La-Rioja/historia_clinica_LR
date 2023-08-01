import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NewJitsiCallComponent } from './new-jitsi-call.component';



describe('NewJitsiCallComponent', () => {
  let component: NewJitsiCallComponent;
  let fixture: ComponentFixture<NewJitsiCallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewJitsiCallComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewJitsiCallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
