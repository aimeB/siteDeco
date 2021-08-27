import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

const URL =
  'https://player.vimeo.com/video/416594060?loop=1&amp;autoplay=1&amp;title=0&amp;byline=0&amp;setVolume=0&amp;api=1&amp;player_id=1';

@Component({
  selector: 'app-acceuil',
  templateUrl: './acceuil.component.html',
  styleUrls: ['./acceuil.component.scss'],
})
export class AcceuilComponent {
  constructor(public sanitizer: DomSanitizer) {}

  getSafeUrl(): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl(URL);
  }
}
