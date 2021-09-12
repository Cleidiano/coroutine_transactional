package br.com.demo.coroutine_transactional.domain.book.model

data class BookDefinition(
    val id: String,
    val isbn: String,
    val title: String,
    val author: AuthorDefinition,
)

data class AuthorDefinition(
    val id: String,
    val name: String
)
