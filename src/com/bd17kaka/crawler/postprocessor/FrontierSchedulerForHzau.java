package com.bd17kaka.crawler.postprocessor;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.postprocessor.FrontierScheduler;

public class FrontierSchedulerForHzau extends FrontierScheduler {
	
	private static final long serialVersionUID = 1L;

	public FrontierSchedulerForHzau(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 将包含hazu的url进行爬虫处理
	 */
	protected void schedule(CrawlURI caUri){
		String uri = caUri.toString();
		if(uri.contains("hzau")){
			getController().getFrontier().schedule(caUri);
		}
	}
}