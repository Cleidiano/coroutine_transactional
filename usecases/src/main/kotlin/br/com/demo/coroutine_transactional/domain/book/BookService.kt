package br.com.demo.coroutine_transactional.domain.book

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.model.Book
import br.com.demo.coroutine_transactional.domain.book.model.BookDefinition
import br.com.demo.coroutine_transactional.domain.book.repository.AuthorRepository
import br.com.demo.coroutine_transactional.domain.book.repository.BookRepository
import br.com.demo.coroutine_transactional.tx.TransactionalOperator
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val transactional: TransactionalOperator
) {

    suspend fun publish(bookDefinition: BookDefinition, shouldThrows: Boolean): Book =
        transactional.execute {
            val author = async {
                authorRepository.getOrCreate(
                    author = Author(
                        id = bookDefinition.author.id,
                        name = bookDefinition.author.name
                    )
                )
            }


            val book = async {
                bookRepository.save(
                    Book(
                        id = bookDefinition.id,
                        isbn = bookDefinition.isbn,
                        author = author.await(),
                        title = bookDefinition.title,
                        published = LocalDate.now()
                    )
                )
            }
            book.await().also {
                if (shouldThrows) throw RuntimeException("Just to rollback")
            }
        }
}
