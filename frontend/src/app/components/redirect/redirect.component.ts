import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UrlShortenerService } from '../../services/url-shortener.service';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
})
export class RedirectComponent implements OnInit {
  constructor(private urlShortenerService: UrlShortenerService
    , private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    const shortUrl: string = this.route.snapshot.paramMap.get('shortUrl')!;
    this.urlShortenerService.resolveShortUrl(shortUrl).subscribe({
      next: (response) => {
        const destinationUrl = response.destinationUrl.startsWith('http') ? response.destinationUrl : `http://${response.destinationUrl}`;
        window.location.href = destinationUrl;
      },
      error: (error) => {
        console.error('Redirection failed', error);
        window.location.href = "notfound";
      }
    });
  }
}
