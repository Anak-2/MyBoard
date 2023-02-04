package com.myspring.pro30.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileDownloadController {
	private static final String ARTICLE_IMAGE_REPO = "D:\\BackendStudy\\img";
	@RequestMapping("/download.do")
	protected void download(@RequestParam("imageFileName") String imageFileName,
			@RequestParam("articleNO") String articleNO,
			HttpServletResponse response) throws Exception{
//		download file from local directory 
//		(specify the image file to be downloaded through the articleNO)
		String downFilePath = ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+imageFileName;
		File file = new File(downFilePath);
		
//		write image file to an OutputStream as a buffer
		OutputStream out = response.getOutputStream();
		response.setHeader("Cache-Control", "no-cache");
//		Http response 헤더(패킷)에 body에서 첨부된 파일을 다운로드 받으라는 설정
		response.addHeader("Content-disposition", "attachment; fileName="+imageFileName);
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024*8];
		while(true) {
//			read the file as the buffer size
			int count = in.read(buffer);
			if(count == -1)
				break;
//			어디다 써지는가? HTTP의 요청한 body | out.wrtie() - 0부터 시작해서 count까지 buffer에서 읽어서 쓰라는 함수
			out.write(buffer,0,count);
		}
		in.close();
		out.close();
	}
}
