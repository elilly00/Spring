package com.kh.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {

	private Logger logger = LoggerFactory.getLogger(Log4jTest.class); // logger 생성
	
	public static void main(String[] args) {
		new Log4jTest().test();
	}

	private void test() {
//		logger.fatal("fatal 로그"); 
		// fatal이라는 메소드를 제공하지 않아 빨간줄이 뜸
		// fatal은 아주 심각한 에러이기 때문에 해당 레벨로 설정할 수 없는 영역임
		logger.error("error 로그");
		logger.warn("warn 로그");
		logger.info("info 로그");
		logger.debug("debug 로그");
		logger.trace("trace 로그");
		
		// [debug가 console에 출력되지 않는 이유] 
		// 해당 클래스의 package에 포함된 com.kh.spring의 logger에서 level이 info로 설정되어 있기 때문에 
		// debug를 제외한 모든 로그가 출력 됨 
	}
	

}
