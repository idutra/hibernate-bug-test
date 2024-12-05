package org.hibernate.bugs.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ENTITY")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@SuperBuilder(toBuilder = true)
public abstract class AbstractEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @Builder.Default
    private String id = UUID.randomUUID().toString();

}
