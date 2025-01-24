package hello.fclover.config;

import static org.apache.ibatis.type.JdbcType.NULL;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@RequiredArgsConstructor
@MapperScan(
        basePackages = "hello.fclover.mybatis.mapper",
        sqlSessionTemplateRef = "simpleSqlSessionTemplate" // 기본적으로 사용되는 템플릿
)
public class MybatisConfig {

    private final DataSource dataSource;

    // SIMPLE 모드 ExecutorType
    @Bean
    public SqlSessionFactory simpleSqlSessionFactory() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(NULL);

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(configuration);

        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/mapper/*.xml")
        );

        factoryBean.setTypeAliasesPackage(("hello.fclover.domain"));

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate simpleSqlSessionTemplate(SqlSessionFactory simpleSqlSessionFactory) {
        return new SqlSessionTemplate(simpleSqlSessionFactory, ExecutorType.SIMPLE);
    }


    // BATCH 모드 ExecutorType
    @Bean
    public SqlSessionFactory batchSqlSessionFactory() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setDefaultExecutorType(ExecutorType.BATCH);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(NULL);

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(configuration);

        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/mapper/*.xml")
        );

        factoryBean.setTypeAliasesPackage(("hello.fclover.domain"));

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate batchSqlSessionTemplate(SqlSessionFactory batchSqlSessionFactory) {
        return new SqlSessionTemplate(batchSqlSessionFactory, ExecutorType.BATCH);
    }
}
