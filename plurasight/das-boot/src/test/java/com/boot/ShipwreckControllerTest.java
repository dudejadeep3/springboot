package com.boot;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.boot.controller.ShipwreckController;
import com.boot.model.Shipwreck;
import com.boot.repository.ShipwreckRepository;


/**
 * 
 * @author DDudeja
 *
 */
public class ShipwreckControllerTest {

	@InjectMocks
	private ShipwreckController shipwreckController;
	
	@Mock
	private ShipwreckRepository shipwreckRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testShipwreckGet() {
		Shipwreck mockedShipwreck = new Shipwreck();
		mockedShipwreck.setId(1L);
		Mockito.when(shipwreckRepository.findOne(1L)).thenReturn(mockedShipwreck);
		
		Shipwreck shipwreck = shipwreckController.get(1L);
		Mockito.verify(shipwreckRepository).findOne(1L); //to check if the findOne method was called once.
		
		//Assert.assertEquals(1L, shipwreck.getId().longValue());
		MatcherAssert.assertThat(shipwreck.getId(), Matchers.is(1L)); // Hamcrest matching
	}
	
	
}
