package org.jsondoc.core.util;

import com.github.jsonzou.jmockdata.JMockData;

public class Test {
	public static void main(String[] args) {
		String str = JMockData.mockSimpleType(String.class);

		System.out.println(str);
		System.out.println(JMockData.mockMetaDataAll().getStringData().replaceAll(" ", ""));
	}
}
