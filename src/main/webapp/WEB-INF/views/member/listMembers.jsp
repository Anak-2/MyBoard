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
<meta charset="UTF-8">
<title>회원 목록창</title>
<script type="text/javascript">
	<c:if test="${not empty message}">
		window.onload = function(){
			alert("${message}");
		}
	</c:if>
</script>
</head>
<body>
	<h3>회원 목록</h3>
	<table border="1" align="center" width="80%">
		<tr align="center" bgcolor="lightgreen">
			<td><b>아이디</b></td>
			<td><b>비밀번호</b></td>
			<td><b>이름</b></td>
			<td><b>이메일</b></td>
			<td><b>가입일</b></td>
			<td><b>삭제</b></td>
			<c:if test="${isLogOn }">
				<td><b>회원정보 수정</b></td>
			</c:if>
		</tr>
		<c:forEach var="member" items="${membersList }">
			<tr>
				<td><c:out value="${member.id }" /></td>
				<td><c:out value="${member.pwd }" /></td>
				<td><c:out value="${member.name }" /></td>
				<td><c:out value="${member.email }" /></td>
				<td><c:out value="${member.joinDate }" /></td>
				<td><a
					href="${contextPath}/member/removeMember.do?id=${member.id }">삭제하기</a>
				</td>
				<c:if test="${memberInfo.id == member.id}">
					<td align="center"><a href="${contextPath }/member/modMemberForm.do">수정하기</a></td>
				</c:if>
			</tr>
		</c:forEach>
		<tr><a href="${contextPath }/board/listArticles.do">글목록</a></tr>
	</table>
</body>
</html>