import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UrlShortenerService {
  private backendUrl = 'http://localhost:8080'; // Adjust this to your actual backend URL

  constructor(private http: HttpClient) {}

  shortenUrl(originalUrl: string): Observable<any> {
    return this.http.post<any>(`${this.backendUrl}/shorten`, { url: originalUrl } );
  }

  resolveShortUrl(shortUrl: string): Observable<{ destinationUrl: string }> {
    return this.http.get<{ destinationUrl: string }>(`http://localhost:8080/${shortUrl}`);
  }
}
