package br.com.demo.coroutine_transactional.domain.book.model

import java.time.LocalDate

class Book(
    val id: String,
    val isbn: String,
    val title: String,
    val published: LocalDate,
    val author: Author,
)
