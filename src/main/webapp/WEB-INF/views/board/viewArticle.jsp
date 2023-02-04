<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ page import="com.myspring.pro30.board.BoardVO"
	import="java.util.Map"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<c:set var="article" value="${articleMap.boardVO }" />
<c:set var="imageFileList" value="${articleMap.imageFileList}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${article.title }</title>
<script type="text/javascript">
	
</script>
</head>
<body>
	<!-- print Article Info -->
	<% 
	Map articleMap = (Map)request.getAttribute("articleMap");
	BoardVO article = (BoardVO)articleMap.get("boardVO"); 
	/* out.println(article); */%>
	<!-- print image files -->
	<table>
		<tr>
			<td>글제목:</td>
			<td><input type="text" maxlength="500" name="title"
				value="${article.title }" disabled/></td>
		</tr>
		<tr>
			<td>글쓴이:</td>
			<td><input type="text" maxlength="500" name="title"
				value="${article.id }" disabled/></td>
		</tr>
		<tr>
			<td>글내용:</td>
			<!-- textarea는 value가 아닌 괄호 사이에 넣어줘야 값이 출력됨 -->
			<td><textarea name="content" rows="10" cols="65"
					maxlength="4000" disabled>${article.content }</textarea></td>
		</tr>
		<tr>
			<td>이미지</td>
			<td><c:if
					test="${not empty imageFileList && imageFileList!='null'}">
					<c:forEach var="imageVO" items="${imageFileList }"
						varStatus="status">
						<div>
							이미지${status.count }<br> <img
								src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${imageVO.imageFileName}"
								id="preview" />
						</div>
					</c:forEach>
				</c:if></td>
		</tr>
	</table>
	<div>
		<a href="${contextPath }/board/listArticles.do">글목록</a>
	</div>
</body>
</html>