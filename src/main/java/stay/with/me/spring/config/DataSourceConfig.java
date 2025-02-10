package stay.with.me.spring.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("POSTGRES_URL");
        String dbUsername = System.getenv("POSTGRES_USERNAME");
        String dbPassword = System.getenv("POSTGRES_PASSWORD");

        if (dbUrl == null || dbUsername == null || dbPassword == null) {
            throw new IllegalStateException("DB 환경 변수가 설정되지 않았습니다!");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");

        return dataSource;
    }
}