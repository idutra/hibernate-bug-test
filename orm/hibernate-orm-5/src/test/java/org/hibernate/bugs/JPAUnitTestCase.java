package org.hibernate.bugs;

import org.hibernate.bugs.entity.ExclusionEntity;
import org.hibernate.bugs.entity.PendingEntity;
import org.hibernate.bugs.entity.PostedEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	private final UUID pendingId = UUID.fromString("1c22bdda-631a-4586-80c3-ed92861d3ee4");
	private final UUID postedId = UUID.fromString("f243f815-e55a-4ede-bcd8-513a55991064");
	private final UUID exclusionId = UUID.fromString("61137189-40f8-478b-b2aa-5f63b91f3506");

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		initialInserts();

		ExclusionEntity excluded = entityManager.find(ExclusionEntity.class, exclusionId.toString());
		assertEquals(excluded.getId(), exclusionId.toString());
		assertThat(excluded.getCompensatingEntity()).isNotNull();

		PendingEntity compensated = (PendingEntity) excluded.getCompensatingEntity();
		assertEquals(compensated.getId(), pendingId.toString());
		assertThat(compensated.getFulfilledBy()).isNotNull();

		PostedEntity fulfilled = (PostedEntity) compensated.getFulfilledBy();
		assertEquals(fulfilled.getId(), postedId.toString());


		entityManager.close();
	}


	private void initialInserts() {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		PendingEntity pe = new PendingEntity();
		pe.setId(pendingId.toString());
		em.persist(pe);
		PostedEntity ppe = new PostedEntity();
		ppe.setId(postedId.toString());
		ppe.setFulfilledEntity(pe);
		em.persist(ppe);
		ExclusionEntity ex = new ExclusionEntity();
		ex.setId(exclusionId.toString());
		ex.setCompensatingEntity(pe);
		em.persist(ex);

		em.getTransaction().commit();
		em.close();
	}
}
