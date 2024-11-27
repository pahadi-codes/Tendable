package com.tendable.pages;

import com.tendable.Executor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomePage {
	Executor executor;
	public HomePage(Executor executor) {
		log.info("Home Page Initialized");
		this.executor = executor;
	}
}
