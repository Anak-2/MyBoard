package com.myspring.pro30.board;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.BoardDAO;
import com.myspring.pro30.board.BoardVO;
import com.myspring.pro30.board.ImageVO;

@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardService {
	@Autowired
	private BoardDAO boardDAO;
	
	public List<BoardVO> listArticles() throws Exception{
		List<BoardVO> articlesList = boardDAO.selectAllArticlesList();
		return articlesList;
	}
	
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap);
		articleMap.put("articleNO", articleNO);
		if(articleMap.get("imageFileList") != null) {
//			System.out.println("YES IMAGE ATTACHMENT");
			boardDAO.insertNewImage(articleMap);
		}
		else {
//			System.out.println("NO IMAGE ATTACHMENT");
		}
		return articleNO;
	}

	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		BoardVO boardVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		articleMap.put("boardVO",boardVO);
		articleMap.put("imageFileList",imageFileList);
		return articleMap;
	}

	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateArticle(articleMap);
//		기존 이미지는 submit할 때 name을 안 줬기 때문에 안 받아옴
//		boardDAO.updateImage((ArrayList)articleMap.get("imageFileList"));
		if(articleMap.get("imageFileList") != null) {
			System.out.println("YES IMAGE ATTACHMENT");
			List<ImageVO> imageFileList = new ArrayList<>();
			imageFileList = (List<ImageVO>) articleMap.get("imageFileList");
			System.out.println(articleMap.get("articleNO"));
			for(ImageVO image : imageFileList) {
				System.out.println("imageFileName: "+image.getImageFileName());
			}
			boardDAO.insertNewImage(articleMap);
		}
	}
}
