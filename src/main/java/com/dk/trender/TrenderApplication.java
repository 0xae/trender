package com.dk.trender;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.SessionFactory;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.exceptions.NoResultExceptionExceptionMapper;
import com.dk.trender.resources.ListingResource;
import com.dk.trender.resources.PostResource;
import com.dk.trender.service.ListingService;
import com.dk.trender.service.PostService;

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
            new HibernateBundle<TrenderConfiguration>(Listing.class,
            		                            Post.class) {
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
	            ));

		bootstrap.addBundle(new MigrationsBundle<TrenderConfiguration>() {
			public DataSourceFactory getDataSourceFactory(TrenderConfiguration configuration) {
	            return configuration.getDatabase();
			}
	    });

		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(TrenderConfiguration conf, Environment env) throws Exception {
		final FilterRegistration.Dynamic cors = env.servlets()
				.addFilter("CORS", CrossOriginFilter.class);
		// so much have been said and yet so little has been done
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "*");
		// cors.setInitParameter("allowedHeaders", "X-Requested-With, Content-Type, Accept, Origin, Authorization");
		cors.setInitParameter("allowedMethods", "OPTIONS, GET, PUT, POST, DELETE, HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

		SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
		PostService postService = new PostService(sessionFactory);
		ListingService listingService = new ListingService(sessionFactory, postService);
		
		env.jersey().register(new ListingResource(listingService));
		env.jersey().register(new PostResource(postService));
		env.jersey().register(new NoResultExceptionExceptionMapper(env.metrics()));
	}

    public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);
	}
}
