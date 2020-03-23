package com.ges.check.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.mapper.MapperScannerConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


@Configuration
@MapperScan(value="com.ges.check.dao")
public class MybatisConfig {
	
	// private static Logger log = (Logger) LoggerFactory.getLogger(MybatisConfig.class);
	
	@Bean
	ConfigurationCustomizer mybatisConfiguration() {
		return new ConfigurationCustomizer() {
			
			@Override
			public void customize(org.apache.ibatis.session.Configuration configuration) {
				
			}
		};
	}
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer () {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.green.cms.dao");
		return msc;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory=new SqlSessionFactoryBean();
		  sessionFactory.setDataSource(dataSource);
		  
		  Resource[] res=new PathMatchingResourcePatternResolver()
		       .getResources("classpath:/mappers/*_mapper.xml");
		  //sessionFactory.setEnvironment("dev");
		  sessionFactory.setMapperLocations(res);
		  sessionFactory.setTypeAliasesPackage("com.ges.check.config");
		  sessionFactory.setTypeHandlers(new TypeHandler[] {
		    new DateTypeHandler(),
		    new BooleanTypeHandler()
		  });
		  return sessionFactory.getObject();
	}
	
    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
	

}
