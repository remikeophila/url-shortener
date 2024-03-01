import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UrlShortenerService } from '../../services/url-shortener.service';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './home.component.html'
})
export class HomeComponent {
  url: string = '';
  shortenedUrl: string = '';

  constructor(private urlShortenerService: UrlShortenerService, private location: Location) {}

  shortenUrl() {
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

}
