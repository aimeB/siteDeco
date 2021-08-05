import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BsDropdownModule, BsDropdownConfig } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AppComponent } from './app.component';
import { SignupComponent } from './auth/signup/signup.component';
import { AcceuilComponent } from './acceuil/acceuil.component';
import { FaqComponent } from './faq/faq.component';
import { SingleEventComponent } from './event-list/single-event/single-event.component';
import { ArticleListComponent } from './article-list/article-list.component';
import { ArticleFormComponent } from './article-list/article-form/article-form.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { BlogListComponent } from './blog-list/blog-list.component';
import { BlogFormComponent } from './blog-list/blog-form/blog-form.component';
import { SingleDevisComponent } from './devis-list/single-devis/single-devis.component';
import { ContactComponent } from './contact/contact.component';
import { ListComponent } from './user/list/list.component';
import { SigninComponent } from './auth/signin/signin.component';
import { MentionLegaleComponent } from './mention-legale/mention-legale.component';
import { ConditionsGeneraleComponent } from './conditions-generale/conditions-generale.component';
import { EventListComponent } from './event-list/event-list.component';
import { EventFormComponent } from './event-list/event-form/event-form.component';
import { SingleArticleComponent } from './article-list/single-article/single-article.component';
import { StockComponent } from './stock/stock.component';
import { GalerieComponent } from './galerie/galerie.component';
import { AproposComponent } from './apropos/apropos.component';
import { SingleBlogComponent } from './blog-list/single-blog/single-blog.component';
import { DevisListComponent } from './devis-list/devis-list.component';
import { DevisFormComponent } from './devis-list/devis-form/devis-form.component';
import { DisponibiliteComponent } from './disponibilite/disponibilite.component';
import { AccueilService } from './services/accueil.service';

@NgModule({
  declarations: [
    AppComponent,
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
    DisponibiliteComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    FontAwesomeModule,
    BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot()
  ],
  providers: [
    BsDropdownConfig,
 
    AccueilService

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
