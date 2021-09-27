import br.com.demo.coroutine_transactional.domain.book.BookService
import br.com.demo.coroutine_transactional.domain.book.model.AuthorDefinition
import br.com.demo.coroutine_transactional.domain.book.model.BookDefinition
import br.com.demo.coroutine_transactional.persistence.PersistenceConfiguration
import br.com.demo.coroutine_transactional.persistence.query.QAuthorEntity
import br.com.demo.coroutine_transactional.persistence.query.QBookEntity
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
import io.kotest.property.checkAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@ContextConfiguration(classes = [PersistenceConfiguration::class])
class CoroutineTransactionalApplicationTests : StringSpec() {

    @Autowired
    lateinit var bookService: BookService

    override fun extensions() = listOf(SpringExtension)

    init {

        "validate transactional behaviour when using coroutine" {
            val bookGenerator = arbitrary { rs ->
                BookDefinition(
                    id = UUID.randomUUID().toString(),
                    isbn = Arb.string().next(rs),
                    title = Arb.string().next(rs),
                    author = AuthorDefinition(
                        id = UUID.randomUUID().toString(),
                        name = Arb.string(minSize = 1).next(rs)
                    )
                )
            }

            checkAll(bookGenerator, Arb.bool()) { book, shouldThrows ->
                if (shouldThrows) {
                    shouldThrowAny { bookService.publish(book, shouldThrows) }

                    QAuthorEntity()
                        .id
                        .eq(book.author.id)
                        .exists()
                        .shouldBeFalse()

                    QBookEntity().id.eq(book.id).exists().shouldBeFalse()
                } else {
                    bookService.publish(book, shouldThrows)

                    QAuthorEntity()
                        .id
                        .eq(book.author.id)
                        .exists()
                        .shouldBeTrue()
                    QBookEntity().id.eq(book.id).exists().shouldBeTrue()

                }
            }
        }
    }
}
