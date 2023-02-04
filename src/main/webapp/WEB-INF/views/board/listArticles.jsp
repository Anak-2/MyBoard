<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<style>
.cls1 {
	text-decoration: none;
}

.cls2 {
	text-align: center;
	font-size: 30px;
}
</style>
<meta charset="UTF-8">
<title>글목록</title>
<script>
	function fn_articleForm(isLogOn, articleForm, loginForm) {
		if (isLogOn != '' && isLogOn != 'false') {
			location.href = articleForm;
		} else {
			alert("로그인 후 글쓰기가 가능합니다.")
			location.href = loginForm + '?action=/board/articleForm.do';
		}
	}
	console.log("${isLogOn}");
	console.log("${memberInfo}")
</script>
</head>
<body>
	<c:if test="${isLogOn == '' || isLogOn == 'false' || isLogOn == null}">
		<a href="${contextPath }/member/loginForm.do">로그인</a>
	</c:if>
	<table align="center" border="1">
		<tr bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일</td>
			<c:if test="${isLogOn }"><td>글수정</td></c:if>
		</tr>
		<c:choose>
			<c:when test="${articlesList  == null}">
				<tr>
					<td colspan="4">
						<p align="center">등록된 글이 없습니다</p>
					</td>
				</tr>
			</c:when>
			<c:when test="${articlesList != null}">
				<c:forEach var="article" items="${articlesList }"
					varStatus="articleNum">
					<tr>
						<td>${articleNum.count }</td>
						<td>${article.id }</td>
						<td><c:choose>
								<c:when test="${article.level  > 1}">
									<c:forEach begin="2" end="${article.level }" step="1">
										<span style="padding-left: 20px"></span>
									</c:forEach>
									<span style="font-size: 12px;">[답변]</span>
									<a class="cls1"
										href="${contextPath }/board/viewArticle.do?articleNO=${article.articleNO}">${article.title }</a>
								</c:when>
								<c:otherwise>
									<a class='cls1'
										href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title }</a>
								</c:otherwise>
							</c:choose></td>
						<td>${article.writeDate }</td>
						<c:if test="${article.id  == memberInfo.id }">
							<td><a href="${contextPath }/board/modArticleForm.do?articleNO=${article.articleNO}">수정하기</a></td>
						</c:if>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<a class="cls1"
		href="javascript:fn_articleForm('${isLogOn}','${contextPath}/board/articleForm.do', 
                                                    '${contextPath}/member/loginForm.do')">
		<p class="cls2">글쓰기</p>
	</a>
</body>
</html>