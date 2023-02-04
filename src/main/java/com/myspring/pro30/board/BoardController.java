package com.myspring.pro30.board;

import java.io.File;
import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.BoardService;
import com.myspring.pro30.board.BoardVO;
import com.myspring.pro30.member.MemberVO;
import com.myspring.pro30.board.ImageVO;;

@Controller("boardController")
@RequestMapping(value = "/board")
public class BoardController {
	private static final String ARTICLE_IMAGE_REPO = "D:\\BackendStudy\\img";
	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardVO boardVO;

	@RequestMapping(value = "/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView("board/listArticles");
		mav.addObject("articlesList", articlesList);
		return mav;
	}

	@RequestMapping(value = "/articleForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView articleForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("board/articleForm");
		return mav;
	}

	@RequestMapping(value = "/addNewArticle.do", method = RequestMethod.POST)
	public ModelAndView addNewArticle(HttpServletRequest request, MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		String imageFileName = null;

		Map articleMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

//		로그인 시 세션에 저장된 회원 정보를 이용하여 member id를 Map에 저장
//		getSession(false)는 HttpSession이 존재하지 않으면 null 반환
		HttpSession session = request.getSession(false);
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String id = memberVO.getId();
		articleMap.put("id", id);
		articleMap.put("parentNO", 0);

		List<String> fileList = upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		if (fileList != null && fileList.size() != 0) {
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
//				set imageFileName through URL Encoder
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList);
		}
		String message;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
//			새 글을 추가하는 모델
			int articleNO = boardService.addNewArticle(articleMap);
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
//					move(not copy) file to the directory. If there is no dest directory, make directory and then move
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
		return mav;
	}

//	사용자에게서 받은 MultipartRequest 중 파일을 temp폴더에 임시로 업로드하는 함수
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
//			MultipartRequest로 들어온 파일을 처리할 수 있는 MultipartFile 객체로 파일 받기
			MultipartFile mFile = multipartRequest.getFile(fileName);

//			파일의 원래 이름
			String originalFileName = mFile.getOriginalFilename();
			fileList.add(originalFileName);

//			temp 폴더에 새 File객체 생성 (실제 경로에 디렉토리를 생성하는 것 X)
			File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
//			mFile이 있을 때
			if (mFile.getSize() != 0) {
//				temp 폴더에 파일이 존재하지 않을 때
				if (!file.exists()) {
//					temp 폴더 생성
					file.getParentFile().mkdirs();
//					임시로 저장된 multipartFile을 실제 파일(originalFileName)으로 만들어서 전송
//					temp에 저장한 이유는 request가 다 처리된 후 file을 실제 업로드해서 사용할 폴더에 보내기 위해서이다
					mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName));
				}
			}
		}
		return fileList;
	}

	@RequestMapping(value = "/viewArticle.do", method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("board/viewArticle");
		Map articleMap = boardService.viewArticle(articleNO);
		mav.addObject("articleMap", articleMap);
		return mav;
	}
	
	@RequestMapping(value = "/modArticleForm.do", method = RequestMethod.GET)
	public ModelAndView modArticleForm(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("board/modArticle");
		Map articleMap = boardService.viewArticle(articleNO);
		mav.addObject("articleMap", articleMap);
		return mav;
	}

	@RequestMapping(value = "/modArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		Map<String, Object> articleMap = null;
		
		String imageFileName = null;
		multipartRequest.setCharacterEncoding("utf-8");
		articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		
//		파일을 제외한 입력값들 articleMap에 추가
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}
		
		
//		파일들을 articleMap에 추가
		List<String> fileList = upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<>();
		if (fileList != null && fileList.size() != 0) {
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
//				System.out.println("fileName: "+fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList);
		}
		
//		articleMap 출력
//		Iterator<String> keys = articleMap.keySet().iterator();
//		while(keys.hasNext()) {
//			String strKey = keys.next();
//			Object strValue = articleMap.get(strKey);
//			System.out.println(strKey+" : "+strValue);
//		}  
		

		String articleNO = (String) articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt = null;
//		응답 헤더에 정보 추가
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			boardService.modArticle(articleMap);
			System.out.println("after modArticle service in try");
			if (!imageFileList.isEmpty()) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					System.out.println(imageFileName);
//					미리 업로드 해둔 파일들이 있는 temp 폴더가 srcFile
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
//					srcFile을 destDir로 보내는 함수, true면 destDir가 없을 때 폴더를 생성해준다
					if(!destDir.exists() && !destDir.isDirectory()) {						
						FileUtils.moveFileToDirectory(srcFile, destDir, true);
					}

//					새로 파일을 업로드했으므로 원래 있던 파일은 삭제
//					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName);
//					oldFile.delete();
				}
			}
			message = "<script>";
			message += " alert('글을 수정했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();
			message = "<script>";
			message += " alert('오류가 발생했습니다');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += "</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		return resEnt;
	}
}
