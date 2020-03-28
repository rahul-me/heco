package com.gcn.heco.httpservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gcn.heco.app.model.NocMiSchedule;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.helper.HecoHelper;
import com.gcn.heco.manager.MiHandler;
import com.gcn.heco.schedule.listener.IScheduleEventListener;

@Service
public class HttpServiceHandler {

	static Logger logger = LoggerFactory.getLogger(HttpServiceHandler.class);

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private HecoHelper helper;

	@Autowired
	private MiHandler miHandler;

	@Autowired
	private IScheduleEventListener scheduleEventListner;

	@Async
	public void sendPostRequestToMI(MiSchedule miSchedule) {
		logger.info("heco: Got schedule having id "+miSchedule.getId()+" to be posted at NOC.");
		String requestBody = helper.getJsonString(miHandler.generateMIRequestObject(miSchedule));

		StringBuilder response = new StringBuilder();
		NocMiSchedule nocMiSchedule = null;
		String adminKey = configProperties.getAdminKey();

		URL url = null;
		try {
			url = new URL(configProperties.getNocScheduleEndpoint());
		} catch (MalformedURLException e1) {
			logger.error(e1.getMessage(), e1);
		}

		HttpURLConnection conn = null;

		if (url != null) {
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("adminKey", adminKey);
				conn.setDoOutput(true);
				conn.setRequestMethod(configProperties.getReqPost());
				conn.setRequestProperty(configProperties.getConTypeStr(), configProperties.getConTypeAJ());

				writePostBody(conn, requestBody);				

				getResponse(response, conn);
				
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					nocMiSchedule = (NocMiSchedule) helper.getObjectFromJsonString(response.toString(),
							NocMiSchedule.class);

				} else {
					logger.error("heco: Got http response code "+conn.getResponseCode()+" when posting schedule at NOC having id "+miSchedule.getId()+".");
				}

				scheduleEventListner.afterPost(miSchedule, nocMiSchedule);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					if (conn != null) {
						conn.disconnect();
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private void writePostBody(HttpURLConnection conn, String requestBody) {
		try (OutputStream os = conn.getOutputStream()) {
			os.write(requestBody.getBytes());
			os.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void getResponse(StringBuilder response, HttpURLConnection conn) {
		try (Reader reader = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(reader)) {
			String output;
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Async
	public void sendDeleteRequestToNOC(MiSchedule miSchedule) {
		logger.info("heco: Got schedule having id "+miSchedule.getId()+" to be cancelled at NOC.");
		String deleteUrl = configProperties.getNocScheduleEndpoint() + "/" + miSchedule.getScheduleId();
		String adminKey = configProperties.getAdminKey();
		StringBuilder response = new StringBuilder();
		NocMiSchedule nocMiSchedule = null;

		HttpURLConnection conn = null;

		URL url = null;
		try {
			url = new URL(deleteUrl);
		} catch (MalformedURLException e1) {
			logger.error(e1.getMessage(), e1);
		}

		if (url != null) {
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("adminKey", adminKey);
				conn.setDoOutput(true);
				conn.setRequestMethod(configProperties.getReqDelete());
				conn.setRequestProperty(configProperties.getConTypeStr(), configProperties.getConTypeAJ());

				getResponse(response, conn);

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					nocMiSchedule = (NocMiSchedule) helper.getObjectFromJsonString(response.toString(),
							NocMiSchedule.class);
				} else {
					logger.error("heco: Got http response code "+conn.getResponseCode()+" when cancelling schedule at NOC having id "+miSchedule.getId());
				}

				scheduleEventListner.afterCancel(miSchedule, nocMiSchedule);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					if (conn != null) {
						conn.disconnect();
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
