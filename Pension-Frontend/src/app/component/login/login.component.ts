import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  @ViewChild('loginForm') loginForm: NgForm;
  error: string = '';
  authResponse: any;
  user = {
    userName: '',
    password: '',
  };

  constructor(private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {}

  onSubmit() {
    this.user.userName = this.loginForm.value.user.username;
    this.user.password = this.loginForm.value.user.password;

    console.log('Login details submitted');

    this.loginService.generateToken(this.user).subscribe(
      (data: any) => {
        console.log(data);
        this.authResponse = data;
        this.loginService.loginUser(this.authResponse.token);
        this.loginService.autologout(this.authResponse.validity);
        this.router.navigate(['/pensioner/search']);
      },
      (err) => {
        // console.log(err.error);
        console.log('User or Password incorrect');
        this.error = 'Please enter valid credentials';
      }
    );
  }
}
