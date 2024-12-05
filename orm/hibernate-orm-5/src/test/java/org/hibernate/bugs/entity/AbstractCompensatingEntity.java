package org.hibernate.bugs.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
@SecondaryTable(name = "COMPENSATING_ENTITY", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ENTITY_ID"))
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class AbstractCompensatingEntity extends AbstractEntity implements Serializable {


    @ManyToOne(fetch = FetchType.EAGER, targetEntity = AbstractEntity.class)
    @JoinColumn(name = "COMPENSATING_ENTITY_ID", table = "COMPENSATING_ENTITY")
    private AbstractEntity compensatingEntity;

}
