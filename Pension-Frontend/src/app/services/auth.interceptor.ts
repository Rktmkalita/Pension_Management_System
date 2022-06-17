import { Injectable, Injector } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';

import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private injector: Injector) {}
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authToken = this.injector.get(LoginService);
    const reqHeader = req.clone({
      headers: req.headers.set(
        'Authorization',
        `Bearer ` + authToken.getToken()
      ),
    });
    if (authToken.isLoggedIn() == true) {
      return next.handle(reqHeader);
    } else {
      return next.handle(req);
    }
  }
}
