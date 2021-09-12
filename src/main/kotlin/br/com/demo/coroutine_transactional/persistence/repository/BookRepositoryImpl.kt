package br.com.demo.coroutine_transactional.persistence.repository

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.model.Book
import br.com.demo.coroutine_transactional.domain.book.repository.BookRepository
import br.com.demo.coroutine_transactional.persistence.AuthorEntity
import br.com.demo.coroutine_transactional.persistence.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class BookRepositoryImpl(
    private val repo: JPAAuthorRepository,
) : BookRepository {

    override suspend fun save(book: Book): Book = withContext(Dispatchers.IO) {
        val entity = BookEntity(
            id = book.id,
            isbn = book.isbn,
            author = book.author.toEntity(),
            title = book.title,
            published = book.published
        ).also { it.author.books.add(it) }
        repo.save(entity.author)
        book
    }
}

@Component
interface JPABookRepository : JpaRepository<BookEntity, String>

fun Author.toEntity(): AuthorEntity {
    return AuthorEntity().apply {
        id = this@toEntity.id
        name = this@toEntity.name
    }
}
