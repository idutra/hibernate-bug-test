package org.hibernate.bugs.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("EXCLUSION")
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class ExclusionEntity extends AbstractCompensatingEntity{
}
