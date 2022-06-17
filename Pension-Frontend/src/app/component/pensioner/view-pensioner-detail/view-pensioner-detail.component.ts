import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PensionerService } from 'src/app/services/pensioner.service';

@Component({
  selector: 'app-view-pensioner-detail',
  templateUrl: './view-pensioner-detail.component.html',
  styleUrls: ['./view-pensioner-detail.component.css'],
})
export class ViewPensionerDetailComponent implements OnInit {
  pensionerAadhar: string = '';
  pensionerDetails: any;
  message: string;
  constructor(
    private pensionerService: PensionerService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((data) => {
      this.pensionerAadhar = data.id;
    });

    this.pensionerService.viewPensioner(this.pensionerAadhar).subscribe(
      (data) => {
        this.pensionerDetails = data;
        this.message = 'Profile Fetched Sucessfully';
        console.log(this.message);
      },
      () => {
        this.message =
          'Something went wrong! Could not fetch profile. Please try again later';
        window.alert(this.message);
      }
    );
  }

  processPension() {
    this.router.navigate(['/pensioner/process']);
  }
}
