package br.com.demo.coroutine_transactional.persistence

import br.com.demo.coroutine_transactional.domain.book.model.Book
import io.ebean.Model
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
class BookEntity : Model() {

    @Id
    lateinit var id: String

    lateinit var isbn: String

    lateinit var title: String

    lateinit var published: LocalDate

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "author_id")
    lateinit var author: AuthorEntity


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
