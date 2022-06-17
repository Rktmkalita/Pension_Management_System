import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { HomeComponent } from './component/home/home.component';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { LoginService } from './services/login.service';
import { ViewPensionerDetailComponent } from './component/pensioner/view-pensioner-detail/view-pensioner-detail.component';
import { ViewPensionComponent } from './component/pensioner/view-pension/view-pension.component';
import { PageNotFoundComponent } from './component/page-not-found/page-not-found.component';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptorService } from './services/auth.interceptor';
import { SearchPensionerComponent } from './component/pensioner/search-pensioner/search-pensioner.component';
import { ListPensionerDetailComponent } from './component/pensioner/list-pensioner-detail/list-pensioner-detail.component';
import { PensionerService } from './services/pensioner.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    NavBarComponent,
    SearchPensionerComponent,
    ViewPensionerDetailComponent,
    ViewPensionComponent,
    PageNotFoundComponent,
    ListPensionerDetailComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule, HttpClientModule],
  providers: [
    LoginService,
    PensionerService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
