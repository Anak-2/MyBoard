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

//		????????? ??? ????????? ????????? ?????? ????????? ???????????? member id??? Map??? ??????
//		getSession(false)??? HttpSession??? ???????????? ????????? null ??????
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
//			??? ?????? ???????????? ??????
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

//	?????????????????? ?????? MultipartRequest ??? ????????? temp????????? ????????? ??????????????? ??????
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
//			MultipartRequest??? ????????? ????????? ????????? ??? ?????? MultipartFile ????????? ?????? ??????
			MultipartFile mFile = multipartRequest.getFile(fileName);

//			????????? ?????? ??????
			String originalFileName = mFile.getOriginalFilename();
			fileList.add(originalFileName);

//			temp ????????? ??? File?????? ?????? (?????? ????????? ??????????????? ???????????? ??? X)
			File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
//			mFile??? ?????? ???
			if (mFile.getSize() != 0) {
//				temp ????????? ????????? ???????????? ?????? ???
				if (!file.exists()) {
//					temp ?????? ??????
					file.getParentFile().mkdirs();
//					????????? ????????? multipartFile??? ?????? ??????(originalFileName)?????? ???????????? ??????
//					temp??? ????????? ????????? request??? ??? ????????? ??? file??? ?????? ??????????????? ????????? ????????? ????????? ???????????????
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
		
//		????????? ????????? ???????????? articleMap??? ??????
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}
		
		
//		???????????? articleMap??? ??????
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
		
//		articleMap ??????
//		Iterator<String> keys = articleMap.keySet().iterator();
//		while(keys.hasNext()) {
//			String strKey = keys.next();
//			Object strValue = articleMap.get(strKey);
//			System.out.println(strKey+" : "+strValue);
//		}  
		

		String articleNO = (String) articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt = null;
//		?????? ????????? ?????? ??????
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			boardService.modArticle(articleMap);
			System.out.println("after modArticle service in try");
			if (!imageFileList.isEmpty()) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					System.out.println(imageFileName);
//					?????? ????????? ?????? ???????????? ?????? temp ????????? srcFile
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
//					srcFile??? destDir??? ????????? ??????, true??? destDir??? ?????? ??? ????????? ???????????????
					if(!destDir.exists() && !destDir.isDirectory()) {						
						FileUtils.moveFileToDirectory(srcFile, destDir, true);
					}

//					?????? ????????? ????????????????????? ?????? ?????? ????????? ??????
//					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName);
//					oldFile.delete();
				}
			}
			message = "<script>";
			message += " alert('?????? ??????????????????.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();
			message = "<script>";
			message += " alert('????????? ??????????????????');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += "</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		return resEnt;
	}
}
