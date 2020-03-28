package com.gcn.heco.controller;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.app.model.ErrorResponse;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.file.service.MiFileControllerService;
import com.gcn.heco.helper.HecoHelper;

@Api(name = "heco-interface", description = "heco-interface")
@Controller
@RequestMapping(path = "/heco-interface")
public class FileController {

	static Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private MiFileControllerService fileService;

	@Autowired
	private HecoHelper helper;

	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private ErrorResponse errorResponse;

	@ApiMethod(description = "Save the Regulation signal from file")
	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public HttpEntity<Map<String, Object>> createSchedules(HttpServletRequest request,
			@ApiPathParam(name = "file") @RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			if (helper.isRequestValid(request, file)) {
				InputStream inputStream = file.getInputStream();
				DateTimeInfo dateTimeInfo = helper.generateDTI(request);
				List<MiSchedule> manualApiSchedules = fileService.listSchedules(inputStream, dateTimeInfo);
				int isScedulesValid = fileService.isSchedulesValid(manualApiSchedules);
				if (isScedulesValid != 0) {
					response.put(Constant.BAD_REQ_REASON, errorResponse.getErrorMessage());
					response.put(Constant.BAD_REQ_ROW, errorResponse.getErrorAtRow());
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					fileService.handleSchedules(manualApiSchedules, dateTimeInfo);
					response.put(Constant.STATUS, Constant.STATUS_RECEIVED);
				}
			} else {
				response.put(Constant.STATUS, Constant.STATUS_NOT_RECEIVED);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.put("status", e);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiMethod(description = "Delete the Regulation signal")
	@RequestMapping(value = "/events", method = RequestMethod.DELETE)
	public HttpEntity<Map<String, Object>> deleteSchedules(@ApiPathParam(name="dateTimeInfo",format="YYYY-MM-DD") @RequestBody DateTimeInfo dateTimeInfo) {
		Map<String, Object> response = new LinkedHashMap<>();

		try {
			fileService.deleteSchedules(dateTimeInfo);
			response.put(Constant.STATUS, Constant.STATUS_RECEIVED);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.put(Constant.STATUS, e);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiMethod(description = "Update the Regulation signal")
	@RequestMapping(value = "/events/{date}", method = RequestMethod.PUT)
	public HttpEntity<Map<String, Object>> updateSchedules(@ApiPathParam(name="date",format="YYYY-MM-DD") @PathVariable("date") String date, HttpServletRequest request){
		Map<String, Object> response = new LinkedHashMap<>();

		try {
			InputStream inputStream = request.getInputStream();
			Map<String, List<String[]>> data = helper.isRequestValid(inputStream);
			boolean validFlag = (!data.get(configProperties.getReqParamTimeZone()).isEmpty()
					&& !data.get(configProperties.getReqParamFile()).isEmpty()) ? true : false;
			List<MiSchedule> manualApiSchedules = fileService.listSchedules(data, date);
			int isScedulesValid = fileService.isSchedulesValid(manualApiSchedules);
			if (validFlag) {
				if (isScedulesValid != 0) {
					errorResponse.setErrorAtRow(errorResponse.getErrorAtRow() + 1);
					response.put(Constant.BAD_REQ_REASON, errorResponse.getErrorMessage());
					response.put(Constant.BAD_REQ_ROW, errorResponse.getErrorAtRow());
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					fileService.updateSchedules(data, date, manualApiSchedules);
					response.put(Constant.STATUS, Constant.STATUS_RECEIVED);
				}
			} else {
				response.put(Constant.STATUS, Constant.STATUS_INVALID);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.put(Constant.STATUS, e);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
