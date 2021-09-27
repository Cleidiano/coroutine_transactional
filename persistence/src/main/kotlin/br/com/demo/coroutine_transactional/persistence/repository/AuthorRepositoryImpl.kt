package br.com.demo.coroutine_transactional.persistence.repository

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.repository.AuthorRepository
import org.springframework.stereotype.Component

@Component
class AuthorRepositoryImpl : AuthorRepository {

    override suspend fun getOrCreate(author: Author): Author {
        author.toEntity().save()
        return author
    }
}
