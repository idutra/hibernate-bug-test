package org.hibernate.bugs.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class AbstractPendingEntity extends AbstractEntity{

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "fulfilledEntity", targetEntity = AbstractFulfillmentCapableEntity.class, optional = true)
    @ToString.Exclude
    private AbstractFulfillmentCapableEntity fulfilledBy;


}
