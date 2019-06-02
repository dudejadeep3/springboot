package com.boot;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.boot.model.Shipwreck;
import com.boot.repository.ShipwreckRepository;

/**
 * 
 * @author DDudeja
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ShipwreckRepositoryIntegrationTest {
	
	@Autowired
	private ShipwreckRepository shipwreckRepository;

	@Test
	public void testAll() {
		List<Shipwreck> wrecks = shipwreckRepository.findAll();
		MatcherAssert.assertThat(wrecks.size(), Matchers.is(Matchers.greaterThanOrEqualTo(0)));
	}
	
}
