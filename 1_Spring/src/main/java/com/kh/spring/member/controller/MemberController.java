package com.kh.spring.member.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes("loginUser")
@Controller // 객체 생성, Controller의 성질을 가질 수 있도록 생성해줌
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired // DI(의존성 주입) 기능을 구현함
	private MemberService mService; // interface를 불러옴. 
	/* 
	 	클래스명이 바껴도 영향을 미치지않음 하지만 객체를 직접적으로 생성할 수 없음 
	 	-> MemberServiceimpl.java에 @Service(annotation Service)를 선언함으로서 객체를 생성
	 */
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
//	@RequestMapping(value="login.me", method=RequestMethod.POST) // 메소드
//	public void login() {
//		System.out.println("로그인 처리합니다.");
		// WARN : org.springframework.web.servlet.PageNotFound - No mapping for POST /spring/login.me
		// HandlerMapping 즉,@RequestMapping이 선언되어있지 않기 때문에 발생 -> 그래도 여전히 안됨 -> MemberController를 @Controller로 선언을 해야 객체로 인지함 
		
//	}
	
	/*********** 파라미터 전송 받기 ***********/
	// 1. HttpServletRequest를 통해 전송받기(JSP/Servlet방식)
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(HttpServletRequest request) {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		//잘 넘어오는지 확인
		System.out.println("id1 : " + id);
		System.out.println("pwd1 : " + pwd);
	}
*/
	// 2. @RequestParam(value, defaultValue, required) 방식 
	//	[value : view에서 받아오는 파라미터 명 (들어가는 값이 1개면 value생략 가능)]
	//	[defaultValue : 값이 없거나 null일 때 지정한 값이 자동적으로 들어가도록 하는 속성]
	//	[required : 내가 지정한 파라미터가 꼭 필수적으로 받아와하는 파라미터인지를 설정하는 속성, 디폴트 값은 true임]
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(@RequestParam(value="id", defaultValue="hello") String userId, 
					  @RequestParam(value="pwd", defaultValue="world") String userPwd,
					  @RequestParam(value="test", defaultValue="Spring", required=false) String test) { // menubar의 id와 pwd를 가져오는 거임
		// [Error] 400error
		// Required request parameter 'test' for method parameter type String is not present
		// test라는 파라미터가 존재하지 않아 받을 수 없다는 에러가 발생함
		// currnetPage같이 지금은 존재하지 않지만 나중에 생길 수도 있는 것은 required 속성을 통해 설정 가능함 
		
		//잘 넘어오는지 확인
		System.out.println("id2 : " + userId);
		System.out.println("pwd2 : " + userPwd);
		System.out.println("test : " + test);
	}
*/
	
	// 3. @RequestParam 생략
	// 매개변수 명과 파라미터 명이 일치하면 생략 가능
	// value, defaultValue, required속성 사용 불가, 웬만하면 생략하지 않고 연습하기
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(String id, String pwd) { 
		
		System.out.println("id3 : " + id);
		System.out.println("pwd3 : " + pwd);
	}
*/
	
	// 4.@ModelAttribute 방식
	// 받아올 파라미터가 많을 때 사용하면 효과적임
	// 제대로 동작하는 조건 : 1. 기본생성자가 존재해야 함 2. setter가 있어야 한다
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(@ModelAttribute Member m) { 
		// 기본생성자가 없다면 기본생성자를 이용해 객체를 만들어야하지만 만들 수 없기때문에 에러가 남
		// setId가 없거나 필드 명과 파라미터 명이 다르다면 id를 받아오지 못하기 때문에 null이 출력 됨
		
		System.out.println("id3 : " + m.getId());
		System.out.println("pwd3 : " + m.getPwd());
	}
*/
	// 5.@ModelAttribute 생략 
	// 실무에 가면 혼나거나 가독성이 떨어지기 때문에 웬만하면 생략하지 않는 것이 좋음 
	/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(Member m, HttpSession session) { 
		//	MemberService mService = new MemberService(); 

		//	System.out.println(mService);

		// 이전에 사용했던 new MemberService() 객체를 만드는 방식은 주도권이 나한테 있으며 결합도가 높음
		// 결합도가 높다는 것을 확인할 수 있는 두가지 
		// 1. 클래스 명 변경에 직접적인 영향을 받음  
		// 2. 매 요청마다 새로운 service객체가 생성 됨(주소값이 계속 달라짐)
		
		// 결합도를 낮추기 위한 방법
		// 1. MemberService를 필드로 올리면 2번이 해결됨(새로운 객체가 생성되지 않음) -> 주도권을 프레임워크가 가지도록 함
		// 2. MemberService파일을 생성하면서 interface를 끼우면 1번이 해결됨
		
		Member loginMember = mService.memberLogin(m);
		
		if(loginMember != null) {
			session.setAttribute("loginUser", loginMember);
		} else {
			
		}
	}
