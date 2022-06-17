import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs/internal/observable/throwError';
import { catchError } from 'rxjs/internal/operators/catchError';
import { baseUrlProcess } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: HttpClient, private router: Router) {}

  generateToken(credentials: any) {
    localStorage.setItem('username', credentials.userName);
    return this.http
      .post(`${baseUrlProcess}authenticate`, credentials)
      .pipe(catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

  loginUser(token: string) {
    localStorage.setItem('token', token);
    return true;
  }

  loginUserName() {
    return localStorage.getItem('username');
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['login']);
    return true;
  }

  autologout(validity: number) {
    setTimeout(() => {
      this.logout();
    }, validity * 1000);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isLoggedIn() {
    let token = localStorage.getItem('token');
    if (token == undefined || token === '' || token == null) {
      return false;
    } else {
      return true;
    }
  }
}
