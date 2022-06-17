import { Component, OnInit } from '@angular/core';
import { PensionerService } from 'src/app/services/pensioner.service';

@Component({
  selector: 'app-view-pension',
  templateUrl: './view-pension.component.html',
  styleUrls: ['./view-pension.component.css'],
})
export class ViewPensionComponent implements OnInit {
  pensionerAadhar: string = '';
  pensionerDetails: any;
  pensionAmounts: any;
  message: string;
  constructor(private pensionerService: PensionerService) {
    this.pensionerAadhar = this.pensionerService.aadhar;
  }

  ngOnInit(): void {
    this.processPension();

    this.pensionerService
      .viewPensioner(this.pensionerAadhar)
      .subscribe((data) => {
        this.pensionerDetails = data;
      });
  }

  processPension() {
    this.pensionerService.processPensioner(this.pensionerAadhar).subscribe(
      (data) => {
        this.pensionAmounts = data;
        this.message = 'Pension Processed Successfully';
        console.log(this.message);
      },
      (err) => {
        this.message = 'Something went wrong! Please try again later';
        console.log(this.message);
      }
    );
  }
}
