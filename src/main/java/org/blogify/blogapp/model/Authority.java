package org.blogify.blogapp.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "authorities")
@SequenceGenerator(name = "authority_seq_gen", sequenceName = "authority_seq", initialValue = 10, allocationSize = 1)
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "authority", unique = true, nullable = false)
    private String authority;

    @ManyToMany(mappedBy = "authorities", cascade = CascadeType.ALL)
    private Collection<BlogUser> users;

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
