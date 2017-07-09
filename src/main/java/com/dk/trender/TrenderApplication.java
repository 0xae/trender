package com.dk.trender;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.SessionFactory;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostMedia;
import com.dk.trender.core.Profile;
import com.dk.trender.exceptions.ConstraintViolationExceptionMapper;
import com.dk.trender.exceptions.NoResultExceptionExceptionMapper;
import com.dk.trender.resources.ListingResource;
import com.dk.trender.resources.PostResource;
import com.dk.trender.resources.ProfileResource;
import com.dk.trender.resources.ApiResource;
import com.dk.trender.service.IndexService;
import com.dk.trender.service.ListingService;
import com.dk.trender.service.PostMediaService;
import com.dk.trender.service.PostService;
import com.dk.trender.service.ProfileService;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TrenderApplication extends Application<TrenderConfiguration> {
    private final HibernateBundle<TrenderConfiguration> hibernateBundle =
    		new HibernateBundle<TrenderConfiguration>(Listing.class, Post.class, Profile.class, PostMedia.class) {
			public DataSourceFactory getDataSourceFactory(TrenderConfiguration configuration) {
				return configuration.getDatabase();
			}
        };

	@Override
	public void initialize(Bootstrap<TrenderConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
        		bootstrap.getConfigurationSourceProvider(),
        		new EnvironmentVariableSubstitutor(false)
            )
	    );

		bootstrap.addBundle(new MigrationsBundle<TrenderConfiguration>() {
			public DataSourceFactory getDataSourceFactory(TrenderConfiguration configuration) {
	            return configuration.getDatabase();
			}
	    });

		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(TrenderConfiguration conf, Environment env) throws Exception {
		final FilterRegistration.Dynamic cors = 
			env.servlets().addFilter("CORS", CrossOriginFilter.class);
		cors.setInitParameter("allowedOrigins", "*, https://steemit.com");
		cors.setInitParameter("allowedHeaders", "*");
		cors.setInitParameter("allowedMethods", "OPTIONS, GET, PUT, POST, DELETE, HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//		cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

		SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
		ProfileService profileService = new ProfileService(sessionFactory);
		ListingService listingService = new ListingService(sessionFactory);
		PostMediaService postMediaService = new PostMediaService(sessionFactory);
		PostService postService = new PostService(sessionFactory, profileService, postMediaService);

		env.jersey().register(new ApiResource(postService, new IndexService()));
		env.jersey().register(new ListingResource(listingService));
		env.jersey().register(new ProfileResource(profileService));
		env.jersey().register(new PostResource(postService));

		env.jersey().register(new NoResultExceptionExceptionMapper(env.metrics()));
		env.jersey().register(new ConstraintViolationExceptionMapper());
	}

    public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);
	}
}
