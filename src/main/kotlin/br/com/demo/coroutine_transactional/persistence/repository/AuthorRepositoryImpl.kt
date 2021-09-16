package br.com.demo.coroutine_transactional.persistence.repository

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.repository.AuthorRepository
import br.com.demo.coroutine_transactional.tx.inTransaction
import org.springframework.stereotype.Component

@Component
class AuthorRepositoryImpl : AuthorRepository {

    override suspend fun getOrCreate(author: Author): Author {
        author.toEntity().inTransaction { transaction -> save(transaction) }
        return author
    }
}
