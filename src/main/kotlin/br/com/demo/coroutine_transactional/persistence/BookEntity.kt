package br.com.demo.coroutine_transactional.persistence

import br.com.demo.coroutine_transactional.domain.book.model.Book
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "books")
data class BookEntity(

    @Id
    val id: String,

    var isbn: String,

    var title: String,

    var published: LocalDate,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "author_id")
    var author: AuthorEntity,
) {
    fun toModel(): Book {
        return Book(
            id = this.id,
            isbn = this.isbn,
            published = this.published,
            author = this.author.toModel(),
            title = this.title
        )
    }
}
