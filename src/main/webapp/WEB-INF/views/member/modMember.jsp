<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<%
  request.setCharacterEncoding("UTF-8");
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
</head>
<body>
	<form method="post" action="${contextPath }/member/modMember.do">
		<h1>회원 정보 수정창</h1>
		<table>
			<tr>
				<td>아이디</td>
				<td><input type="text" value="${member.id }" name="id" readonly/></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="text" value="${member.pwd }" name="pwd"/></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" value="${member.name }" name="name"/></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><input type="text" value="${member.email }" name="email"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="수정하기"/></td>
			</tr>
		</table>
	</form>
</body>
</html>