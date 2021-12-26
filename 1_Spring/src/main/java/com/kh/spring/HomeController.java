package com.kh.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class); 
	// - Logger: 로그를 생성(발생시키기) 위한 클래스  
	// - LoggerFactory : logger를 사용하기 위해 생성하는 메소드
	// HomeController에 있다고 해서 매개변수에 HomeController.class를 작성해야하는 건 아님
	// MemberController, DAO 등 아무 클래스 작성 가능함 (매개변수는 어떤 클래스에 있는 logger를 읽어오고 싶은지 작성함)
	// 매개변수에 HomeController가 아닌 다른 클래스가 적혀있다면 다른 클래스의 logger를 읽어오기 때문에 32번째 줄 부분은 의미가 없어지게 됨
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home.do", method = RequestMethod.GET) // @RequestMapping이 HandlerMapping임
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale); 
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate ); 
		// jspController에선 request.setAttribute를 통해 데이터를 보냈지만 
		// Spring에선 Model이란 것을 사용할 수 있고 Model에 addAttribute메소드를 통해서 데이터를 전송 할 수 있음
		
		return "home";
	}
	
	
}
