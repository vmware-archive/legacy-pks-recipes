package com.pivotalservices.sample.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
        @NamedQuery(name = "post.list", query = "select p from Post p")
})
@XmlRootElement(name = "post")
public class Post extends DatedModel {

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    @Size(min = 1)
    @Lob
    private String content;

    @ManyToOne
    @Valid
    private User user;


    public void setTitle(final String title) {
        this.title = title;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

}