*/
	
	/*********** 전달하고자 하는 데이터가 있을 경우에 대한 방법 ***********/
	// 1. Model 객체 사용 (Model은 HomeController에 위치함)  -> 
	/* 
		Spring에선 결과 view로 넘어갈 때 ViewResolver가 담아서 넘김 
	 	지금까진 메소드에서의 반환 값을 기본적으로 void로 뒀었지만 이제는 String으로 둘거임 
	 	WHY? String으로 반환된 값을 viewResolver가 받아서 사용할 수 있도록 하기 위해
	 */
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, HttpSession session, Model model) { 
	
		Member loginMember = mService.memberLogin(m);
		
		if(loginMember != null) {
			session.setAttribute("loginUser", loginMember);
			// return "../home"; // forward 방식으로 넘어감(url에 login.me가 그대로 찍힘)
			// home.jsp라고 작성하면 home.jsp.jsp로 값이 넘어감
			return "redirect:home.do"; // redirect: : sendRedirect와 같음
		} else {
			model.addAttribute("msg", "로그인에 실패하였습니다.");
			return "../common/errorPage";
		}
		// 로그인 성공, 실패 시 둘 다 404에러가 뜸 -> member폴더에 home, errorPage가 없기 때문 -> member-context에서 경로를 바꾸지 않고 Controller에서 바꿔주기
		// ../ , ../common/ 추가하기
	}
*/	
	
	// 2. ModelAndView객체 사용
	// 값과 뷰가 함께 들어있기 때문에 반환값을 ModelAndView로 작성(forward 방식으로 넘어감)
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public ModelAndView login(Member m, HttpSession session, ModelAndView mv) { 
	
		Member loginMember = mService.memberLogin(m);
		
		if(loginMember != null) {
			session.setAttribute("loginUser", loginMember);
			mv.setViewName("redirect:home.do");
			// 데이터는 session의 loginMember를 담았기 때문에 데이터는 보낼 필요 없음
		} else {
			mv.addObject("msg", "로그인에 실패하였습니다.");
			mv.setViewName("../common/errorPage");
		}	
		return mv;
	}	
*/	

/*	
 	// 로그아웃 방법 1
	@RequestMapping("logout.me") // method는 생략 가능
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:home.do";
	}
*/
	
	// 3. session에 저장할 때 @SessionAttributes 사용
	// Model을 사용하는 이유 : model객체에 attribute가 추가될 때, 자동으로 키 값을 찾아 세션에 등록하는 기능을 함
