package com.dk.trender;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZTimeline;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.managed.ManagedSolr;
import com.dk.trender.exceptions.ConnectExceptionMapper;
import com.dk.trender.exceptions.ConstraintViolationExceptionMapper;
import com.dk.trender.exceptions.NoResultExceptionExceptionMapper;
import com.dk.trender.resources.ApiResource;
import com.dk.trender.service.ZMediaService;
import com.dk.trender.service.ZPostService;
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;
import com.dk.trender.service.ZTimelineService;

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
	private static final Logger log = LoggerFactory.getLogger(TrenderApplication.class);

	final HibernateBundle<TrenderConfiguration> hibernateBundle = 
    		new HibernateBundle<TrenderConfiguration>(ZTimeline.class, ZChannel.class,
    												  ZCollection.class) {
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

		ZPostService post = new ZPostService(solr.getClient());
		ZMediaService media = new ZMediaService(conf.getMediaHost());
		ZChannelService $channel = new ZChannelService(session);
		ZCollectionService $col = new ZCollectionService(session);

		env.jersey().register(new ApiResource(post, media, $channel, $col));
		env.jersey().register(new NoResultExceptionExceptionMapper(env.metrics()));
		env.jersey().register(new ConstraintViolationExceptionMapper());
		env.jersey().register(new ConnectExceptionMapper());		
		env.lifecycle().manage(solr);
		
		log.info("authorizationPrefix is {}", conf.getAuthorizationPrefix());
	}

	public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);    		
	}
}
