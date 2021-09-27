package br.com.demo.coroutine_transactional.persistence.repository

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.model.Book
import br.com.demo.coroutine_transactional.domain.book.repository.BookRepository
import br.com.demo.coroutine_transactional.persistence.AuthorEntity
import br.com.demo.coroutine_transactional.persistence.BookEntity
import br.com.demo.coroutine_transactional.persistence.query.QAuthorEntity
import org.springframework.stereotype.Repository

@Repository
class BookRepositoryImpl(
) : BookRepository {

    override suspend fun save(book: Book): Book {
        BookEntity().apply {
            id = book.id
            isbn = book.isbn
            author = QAuthorEntity().id.eq(book.author.id).findOne() ?: book.author.toEntity()
            title = book.title
            published = book.published

            this.save()
        }

        return book
    }
}

fun Author.toEntity(): AuthorEntity {
    return AuthorEntity().apply {
        id = this@toEntity.id
        name = this@toEntity.name
    }
}
