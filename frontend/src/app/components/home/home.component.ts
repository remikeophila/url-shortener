import { Component } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UrlShortenerService } from '../../services/url-shortener.service';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './home.component.html'
})
export class HomeComponent {
  url: string = '';
  shortenedUrl: string = '';
  isError:boolean = false;
  urlControl = new FormControl('', [Validators.required, this.urlValidator]);


  constructor(private urlShortenerService: UrlShortenerService, private location: Location) {}

  shortenUrl() {
    if(this.urlControl.valid) {
      this.isError = false;
    } else {
      this.isError = true;
      return;
    }
    if (this.url) {
      this.urlShortenerService.shortenUrl(this.url).subscribe({
        next: (response: any) => {
          console.log("had a response: "+response.shortenedUrl);
          this.shortenedUrl = response.shortenedUrl;
        },
        error: (error: any) => {
          
        }
      });
    }
  }

  getFullShortenedUrl(): string {
    return window.location.origin + this.location.prepareExternalUrl(this.shortenedUrl);
  }

  urlValidator(control: FormControl): { [key: string]: any } | null {
    // Source: https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
    const urlPattern = new RegExp('(https?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\/=]*)');
    console.log(urlPattern.test(control.value) ? null : { 'invalidUrl': true });
    return urlPattern.test(control.value) ? null : { 'invalidUrl': true };
  }
  
}
