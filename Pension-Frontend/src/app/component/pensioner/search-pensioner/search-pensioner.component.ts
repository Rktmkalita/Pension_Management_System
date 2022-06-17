import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { PensionerService } from 'src/app/services/pensioner.service';

@Component({
  selector: 'app-search-pensioner',
  templateUrl: './search-pensioner.component.html',
  styleUrls: ['./search-pensioner.component.css'],
})
export class SearchPensionerComponent implements OnInit {
  aadharNumber: any;
  message: string;
  error: any;
  constructor(
    private pensionerService: PensionerService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  onSubmit(searchPensionerForm: NgForm) {
    this.aadharNumber = searchPensionerForm.value.aadharNumber;
    this.pensionerService.viewPensioner(this.aadharNumber).subscribe(
      (data) => {
        console.log('Pensioner Found');
        this.router.navigate(['/pensioner/view', this.aadharNumber]);
      },
      (error) => {
        this.message =
          '“Invalid pensioner detail provided, please provide valid detail.';
        console.log(this.message);
      }
    );
  }
}
