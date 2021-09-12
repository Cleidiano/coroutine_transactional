package br.com.demo.coroutine_transactional.persistence

import br.com.demo.coroutine_transactional.tx.CoroutineJpaTransactionInterceptor
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.interceptor.TransactionAttributeSource
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = ["br.com.demo.coroutine_transactional"])
@ComponentScan("br.com.demo")
class PersistenceConfiguration {

    @Bean
    fun transactionInterceptor(
        transactionAttributeSource: TransactionAttributeSource,
        transactionManager: PlatformTransactionManager
    ): TransactionInterceptor {
        return CoroutineJpaTransactionInterceptor(transactionAttributeSource, transactionManager)
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory
        }
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val jpaProperties = Properties().apply {
            this["hibernate.show_sql"] = false
            this["hibernate.format_sql"] = false
            this["hibernate.hbm2ddl.auto"] = "create"
            this["hibernate.dialect"] = "org.hibernate.dialect.H2Dialect"
            this["hibernate.ejb.naming_strategy"] = "org.hibernate.cfg.ImprovedNamingStrategy"
        }

        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setPackagesToScan("br.com.demo.coroutine_transactional")
            setJpaProperties(jpaProperties)
        }
    }

    @Bean
    fun dataSource(env: Environment): DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;"
            username = "sa"
            maximumPoolSize = 2
            minimumIdle = 15
        }
    )
}
