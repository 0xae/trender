package com.dk.trender;

import java.security.Principal;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.api.AuthApi;
import com.dk.trender.api.TestApi;
import com.dk.trender.api.ZChannelApi;
import com.dk.trender.api.ZCollectionApi;
import com.dk.trender.api.ZPostApi;
import com.dk.trender.auth.JwtAuthFilter;
import com.dk.trender.auth.JwtService;
import com.dk.trender.auth.TrenderAuthenticator;
import com.dk.trender.auth.TrenderAuthorizer;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZTimeline;
import com.dk.trender.core.ZUser;
import com.dk.trender.core.managed.ManagedSolr;
import com.dk.trender.exceptions.BadRequestExceptionMapper;
import com.dk.trender.exceptions.ConnectExceptionMapper;
import com.dk.trender.exceptions.ConstraintViolationExceptionMapper;
import com.dk.trender.exceptions.NoResultExceptionExceptionMapper;
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;
import com.dk.trender.service.ZMediaService;
import com.dk.trender.service.ZPostService;
import com.dk.trender.service.ZUserService;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
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
    		new HibernateBundle<TrenderConfiguration>(ZChannel.class,
    												  ZCollection.class, 
    												  ZUser.class) {
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
		JwtService jwt = configAuth(conf, env);
		ManagedSolr solr = new ManagedSolr();
		ZPostService $post = new ZPostService(solr.getClient());
		ZMediaService $media = new ZMediaService(conf.getMediaHost());
		ZChannelService $channel = new ZChannelService(session, solr.getClient());
		ZCollectionService $col = new ZCollectionService(session);
		ZUserService $user = new ZUserService(session, jwt, conf.getAuthorizationPrefix());

		// api resources
		// env.jersey().register(new TestApi());
		env.jersey().register(new AuthApi($user));
		env.jersey().register(new ZCollectionApi($col));
		env.jersey().register(new ZPostApi($post, $media));
		env.jersey().register(new ZChannelApi($channel));

		// exception handlers
		env.jersey().register(new NoResultExceptionExceptionMapper(env.metrics()));
		env.jersey().register(new ConstraintViolationExceptionMapper());
		env.jersey().register(new ConnectExceptionMapper());
		env.jersey().register(new BadRequestExceptionMapper());

		// managed objects
		env.lifecycle().manage(solr);
	}

	private JwtService configAuth(TrenderConfiguration conf, Environment env) throws Exception {
		final byte[] key = conf.getJwtSecretToken().getBytes("UTF-8");
        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance

        env.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        env.jersey().register(RolesAllowedDynamicFeature.class);
        env.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<ZUser>()
                    .setJwtConsumer(consumer)
                    .setRealm("realm")
                    .setPrefix(conf.getAuthorizationPrefix())
                    .setAuthenticator(new TrenderAuthenticator())
                    .setAuthorizer(new TrenderAuthorizer())
                    .buildAuthFilter()));

        return new JwtService(
			conf.getJwtSecretToken().getBytes("UTF-8"), 
			conf.getAuthorizationPrefix()
		);
	}

	public static void main(String[] args) throws Exception {
		new TrenderApplication().run(args);
	}
}
