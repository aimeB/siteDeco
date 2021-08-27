import { NgModule, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/fr';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { TranslateModule, TranslateService, TranslateLoader, MissingTranslationHandler } from '@ngx-translate/core';
import { NgxWebstorageModule, SessionStorageService } from 'ngx-webstorage';
import * as dayjs from 'dayjs';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import { SERVER_API_URL } from './app.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
import { EntityRoutingModule } from './entities/entity-routing.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { translatePartialLoader, missingTranslationHandler } from './config/translation.config';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

import { SignupComponent } from './new-comp/auth/signup/signup.component';
import { AcceuilComponent } from './new-comp/acceuil/acceuil.component';
import { FaqComponent } from './new-comp/faq/faq.component';
import { SingleEventComponent } from './new-comp/event-list/single-event/single-event.component';
import { ArticleListComponent } from './new-comp/article-list/article-list.component';
import { ArticleFormComponent } from './new-comp/article-list/article-form/article-form.component';
import { HeaderComponent } from './new-comp/header/header.component';
import { BlogListComponent } from './new-comp/blog-list/blog-list.component';
import { BlogFormComponent } from './new-comp/blog-list/blog-form/blog-form.component';
import { SingleDevisComponent } from './new-comp/devis-list/single-devis/single-devis.component';
import { ContactComponent } from './new-comp/contact/contact.component';
import { ListComponent } from './new-comp/user/list/list.component';
import { SigninComponent } from './new-comp/auth/signin/signin.component';
import { MentionLegaleComponent } from './new-comp/mention-legale/mention-legale.component';
import { ConditionsGeneraleComponent } from './new-comp/conditions-generale/conditions-generale.component';
import { EventListComponent } from './new-comp/event-list/event-list.component';
import { EventFormComponent } from './new-comp/event-list/event-form/event-form.component';
import { SingleArticleComponent } from './new-comp/article-list/single-article/single-article.component';
import { StockComponent } from './new-comp/stock/stock.component';
import { GalerieComponent } from './new-comp/galerie/galerie.component';
import { FooterComponent } from './new-comp/footer/footer.component';
import { AproposComponent } from './new-comp/apropos/apropos.component';
import { SingleBlogComponent } from './new-comp/blog-list/single-blog/single-blog.component';
import { DevisListComponent } from './new-comp/devis-list/devis-list.component';
import { DevisFormComponent } from './new-comp/devis-list/devis-form/devis-form.component';
import { DisponibiliteComponent } from './new-comp/disponibilite/disponibilite.component';
import { AccueilService } from './new-comp/services/accueil.service';

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    EntityRoutingModule,
    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-', caseSensitive: true }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translatePartialLoader,
        deps: [HttpClient],
      },
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useFactory: missingTranslationHandler,
      },
    }),
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'fr' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    httpInterceptorProviders,
    AccueilService,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    SignupComponent,
    AcceuilComponent,
    FaqComponent,
    SingleEventComponent,
    ArticleListComponent,
    ArticleFormComponent,
    HeaderComponent,
    FooterComponent,
    BlogListComponent,
    BlogFormComponent,
    SingleDevisComponent,
    ContactComponent,
    ListComponent,
    SigninComponent,
    MentionLegaleComponent,
    ConditionsGeneraleComponent,
    EventListComponent,
    EventFormComponent,
    SingleArticleComponent,
    StockComponent,
    GalerieComponent,
    AproposComponent,
    SingleBlogComponent,
    DevisListComponent,
    DevisFormComponent,
    DisponibiliteComponent,
  ],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(
    applicationConfigService: ApplicationConfigService,
    iconLibrary: FaIconLibrary,
    dpConfig: NgbDatepickerConfig,
    translateService: TranslateService,
    sessionStorageService: SessionStorageService
  ) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
    translateService.setDefaultLang('fr');
    // if user have changed language and navigates away from the application and back to the application then use previously choosed language
    const langKey = sessionStorageService.retrieve('locale') ?? 'fr';
    translateService.use(langKey);
  }
}
