package com.kh.spring.board.contorller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.model.vo.Member;

@Controller
public class BoardController /*implements AsyncHandlerInterceptor*/{
	
//	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService bService;
	
	@RequestMapping("blist.bo") // RequestMapping = HandlerMapping
//	public String boardList(@RequestParam("page") int page) { // @RequestParam은 내가 가지고 오고자 하는 데이터의 자료형에 맞춰서 가지고 옴
	public /*String*/ ModelAndView boardList(@RequestParam(value="page", required=false) Integer page, /*Model model*/ ModelAndView mv) {
		
		int currentPage = 1;
		if(page != null) { 
			// page != null이라고 작성하면 빨간 줄이 뜨는 이유 : int는 원시타입(기본자료형)이기때문에 null을 가질 수 없음. 즉, null은 참조형 자료형일때만 가질 수 있다.
			// 하지만 페이지 값이  넘어왔다 안넘어왔다는 것을 체크해야 하기 때문에 0을 작성하는 건 바람직하지 않음
			// -> page를 래퍼클래스로 변환(Integer page)
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if(list != null) {
			// 1) 반환 값이 String인 상태에서  view에 데이터 전달 : Model 추가
//			model.addAttribute("pi", pi);
//			model.addAttribute("list", list);
			
			// 2) 반환 값이 ModelAndView로 변경한 상태에서 view에 데이터 전달 
			mv.addObject("pi", pi);
			mv.addObject("list", list);
			mv.setViewName("boardListView");
			
			
			
		} else {
			throw new BoardException("게시글 전체 조회에 실패하였습니다.");
		}
		
//		return "boardListView";
		return mv;
	}
	// [Error]
	// Required request parameter 'page' for method parameter type Integer is not present
	// 필수인 파라미터 'page'가 없어 발생한 에러. 매개변수에 required=false를 추가해 데이터가 들어가지 않을 수도 있다고 정의해야함
	
	@RequestMapping("binsertView.bo")
	public String boardInsertView() {
		return "boardInsertForm";
	}
	
	@RequestMapping("binsert.bo")
//	public String boardInsert(@ModelAttribute Board b, @RequestParam("uploadFile") String uploadFile ) {
	public String boardInsert(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request ) { // 파일 업로드시 String이 아닌 MultipartFile로 작성함
		System.out.println(b);
		System.out.println(uploadFile);
		System.out.println(uploadFile.getOriginalFilename()); 
		// MultipartFile uploadFile은 파일을 등록하지 않아도 객체가 생성되며 null이 출력되지 않기 때문에 getOriginalFilename()을 통해서 파일이 들어왔는지 안들어왔는지를 확인해야함
		
//		if(!uploadFile.getOriginalFilename().equals("")) { // 아래와 같음
		if(uploadFile != null && !uploadFile.isEmpty()) {
			String renameFileName = saveFile(uploadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(uploadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.insertBoard(b);

		if(result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new BoardException("게시글 등록에 실패하였습니다.");
		}
	}
	
	public String saveFile(MultipartFile file, HttpServletRequest request) {
		// 작은 resources에 접근하기
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/buploadFiles";
		
		File folder = new File(savePath);
		if(!folder.exists()) { // 폴더가 존재하지 않으면
			folder.mkdirs();	// 폴더를 생성함
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String originFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(new Date(System.currentTimeMillis())) + "." + originFileName.substring(originFileName.lastIndexOf(".") + 1);
		
		String renamePath = folder + "/" + renameFileName;
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return renameFileName;
	}
	
	// boardDetil 시작
	@RequestMapping("bdetail.bo")
	public String /*ModelAndView*/ boardDetail(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model /*ModelAndView mv*/ ) { // 반환 값을 String으로 써도 되고 ModelAndView로 바꿔도 됨
		
		Board b = bService.selectBoard(bId);
		
		if(b != null){
			model.addAttribute("board", b);
			model.addAttribute("page", page);
			
//			mv.addObject("board", b);
//			mv.addObject("page", page);
//			mv.setViewName("boardDetailView");
		} else {
			throw new BoardException("게시글 상세보기 실패하였습니다.");
		}
		
		return "boardDetailView";
//		return mv;
	
	}
	
	@RequestMapping("bupView.bo")
	public String boardUpdateView(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model) {
		
		Board b = bService.selectBoard(bId);
		
		model.addAttribute("board", b).addAttribute("page", page);
		
		return "boardUpdateForm";
	}
	
	@RequestMapping("bupdate.bo")
	public String updateBoard(@ModelAttribute Board b, @RequestParam("reloadFile") MultipartFile reloadFile, @RequestParam("page") int page, 
							  HttpServletRequest request, Model model) {
		
		if(reloadFile != null && !reloadFile.isEmpty()) { // 수정할 파일 존재
			// 수정할 파일 존재하는 동시에 기존 파일도 존재한다? = 기존 파일은 삭제
			if(b.getRenameFileName() != null) {
				deleteFile(b.getRenameFileName(), request); // 기존 파일 삭제
			}
			
			String renameFileName = saveFile(reloadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(reloadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			model.addAttribute("page", page);
		} else {
			throw new BoardException("게시글 수정에 실패하였습니다.");
		}
		
		return "redirect:bdetail.bo?bId=" + b.getBoardId();
	}
	
	
	public void deleteFile(String fileName, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources"); // 작은 resources
		String savePath = root + "/buploadFiles";
		
		File f = new File(savePath + "/" + fileName);
		
		if(f.exists()) {
			f.delete();
		}
	}
	
	@RequestMapping("bdelete.bo")
	public String deleteBoard(@RequestParam("bId") int bId, HttpServletRequest request) {
		
		// 게시글 삭제 시 첨부파일도 지워지도록 하기
		Board b = bService.selectBoard(bId);
		if(b.getOriginalFileName() != null) {
			deleteFile(b.getRenameFileName(), request);
		}
		
		// 게시글 지우기
		int result = bService.deleteBoard(bId);
		
		if(result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new BoardException("게시물 삭제를 삭제하였습니다.");
		}
		
	}
	
	
	@RequestMapping("addReply.bo") // 반환값은 String으로, 댓글 등록 성공 시 success 반환
	@ResponseBody 
	public String addReply(@ModelAttribute Reply r,  HttpServletRequest request) {
		
		String replyWriter = ((Member)request.getSession().getAttribute("loginUser")).getId();
		
		r.setReplyWriter(replyWriter);
		
		int result = bService.insertReply(r);

		if(result > 0) {
			return "success";
		} else {
			throw new BoardException("댓글 등록에 실패하였습니다."); 
		}

//		String result = bService.insertReply(r) == 0 ? "fail" : "success";
//		
//		return result;

	}
	
	// 댓글 리스트 1. JSON 방식
//	@RequestMapping(value="rList.bo", produces="application/json; charset=UTF-8")
//	@ResponseBody // 리턴값을 페이지명으로 인식되기 때문에 
//	public String getReplyList(@RequestParam("boardId") int bId) {
//
//		ArrayList<Reply> list = bService.getReplyList(bId);
//		
//		// 객체 보내기 위해선 JSONArray가 필요함 -> JSON 라이브러리 추가
//		JSONArray jArr = new JSONArray();
//		for(Reply r : list) {
//			JSONObject job = new JSONObject();
//			job.put("replyId", r.getReplyId());
//			job.put("replyContent", r.getReplyContent());
//			job.put("replytWriter", r.getReplyWriter());
//			job.put("nickName", r.getNickName());
//			job.put("refBoardId", r.getRefBoardId());
//			job.put("replytCreateDate", r.getReplyCreateDate().toString()); // date타입은 JSON에서 받질 못해 강제로 String으로 바꿔야야 인식함
//			job.put("replytModifyDate", r.getReplyModifyDate().toString());
//			job.put("replytStatus", r.getReplyStatus());
//			
//			jArr.add(job);
//		}
//		
//		return jArr.toJSONString(); 
//	}
	
	// 댓글 리스트 2. GSON 방식
	@RequestMapping("rList.bo")
	public void getReplyList(@RequestParam("boardId") int bId, HttpServletResponse response) {

		ArrayList<Reply> list = bService.getReplyList(bId);
		
		response.setContentType("applicateion/json; charset=UTF-8"); // 인코딩
		
		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd"); // Gson의 날짜 처리 방식이 우리와 달라 내가 원하는 방식으로 바꿔줌
		
//		Gson gson = new Gson();
		Gson gson = gb.create();
		
		try {
			gson.toJson(list, response.getWriter());
			
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Top5 list
	@RequestMapping("topList.bo")
	public void topList(/*@RequestParam("boardId") int bId,*/ HttpServletResponse response) {
		ArrayList<Board> list = bService.topList();
		
		response.setContentType("application/json; charset=UTF-8");
		
		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		
		Gson gson = gb.create();
		
		try {
			gson.toJson(list, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
	
}
