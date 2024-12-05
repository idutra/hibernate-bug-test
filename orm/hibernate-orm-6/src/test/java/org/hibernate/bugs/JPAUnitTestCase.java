package org.hibernate.bugs;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.DynamicUpdate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

    private final UUID pendingId = UUID.fromString("1c22bdda-631a-4586-80c3-ed92861d3ee4");
    private final UUID postedId = UUID.fromString("f243f815-e55a-4ede-bcd8-513a55991064");
    private final UUID exclusionId = UUID.fromString("61137189-40f8-478b-b2aa-5f63b91f3506");

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @AfterEach
    void destroy() {
        entityManagerFactory.close();
    }


    @Test
    void pendingWithPostedExclusion() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        initialInserts();

        ExclusionEntity excluded = entityManager.find(ExclusionEntity.class, exclusionId);
        assertEquals(excluded.getId(), exclusionId);
        assertThat(excluded.getCompensatingEntity()).isNotNull();

        PendingEntity compensated = (PendingEntity) excluded.getCompensatingEntity();
        assertEquals(compensated.getId(), pendingId);
        assertThat(compensated.getFulfilledBy()).isNotNull();

        PostedEntity fulfilled = (PostedEntity) compensated.getFulfilledBy();
        assertEquals(fulfilled.getId(), postedId);

        entityManager.close();
    }


    private void initialInserts() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        PendingEntity pe = new PendingEntity();
        pe.setId(pendingId);
        em.persist(pe);
        PostedEntity ppe = new PostedEntity();
        ppe.setId(postedId);
        ppe.setFulfilledEntity(pe);
        em.persist(ppe);
        ExclusionEntity ex = new ExclusionEntity();
        ex.setId(exclusionId);
        ex.setCompensatingEntity(pe);
        em.persist(ex);

        em.getTransaction().commit();
        em.close();
    }


    @Entity
    @Table(name = "ENTITY")
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
    public static abstract class AbstractEntity implements Serializable {

        @Id
        @Column(name = "ID")
        private UUID id = UUID.randomUUID();

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }



    }

    @Entity
    @DiscriminatorValue("PENDING_ENTITY")
    @DynamicUpdate
    public static class PendingEntity extends AbstractEntity {
        public PendingEntity() {
        }

        @OneToOne(fetch = FetchType.EAGER, mappedBy = "fulfilledEntity", targetEntity = PostedEntity.class, optional = true)
        private PostedEntity fulfilledBy;

        public PostedEntity getFulfilledBy() {
            return fulfilledBy;
        }

        public void setFulfilledBy(PostedEntity fulfilledBy) {
            this.fulfilledBy = fulfilledBy;
        }
    }

    @Entity
    @DiscriminatorValue("EXCLUSION")
    @DynamicUpdate
    @SecondaryTable(name = "COMPENSATING_ENTITY", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ENTITY_ID"))
    public static class ExclusionEntity extends AbstractEntity {

        public ExclusionEntity() {

        }

        public ExclusionEntity(AbstractEntity compensatingEntity) {
            this.compensatingEntity = compensatingEntity;
        }

        @ManyToOne(fetch = FetchType.EAGER, targetEntity = AbstractEntity.class)
        @JoinColumn(name = "COMPENSATING_ENTITY_ID", table = "COMPENSATING_ENTITY")
        private AbstractEntity compensatingEntity;


        public AbstractEntity getCompensatingEntity() {
            return compensatingEntity;
        }

        public void setCompensatingEntity(AbstractEntity compensatingEntity) {
            this.compensatingEntity = compensatingEntity;
        }
    }

    @Entity
    @DiscriminatorValue("ENTITY")
    @DynamicUpdate
    @SecondaryTable(name = "ENTITY_FULFILLMENT", pkJoinColumns = @PrimaryKeyJoinColumn(name = "FULFILLED_ENTITY_ID"))
    public static class PostedEntity extends AbstractEntity {
        public PostedEntity() {

        }

        @OneToOne(fetch = FetchType.EAGER, targetEntity = PendingEntity.class, optional = true, cascade = CascadeType.ALL)
        @JoinColumn(name = "PENDING_ENTITY_ID", table = "ENTITY_FULFILLMENT")
        private AbstractEntity fulfilledEntity;

        public AbstractEntity getFulfilledEntity() {
            return fulfilledEntity;
        }

        public void setFulfilledEntity(AbstractEntity fulfilledEntity) {
            this.fulfilledEntity = fulfilledEntity;
        }
    }
}
