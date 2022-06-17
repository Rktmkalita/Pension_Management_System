import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent {
  constructor(private loginService: LoginService) {}

  ngOnInit(): void {}

  loggedIn = this.loginService.isLoggedIn();
  username = this.loginService.loginUserName();

  logoutUser() {
    this.loginService.logout();
  }
}
