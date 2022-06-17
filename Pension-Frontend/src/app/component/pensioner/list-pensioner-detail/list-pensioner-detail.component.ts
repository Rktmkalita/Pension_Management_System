import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { PensionerService } from 'src/app/services/pensioner.service';

@Component({
  selector: 'app-list-pensioner-detail',
  templateUrl: './list-pensioner-detail.component.html',
  styleUrls: ['./list-pensioner-detail.component.css'],
})
export class ListPensionerDetailComponent implements OnInit {
  isEmpty = true;
  listPensionerDetails: any;
  constructor(private pensionerService: PensionerService) {}

  ngOnInit(): void {
    this.pensionerService.listPensionerDetails().subscribe((data) => {
      this.listPensionerDetails = data;
      if (
        this.listPensionerDetails != undefined ||
        this.listPensionerDetails != null
      ) {
        this.isEmpty = false;
      }
    });
  }
}
