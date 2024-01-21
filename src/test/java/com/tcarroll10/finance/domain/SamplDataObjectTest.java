package com.tcarroll10.finance.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tcarroll10.finance.domain.SamplDataObject;

public class SamplDataObjectTest {
	
	@Test
	public void testConstructor() {

		SamplDataObject a = new SamplDataObject();
		assertEquals(a.getRecordDate(), null);
	}
	

}
