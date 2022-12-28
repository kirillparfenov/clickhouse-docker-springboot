package common.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "test.mappers")
public class ClickHouseConfig {
    public static final String CLICKHOUSE_DB_BEAN = "CLICKHOUSE_DB_BEAN";
    public static final String CLICKHOUSE_SESSION_FACTORY_BEAN = "CLICKHOUSE_SESSION_FACTORY_BEAN";
    public static final String CLICKHOUSE_TX_MANAGER_BEAN = "CLICKHOUSE_TX_MANAGER_BEAN";

    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.hikari.minimumIdle}")
    private Integer minimumIdle;
    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${spring.datasource.hikari.idleTimeout}")
    private Integer idleTimeout;
    @Value("${spring.datasource.hikari.connectionTimeout}")
    private Integer connectionTimeout;
    @Value("${spring.datasource.hikari.leakDetectionThreshold}")
    private Integer leakDetectionThreshold;
    @Value("${spring.datasource.hikari.maxLifetime}")
    private Long maxLifeTime;
    @Value("${spring.datasource.hikari.poolName}")
    private String poolName;

    @Bean(CLICKHOUSE_DB_BEAN)
    public DataSource getDataSource() {
        Properties properties = new Properties();
        properties.setProperty( "socket_timeout" , "3600000");
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setLeakDetectionThreshold(leakDetectionThreshold);
        hikariConfig.setPoolName(poolName);
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setRegisterMbeans(false);
        hikariConfig.setDataSourceProperties(properties);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(CLICKHOUSE_TX_MANAGER_BEAN)
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean(CLICKHOUSE_SESSION_FACTORY_BEAN)
    public SqlSessionFactory sqlSessionFactory(@Qualifier(CLICKHOUSE_DB_BEAN) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis/clickhouse.xml"));
        sessionFactory.setTransactionFactory(new SpringManagedTransactionFactory());
        return sessionFactory.getObject();
    }
}
