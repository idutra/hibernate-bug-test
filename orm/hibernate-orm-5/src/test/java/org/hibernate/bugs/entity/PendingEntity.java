package org.hibernate.bugs.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("PENDING_ENTITY")
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class PendingEntity extends AbstractPendingEntity{
}
