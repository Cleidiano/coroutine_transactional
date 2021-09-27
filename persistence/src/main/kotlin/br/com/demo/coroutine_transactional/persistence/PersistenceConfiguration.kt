package br.com.demo.coroutine_transactional.persistence

import io.ebean.DB
import io.ebean.annotation.EbeanComponent
import io.ebeaninternal.api.HelpScopeTrans
import io.ebeaninternal.api.SpiEbeanServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component

@Configuration
@ComponentScan("br.com.demo")
class PersistenceConfiguration {

    @Bean
    fun server(): SpiEbeanServer {
        return DB.getDefault() as SpiEbeanServer
    }
}
