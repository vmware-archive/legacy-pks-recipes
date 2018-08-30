package com.pivotalservices.sample.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@NamedQueries({
        @NamedQuery(name = "comment.list", query = "select c from Comment c"),
        @NamedQuery(name = "comment.for.post", query = "select c from Comment c where c.post.id=:postId")
})
@XmlRootElement(name = "comment")
public class Comment extends Model {

    @NotNull
    @Size(min = 1)
    private String author;
    @NotNull
    @Size(min = 1)
    @Lob
    private String content;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @Valid
    @XmlTransient
    private Post post;

    public void setAuthor(final String author) {
        this.author = author;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Post getPost() {
        return post;
    }
}
