package br.com.demo.coroutine_transactional.persistence

import br.com.demo.coroutine_transactional.domain.book.model.Author
import io.ebean.Model
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "authors")
class AuthorEntity : Model() {
    @Id
    var id: String? = null

    lateinit var name: String

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    var books: MutableList<BookEntity> = mutableListOf()

    fun toModel(): Author {
        return Author(id = this.id!!, name = this.name)
    }
}
