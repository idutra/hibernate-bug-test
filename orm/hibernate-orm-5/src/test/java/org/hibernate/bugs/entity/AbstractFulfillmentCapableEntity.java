package org.hibernate.bugs.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@SecondaryTable(name = "ENTITY_FULFILLMENT", pkJoinColumns = @PrimaryKeyJoinColumn(name = "FULFILLED_ENTITY_ID"))
public abstract class AbstractFulfillmentCapableEntity extends AbstractEntity {

    @OneToOne(fetch = FetchType.EAGER, targetEntity = AbstractPendingEntity.class, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "PENDING_ENTITY_ID", table = "ENTITY_FULFILLMENT")
    @ToString.Exclude
    private AbstractEntity fulfilledEntity;

    public void fulfilledEntity(AbstractPendingEntity resolvedEntity){
        AbstractPendingEntity resolvedPendingEntry = resolvedEntity;
        resolvedPendingEntry.setFulfilledBy(this);
        this.fulfilledEntity = resolvedEntity;
    }

}
