package br.com.demo.coroutine_transactional.persistence.repository

import br.com.demo.coroutine_transactional.persistence.AuthorEntity
import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.repository.AuthorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
class AuthorRepositoryImpl(val jpaAuthorRepository: JPAAuthorRepository) : AuthorRepository {

    override suspend fun getOrCreate(author: Author): Author = withContext(Dispatchers.IO) {
        jpaAuthorRepository.save(author.toEntity()).toModel()
    }
}

@Component
interface JPAAuthorRepository : JpaRepository<AuthorEntity, String>
