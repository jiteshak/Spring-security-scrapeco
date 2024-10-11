package in.scrapeco.service;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoggerService {
	public void debug(String msg, String functionName, String data) {

		String logString = "Debug [" + "functionName = '" + functionName + "', message = '" + msg + "', data = '" + data
				+ "']";

		this.log.debug(logString);
	}

	public void info(String msg, String functionName, String data) {

		String logString = "Info [" + "functionName = '" + functionName + "', message = '" + msg + "', data = '" + data
				+ "']";

		this.log.info(logString);
	}

	public void error(String msg, String functionName, String data) {

		String logString = "Error [" + "functionName = '" + functionName + "', message = '" + msg + "', data = '" + data
				+ "']";

		this.log.error(logString);
	}

}
