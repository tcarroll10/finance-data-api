package com.tcarroll10.finance.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

public class TestStrings {

	@Test
	public void testSplit() {

		//test with two fields
		String a = "fields=record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr&filter=record_date:eq:2023-11-30";
		String[] b = a.split("&");
		assertEquals(b[0], "fields=record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr");
		assertEquals(b[1], "filter=record_date:eq:2023-11-30");
		
		//test with no "&"
		String c = "fields=record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr";
		String[] d = c.split("&");
		assertEquals(d[0], "fields=record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr");

	}

	@Test
	public void testAddingToMap() {
		//single string
		String a = "fields=record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr";
		String[] b = a.split("=");
		Map<String,String> params = new HashMap<>();
		//Map<String, List<String>> params = new HashMap<>();
		// Using computeIfAbsent to handle the logic
		//params.computeIfAbsent(b[0], k -> new ArrayList<>()).add(b[1]);
		params.put(b[0],b[1]);
		
		String testResult = "record_date,security_type_desc,security_desc,avg_interest_rate_amt,src_line_nbr";
		assertEquals(params.get("fields"),testResult);			
		

	}

	@Test
	public void testHashMapFieldsFunctionCall() {

		String sql;

		Map<String, String[]> params = new HashMap<>();
		params.put("fields", new String[] { "f0, f1", "f2", "f3" });

		String[] values = params.get("fields");

		if (values != null && values.length > 0) {
			String selectFields = String.join(", ", values);
			sql = "SELECT " + selectFields + " FROM SECURITY";
		} else {
			sql = "";
		}

		assertEquals(sql, "SELECT f0, f1, f2, f3 FROM SECURITY");

	}

	@Test
	public void testHashMapFilterFunctionCall() {

		String sql;

		Map<String, String> symbols = new HashMap<>();
		symbols.put("eq", "=");
		symbols.put("in", "in");

		Map<String, String> params = new HashMap<>();
		params.put("filter", "record_date:eq:2023-11-30");

		String values = params.get("filter");

		String[] split = values.split(":");

		sql = "WHERE " + split[0] + " " + symbols.get(split[1]) + " " + split[2];

		assertEquals(sql, "WHERE record_date = 2023-11-30");

	}
	
	@Test
	public void testHashMapSortFunctionCall() {
		
		String sql;
		
		Map<String, String> params = new HashMap<>();
		params.put("sort", "-record_date");
		
		String values = params.get("sort");
		
		if (values.charAt(0) == '-') {
			sql = "ORDER BY " + values.substring(1) + " DESC";
		} else {
			sql = "ORDER BY " + values + " ASC";
		}
			
		assertEquals(sql, "ORDER BY " + "record_date" +" DESC");

		
	}

}
