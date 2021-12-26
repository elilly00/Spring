package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//public class TestInterceptor extends HandlerInterceptorAdapter { 
	// HandlerInterceptorAdapter : The type HandlerInterceptorAdapter is deprecated(Spring 5.3버전부터 사용을 권장하지 않음)
//public class TestInterceptor implements HandlerInterceptor { 
public class TestInterceptor implements AsyncHandlerInterceptor { 
	// AsyncHandlerInterceptor 또는 HandlerInterceptor 둘 중 아무거나 사용 가능 (메소드 내 내용이 조금씩 다름)
	
	private Logger logger = LoggerFactory.getLogger(TestInterceptor.class);
	
	// 세 가지 모두 다 작성할 필요 없이 인터셉터를 넣고자 하는 부분에 필요한 것만 골라서 사용하면 됨
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 전처리
		// DispatcherServlet이 Controller를 호출하기 전(Controller로 요청이 들어가기 전)에 수행
		
		// if를 이용해 많이 작성함
		if(logger.isDebugEnabled()) { // 지금 들어온 것이 디버깅 레벨일 경우에만 사용 -> 사용하지 않아도 되지만 해주면 아래 부분에서 로깅이 필요가 없음
			logger.debug("========== START ==========");
			logger.debug(request.getRequestURI());
		}
//		return HandlerInterceptor.super.preHandle(request, response, handler);
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler); // 항상 true 반환
	}
	
	@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
		
		// 후처리
		// Controller에서 DispatcherServlet으로 리턴되는 순간에 수행
		// ModelAndView를 이용해 작업 결과 등을 참조 가능
		
		if(logger.isDebugEnabled()) { 
			logger.debug("---------- view ----------");
		}
		
		// 반환값이 void이기 때문에 아래 내용은 없어도 됨
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//		AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		}
	
	@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
				throws Exception {
		
		// 뷰단 처리 후
		// 모든 view에서 모든 작업(최종 결과를 생성하는 일 포함)이 완료된 후 수행
		
		if(logger.isDebugEnabled()) {
			logger.debug("========== END ==========\n");
		}
		
		// 반환값이 void이기 때문에 아래 내용은 없어도 됨
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//		AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		}
}
