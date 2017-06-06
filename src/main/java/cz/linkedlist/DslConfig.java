package cz.linkedlist;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.impl.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Configuration
public class DslConfig {

    private final DataSource dataSource;
    private final TenantContext context;

    public DslConfig(DataSource dataSource, TenantContext context) {
        this.dataSource = dataSource;
        this.context = context;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        DataSourceTransactionManager transactionManager= new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public DSLContext dsl() {
        return ProxyFactory.getProxy(DSLContext.class, new MethodInterceptor() {

            final Map<String, DSLContext> contextMap = new ConcurrentHashMap<>();

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                String tenant = context.getTenant().getSchema(); // will throw if not authenticated
                DSLContext ctx = contextMap.computeIfAbsent(tenant, (key) ->{
                    Settings settings = new Settings()
                            .withRenderMapping(new RenderMapping()
                                    .withSchemata(new MappedSchema().withInput("master").withOutput(key)));
                    DefaultConfiguration configuration = new DefaultConfiguration();
                    configuration.setSQLDialect(SQLDialect.POSTGRES_9_5);
                    configuration.setSettings(settings);
                    configuration.setConnectionProvider(new DataSourceConnectionProvider(dataSource));
                    configuration.setExecuteListenerProvider(new DefaultExecuteListenerProvider(new DefaultExecuteListener()));
                    return new DefaultDSLContext(configuration);
                });
                return invocation.getMethod().invoke(ctx, invocation.getArguments());
            }
        });
    }
}
