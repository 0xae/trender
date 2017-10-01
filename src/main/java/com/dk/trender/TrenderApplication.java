package com.dk.trender;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.SessionFactory;

import com.dk.trender.core.Timeline;
import com.dk.trender.core.managed.ManagedSolr;
import com.dk.trender.exceptions.ConstraintViolationExceptionMapper;
import com.dk.trender.exceptions.NoResultExceptionExceptionMapper;
import com.dk.trender.resources.ApiResource;
import com.dk.trender.service.PostService;
import com.dk.trender.service.TimelineService;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author ayrton
 * @date 2017-07-29 07:04:53
 */
public class TrenderApplication extends Application<TrenderConfiguration> {
    final HibernateBundle<TrenderConfiguration> hibernateBundle = new HibernateBundle<TrenderConfiguration>(Timeline.class) {
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
		final FilterRegistration.Dynamic cors = env.servlets()
				.addFilter("CORS", CrossOriginFilter.class);

		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "*");
		cors.setInitParameter("allowedMethods", "OPTIONS, GET, PUT, POST, DELETE, HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

		SessionFactory session = hibernateBundle.getSessionFactory();
		ManagedSolr solr = new ManagedSolr();

		PostService post = new PostService(solr.getSolr());
		TimelineService timeline = new TimelineService(session, post);			

		env.jersey().register(new ApiResource(post, timeline));
		env.jersey().register(new NoResultExceptionExceptionMapper(env.metrics()));
		env.jersey().register(new ConstraintViolationExceptionMapper());
		env.lifecycle().manage(solr);
	}

	public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);    		
	}
}
