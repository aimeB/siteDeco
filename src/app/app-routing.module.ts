import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './auth/signup/signup.component';
import { SigninComponent } from './auth/signin/signin.component';
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



const routes: Routes = [

  { path: '', component: AcceuilComponent },
  { path: 'acceuil', component: AcceuilComponent },
  
  { path: 'faq', component: FaqComponent },
  { path: 'mentionlegale', component: MentionLegaleComponent },
  { path: 'conditionsgenerale', component: ConditionsGeneraleComponent },

  { path: 'signup', component: SignupComponent },
  { path: 'signin', component: SigninComponent },

  { path: 'event-list', component: EventListComponent },
  { path: 'single-event', component: SingleEventComponent },
  { path: 'event-form', component: EventFormComponent },


  { path: 'article-list', component: ArticleListComponent },
  { path: 'single-article', component: SingleArticleComponent },
  { path: 'article-form', component: ArticleFormComponent },

  { path: 'blog-list', component: BlogListComponent },
  { path: 'single-blog', component: SingleBlogComponent },
  { path: 'blog-form', component: BlogFormComponent },
  
  
  { path: 'devis-list', component: DevisListComponent },
  { path: 'single-devis', component: SingleDevisComponent },
  { path: 'devis-form', component: DevisFormComponent },

  { path: 'list', component: ListComponent },
  { path: 'galerie', component: GalerieComponent },
  { path: 'apropos', component: AproposComponent },
  { path: 'contact', component: ContactComponent },

  { path: 'stock', component: StockComponent },
  { path: 'disponibilite', component: DisponibiliteComponent },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
