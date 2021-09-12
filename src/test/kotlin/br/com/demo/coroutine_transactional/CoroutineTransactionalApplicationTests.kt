package br.com.demo.coroutine_transactional

import br.com.demo.coroutine_transactional.domain.book.BookService
import br.com.demo.coroutine_transactional.domain.book.model.AuthorDefinition
import br.com.demo.coroutine_transactional.domain.book.model.BookDefinition
import br.com.demo.coroutine_transactional.persistence.PersistenceConfiguration
import br.com.demo.coroutine_transactional.persistence.repository.JPAAuthorRepository
import br.com.demo.coroutine_transactional.persistence.repository.JPABookRepository
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import io.kotest.property.checkAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [PersistenceConfiguration::class])
class CoroutineTransactionalApplicationTests : StringSpec() {

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var jpaAuthorRepository: JPAAuthorRepository

    @Autowired
    lateinit var jpaBookRepository: JPABookRepository

    override fun extensions() = listOf(SpringExtension)

    init {

        "validate @Transactional commit/rollback behaviour when using coroutine" {
            val bookGenerator = arbitrary { rs ->
                BookDefinition(
                    id = Arb.uuid().next(rs).toString(),
                    isbn = Arb.string().next(rs),
                    title = Arb.string().next(rs),
                    author = AuthorDefinition(
                        id = Arb.uuid().next(rs).toString(),
                        name = Arb.uuid().next(rs).toString(),
                    )
                )
            }
            checkAll(bookGenerator, Arb.bool()) { book, shouldThrows ->
                if (shouldThrows) {
                    shouldThrowAny { bookService.publish(book, shouldThrows) }

                    jpaAuthorRepository.existsById(book.author.id).shouldBeFalse()
                    jpaBookRepository.existsById(book.id).shouldBeFalse()
                } else {
                    bookService.publish(book, shouldThrows)

                    jpaAuthorRepository.existsById(book.author.id).shouldBeTrue()
                    jpaBookRepository.existsById(book.id).shouldBeTrue()
                }
            }
        }
    }
}
