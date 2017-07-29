package com.dk.trender;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.dk.trender.resources.ApiResource;
import com.dk.trender.service.PostService;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author ayrton
 * @date 2017-07-29 07:04:53
 */
public class TrenderApplication extends Application<TrenderConfiguration> {
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
	}

	@Override
	public void run(TrenderConfiguration conf, Environment env) throws Exception {
		final FilterRegistration.Dynamic cors = 
			env.servlets().addFilter("CORS", CrossOriginFilter.class);
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "*");
		cors.setInitParameter("allowedMethods", "OPTIONS, GET, PUT, POST, DELETE, HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

		final PostService postService = new PostService();
		env.jersey().register(new ApiResource(postService));
	}

    public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);
	}
}
