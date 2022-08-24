import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-update-password-success',
  templateUrl: './update-password-success.component.html',
  styleUrls: ['./update-password-success.component.scss']
})
export class UpdatePasswordSuccessComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  submit(){
	  this.router.navigate(['home']);
  }

}
