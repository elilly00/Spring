package com.kh.ajax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.ajax.model.vo.Sample;
import com.kh.ajax.model.vo.User;

@Controller
public class TestController {
	
	@Autowired // 의존성 기입
	private Sample sam;
	
	@RequestMapping("test.do")
	public void test() {
		// 어떤 클래스의 객체를 만들기 위한 방법 
		// 1. annotation 이용 
		// 2.Bean 태그 이용(-> API를 사용할 때 많이 사용함) 
		// 위를 단순히 확인만 하는 용이기 때문에 반환 값은 void로 작성함
		System.out.println(sam);
	}
	
	// 1. ServletOutputStream을 이욯하여 전송 (ServletOutputStream = printWriter)
	// 다른 view에서 보내는 것이 아닌 내가 있는 view안에서 실행하는 것이기 때문에 반환값은 필요없음
	@RequestMapping("test1.do")
	public void test1(@RequestParam("name") String name, @RequestParam("age") int age, HttpServletResponse response) { // RequestParam : 파라미터 받아 오는 annotation
		System.out.println(name);
		System.out.println(age);
		
		// try~catch or throw 둘 다 가능함
		try {
			PrintWriter out = response.getWriter();
			
			if(name.equals("강건강") && age == 20) {
				out.println("ok");
			} else {
				out.println("fail");
			}
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// 1. URLEncoder 이용한 인코딩 방식
//	@RequestMapping("test2.do")
//	@ResponseBody
//	public /*JSONObject*/ String test2() {
//		// JSON을 사용하기 위해선 라이브러리 필요
//		// JSON은 map과 같은 형식으로 작성
//		JSONObject job = new JSONObject();
//		job.put("no", 123);
//		job.put("title", "return json object test");
//		try {
//			job.put("writer", URLEncoder.encode("남나눔", "UTF-8"));
//			job.put("content", URLEncoder.encode("JSON 객체를 뷰로 리턴합니다.", "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		return /*job*/ job.toJSONString();
//		
//		// [Error]
//		// 1. 반환 값이 JSONObject일 땐 404에러가 발생
//		
//		// 2. 반환 값이 String일 땐 404에러 
//		// -> 파일 [/WEB-INF/views/{"no":123,"writer":"남나눔","title":"return json object test","content":"JSON 객체를 뷰로 리턴합니다."}.jsp]을(를) 찾을 수 없습니다.
//		// 에러가 발생함
//		// : String을 보내면 페이지 이름으로 인식해서 넘김. 내가 반환 할 String이 view 이름이 아닌 데이터 자체로 인식하게 하고 싶다면 @ResponseBody annotation을 추가해줘야함
//				// @ResponseBody를 안 붙여주면 return한 String을 화면 이름으로 인식해서
//		// -> 한글이 깨지고 객체 형식이 아닌 String 형식으로 출력됨 
//		// 타입을 String에서 JSON으로 받고싶다? -> dataType을 이용해 'json' 추가
//		// 한글 깨짐 방지 -> URLEncoder.encode(데이터, 인코딩 타입) 이용 -> 뷰에서 디코딩까지 해줘야 제대로 한글이 출력됨
//	}
	
	// 2. setContenttype 이용한 인코딩 방식
//	@RequestMapping("test2.do") // 반환 값이 String이 아니기때문에 @ResponseBody를 사용할 필요없음
//	public void test2(HttpServletResponse response) { // PrintWriter 사용
//		// JSON을 사용하기 위해선 라이브러리 필요
//		// JSON은 map과 같은 형식으로 작성
//		JSONObject job = new JSONObject();
//		job.put("no", 123);
//		job.put("title", "return json object test");
//		job.put("writer", "남나눔");
//		job.put("content", "JSON 객체를 뷰로 리턴합니다.");
//		
//		try {
//			response.setContentType("application/json; charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			out.println(job);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		// 반환 값이 void일 때 인코딩 하는 법 : response.setContentType("application/json; charset=UTF-8"); 추가
//
//	}
	
	// 3. produces를 이용한 인코딩 (요즘에 URLEncoder보다 많이 사용되는 방식)
	@RequestMapping(value="test2.do", produces="application/jsp; charset=UTF-8") // produces : 보내는 방식 작성
	@ResponseBody
	public String test2() {
		// JSON을 사용하기 위해선 라이브러리 필요
		// JSON은 map과 같은 형식으로 작성
		JSONObject job = new JSONObject();
		job.put("no", 123);
		job.put("title", "return json object test");
		job.put("writer", "남나눔");
		job.put("content", "JSON 객체를 뷰로 리턴합니다.");
		
		return job.toJSONString();
	}
	
//	@RequestMapping("test3.do")
//	public void test3(HttpServletResponse response) {
//		ArrayList<User> list = new ArrayList<User>(); // ArrayList : 직접 DB로 이동하지 않고 ArrayList를 사용함으로서 DB에 왔다갔단 한것처럼 하도록 하는 역할
//		list.add(new User("u111", "p111", "강건강", 20, "k111@kh.or.kr", "01011112222"));
//		list.add(new User("u222", "p222", "남나눔", 33, "n222@kh.or.kr", "01022223333"));
//		list.add(new User("u333", "p333", "도대담", 17, "d3331@kh.or.kr", "01033334444"));
//		list.add(new User("u444", "p444", "류라라", 23, "r444@kh.or.kr", "01044445555"));
//		list.add(new User("u555", "p555", "문미미", 29, "m555@kh.or.kr", "01055556666"));
//		
//		// 여러개 담기 위해 JSONArray 사용
//		// list에 담긴 데이터를 jsonObject에 담고 JsonArray에 담기
//		JSONArray jArr = new JSONArray();
//		for(User user : list) {
//			JSONObject job = new JSONObject();
//			job.put("userId", user.getUserId());
//			job.put("userPwd", user.getUserPwd());
//			job.put("userName", user.getUserName());
//			job.put("age", user.getAge());
//			job.put("email", user.getEmail());
//			job.put("phone", user.getPhone());
//			
//			jArr.add(job);
//		}
//		
//		try {
//			response.setContentType("application/json; charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			out.println(jArr);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@RequestMapping(value="test3.do", produces="application/json; charset=UTF-8") // 한글이 깨지지 않도록 인코딩 해줌
	@ResponseBody
	public String test3() {
		ArrayList<User> list = new ArrayList<User>(); 
			list.add(new User("u111", "p111", "강건강", 20, "k111@kh.or.kr", "01011112222"));
			list.add(new User("u222", "p222", "남나눔", 33, "n222@kh.or.kr", "01022223333"));
			list.add(new User("u333", "p333", "도대담", 17, "d3331@kh.or.kr", "01033334444"));
			list.add(new User("u444", "p444", "류라라", 23, "r444@kh.or.kr", "01044445555"));
			list.add(new User("u555", "p555", "문미미", 29, "m555@kh.or.kr", "01055556666"));
		
		JSONArray jArr = new JSONArray();
		for(User user : list) {
			JSONObject job = new JSONObject();
			job.put("userId", user.getUserId());
			job.put("userPwd", user.getUserPwd());
			job.put("userName", user.getUserName());
			job.put("age", user.getAge());
			job.put("email", user.getEmail());
			job.put("phone", user.getPhone());
			
			jArr.add(job);
		}
		return jArr.toJSONString() // 객체로 넘어오도록 toJSONStrig을 반환함
;
		
	}
	
	
	
	
	
}
