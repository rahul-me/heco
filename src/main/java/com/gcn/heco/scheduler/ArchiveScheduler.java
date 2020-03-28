package com.gcn.heco.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.gcn.heco.file.service.MiFileControllerService;


@Repository
@PropertySource("file:config/config.properties")
public class ArchiveScheduler {
	
	static Logger logger = LoggerFactory.getLogger(ArchiveScheduler.class);

	@Autowired
	private MiFileControllerService fileService;
	
	@Scheduled(cron = "${cron.archiveScheduler}")
	private void archiveSchedules(){
		fileService.archiveDataAsExpected();
	}
}
