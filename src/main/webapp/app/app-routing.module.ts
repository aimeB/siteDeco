import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SignupComponent } from './new-comp/auth/signup/signup.component';
import { SigninComponent } from './new-comp/auth/signin/signin.component';
import { AcceuilComponent } from './new-comp/acceuil/acceuil.component';
import { FaqComponent } from './new-comp/faq/faq.component';
import { SingleEventComponent } from './new-comp/event-list/single-event/single-event.component';
import { ArticleListComponent } from './new-comp/article-list/article-list.component';
import { ArticleFormComponent } from './new-comp/article-list/article-form/article-form.component';
import { BlogListComponent } from './new-comp/blog-list/blog-list.component';
import { BlogFormComponent } from './new-comp/blog-list/blog-form/blog-form.component';
import { SingleDevisComponent } from './new-comp/devis-list/single-devis/single-devis.component';
import { ContactComponent } from './new-comp/contact/contact.component';
import { ListComponent } from './new-comp/user/list/list.component';
import { MentionLegaleComponent } from './new-comp/mention-legale/mention-legale.component';
import { ConditionsGeneraleComponent } from './new-comp/conditions-generale/conditions-generale.component';
import { EventListComponent } from './new-comp/event-list/event-list.component';
import { EventFormComponent } from './new-comp/event-list/event-form/event-form.component';
import { SingleArticleComponent } from './new-comp/article-list/single-article/single-article.component';
import { StockComponent } from './new-comp/stock/stock.component';
import { GalerieComponent } from './new-comp/galerie/galerie.component';
import { AproposComponent } from './new-comp/apropos/apropos.component';
import { SingleBlogComponent } from './new-comp/blog-list/single-blog/single-blog.component';
import { DevisListComponent } from './new-comp/devis-list/devis-list.component';
import { DevisFormComponent } from './new-comp/devis-list/devis-form/devis-form.component';
import { DisponibiliteComponent } from './new-comp/disponibilite/disponibilite.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
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
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        ...LAYOUT_ROUTES,

      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule { }
