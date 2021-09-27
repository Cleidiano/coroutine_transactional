package br.com.demo.coroutine_transactional.domain.book.repository

import br.com.demo.coroutine_transactional.domain.book.model.Author
import br.com.demo.coroutine_transactional.domain.book.model.Book

interface BookRepository {
    suspend fun save(book: Book): Book
}

interface AuthorRepository {
    suspend fun getOrCreate(author: Author): Author
}