/*	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, Model model) {  
	
		Member loginMember = mService.memberLogin(m);
		
		if(loginMember != null) {
			model.addAttribute("loginUser", loginMember); // 제일 위 @SessionAttributes을 작성해 이를 통해 찾아옴
			return "redirect:home.do";
		} else {
			// 에러를 발생시키도록 함
			throw new MemberException("로그인에 실패하였습니다.");
			// 에러가 날 때 어느 페이지로 이동할 것인지 작성하지 않아서 
			// Request processing failed; nested exception is com.kh.spring.member.model.exception.MemberException: 로그인에 실패하였습니다.
			// 이란 에러가 발생함
		}	
	}
*/	
	// 로그아웃 방법 2
	@RequestMapping("logout.me") 
	public String logout(SessionStatus session) {
		session.setComplete();
		
		return "redirect:home.do";
	}
	
	@RequestMapping("enrollView.me")
	public String enrollView() {
		logger.debug("회원등록페이지");
		return "memberJoin";
	}
	
	@RequestMapping("minsert.me") // @ModelAttribute : 가져오는 값이 많으면 사용함
	public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
														 @RequestParam("address1") String address1,
														 @RequestParam("address2") String address2) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		// 비밀번호 암호화 : SHA-512 방식 사용
		// SHA-512 방식의 한계점을 보완한 bcrypt 방식 사용
		// bcrypt 암호화 방식 : 스프링 시큐리티 모듈에서 제공
		//		암호화 + 랜덤한 salt값 
		//		-> 같은 문자를 입력해도 랜덤한 값을 생성하기 때문에 아무리 많은 데이터가 있더라도 복구하기 힘듦(강력한 암호화 방식임)
		String encPwd = bcrypt.encode(m.getPwd()); // rawPassword : 평문화된 비밀번호(암호화 전 비밀번호)
		m.setPwd(encPwd);
		
		int result = mService.insertMember(m);
		
		if(result > 0) {
			return "redirect:home.do";
		} else {
			throw new MemberException("회원가입에 실패하였습니다.");
		}
		
	//	System.out.println(m); 
		// 한글이 깨지고 이 뜸 -> fitter도 Spring에서 지원 함 -> web.xml에서 작성
		// address에 null이 뜸 -> 매개변수에 @RequestParam 이용해 가져오기
	//	System.out.println(bcrypt);
		// [Error]
		// org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type~~ 에러 뜸
		// spring-security.xml을 생성했지만 spring이 읽을 수 있도록 등록하지 않아 객체를 읽을 수 없어 발생한 에러
	}
	
	// [Error] 로그인 시도 시 발생한 에러
	// java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener
	// 비밀번호가 암호화 되었기 때문에
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, Model model) { 
		
		// 로그인 할 때 쓴 비밀번호 암호화 -> 로그인 실패 -> 암호화를 통해 비밀번호 암호는 계속 바뀌게 되기때문에 비밀번호가 일치 하지 않아 로그인 실패
/*		String encPwd = bcrypt.encode(m.getPwd());
		m.setPwd(encPwd);
		System.out.println(encPwd);
*/		
		
		// Member m의 pwd는 암호화 x , loginMember의 pwd는 암호화 o
		
		System.out.println(bcrypt.encode(m.getPwd()));
		
		Member loginMember = mService.memberLogin(m); 
		
		if(bcrypt.matches(m.getPwd(), loginMember.getPwd())) { 
		//  matches(rawPassword, encodedPassword) : 원본 비밀번호화 암호화된 비밀번호가 서로 일치한지 비교하는 메소드 
		//	-> rawPassword: 원본, encodedPassword: 암호화된 비밀번호
			model.addAttribute("loginUser", loginMember); 
			logger.info(loginMember.getId());
			return "redirect:home.do";
		}else {
			throw new MemberException("로그인에 실패하였습니다.");
		}	
	}
	
	@RequestMapping("myinfo.me")
	public String myInfoView() {
		return "mypage";
	}
	
	@RequestMapping("mupdateView.me")
	public String updateFormView() {
		return "memberUpdateForm";
	}
	
	@RequestMapping("mupdate.me")
	public String updateMember(@ModelAttribute Member m, @RequestParam("post") String post,
												   		 @RequestParam("address1") String address1,
												   		 @RequestParam("address2") String address2, Model model) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.updateMember(m);
		
		if(result > 0) {
			model.addAttribute("loginUser", m); // sessionAttribute를 통해서 session에 값을 넣었기 때문에 model를 이용해 내 정보를 넣음
			return "redirect:myinfo.me";
		} else {
			throw new MemberException("회원정보 수정에 실패하였습니다.");
		}
	}
	
	@RequestMapping("mpwdUpdateView.me")
	public String updatePasswordForm() {
		return "memberPwdUpdateForm";
	}
	
	@RequestMapping("mPwdUpdate.me")
	public String updatePassword(@RequestParam("pwd") String oldPwd, @RequestParam("newPwd1") String newPwd, Model model
								 /* HttpServletRequest request*/) {
	/*	HttpSession session = request.getSession();
		Member m = (Member)session.getAttribute("loginUser");
		System.out.println(m);
	 	이 방법이나 Model을 사용하는 방법 둘 다 가능
	*/	
		Member m = (Member)model.getAttribute("loginUser"); 
		
		Member dbMember = mService.memberLogin(m); 
		
		int result = 0;
		if(bcrypt.matches(oldPwd, dbMember.getPwd())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", m.getId());
			map.put("newPwd", bcrypt.encode(newPwd)); // bcrypt.encode를 사용해야 암호회된 비번으로 db에 들어감
			
			result = mService.updatePassword(map);
		}
		
		String id = m.getId();
		
		// 예전에 사용하던 방식 -> 비밀번호를 변경하면 암호화가 되지 않음 -> 암호화가 되도록 해야함
	/*	HashMap<String, String> map = new HashMap<String, Stirng>();
		map.put("id", id);
		map.put("oldPwd", oldPwd);
		map.put("newPwd", newPwd); // 현재 내 비번 (평문)
		int result = mService.updatePwd(m); // 바꿀 비번 (평문)
	*/ 
		
		if(result <= 0) {
			throw new MemberException("비밀번호 수정에 실패하였습니다.");
		} 
		
		return "redirect:home.do";
	}
	
	// 아이디 체크
	@RequestMapping("dupId.me")
	public void duplicateId(@RequestParam("id") String id, HttpServletResponse response) {
//		int result = mService.duplicateId(id);
		String result = mService.duplicateId(id) == 0 ? "NoDup" : "Dup";
		
		try {
			response.getWriter().println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
