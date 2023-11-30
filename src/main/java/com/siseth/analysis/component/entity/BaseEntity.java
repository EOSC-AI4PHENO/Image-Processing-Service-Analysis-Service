package com.siseth.analysis.component.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    protected Long id;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Column(name = "`createdAt`")
    protected OffsetDateTime createdAt;

    @Column(name = "`modifiedAt`")
    protected OffsetDateTime modifiedAt = OffsetDateTime.now();

    @PrePersist
    void setCreatedAt(){
        if(this.createdAt == null)
            this.setCreatedAt(OffsetDateTime.now());
    }

    @PreUpdate
    void setModifiedAt(){
        this.modifiedAt = OffsetDateTime.now();
    }

}
