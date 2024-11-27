package com.tendable;

import com.tendable.util.ExcelFileReader;
import com.tendable.util.ExtentManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Slf4j
public class TestBase {
	public String testName = "";
	public Executor executor;
	public Map<String, String> executionSpecifications = new TreeMap<>();
	Properties properties = new Properties();

	public void initialize() {
		log.info("Initializing Execution");
		try {
			InputStream stream = Executor.class.getResourceAsStream(File.separator + "environment.properties");
			properties.load(stream);
		} catch (IOException e) {
			e.fillInStackTrace();
		}
		initializeExecutionSpecifications();
	}

	public void initializeExecutionSpecifications() {
		log.info("Initializing execution specifications");
		executionSpecifications.put("browser", System.getProperty("browser", properties.getProperty("browser").toUpperCase()));
		executionSpecifications.put("channel", System.getProperty("channel", properties.getProperty("channel").toUpperCase()));
		if (executionSpecifications.get("channel").equals("GRID")) {
			executionSpecifications.put("gridUrl", System.getProperty("gridUrl", properties.getProperty("gridUrl").toUpperCase()));
		}
		executionSpecifications.put("url", System.getProperty("url", properties.getProperty("url")));
		log.info("Execution Specifications: {}", executionSpecifications);
	}

	@DataProvider
	public Object[] getData(Method method) {
		ExcelFileReader excel = new ExcelFileReader(Executor.class.getResource(File.separator + "TestData.xlsx"));
		testName = method.getDeclaringClass().getSimpleName();
		int rowCount = excel.getRowCount(testName) - 1;
		Object[] data = new Object[rowCount];
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			int rowNum = rowIndex + 2;
			Map<String, String> rowData = new TreeMap<>();
			for (String colName : excel.getCellsFromRow(testName, 1).values()) {
				rowData.put(colName, excel.getCellValue(testName, rowNum, colName));
			}
			data[rowIndex] = rowData;
		}
		System.out.println(Arrays.toString(data));
		return data;
	}

	@BeforeMethod
	public void beforeMethod() {
		initialize();
	}

	@AfterTest
	public void afterTest() {
		ExtentManager.getInstance().flush();
	}
}
