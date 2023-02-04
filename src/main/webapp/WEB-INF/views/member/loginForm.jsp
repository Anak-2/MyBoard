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
	td {
		text-align: center;
		width: 100px;
	}
</style>
<meta charset="UTF-8">
<title>로그인 창</title>
<c:choose>
	<c:when test="${param.result=='loginFailed' }">
	  <script>
	    window.onload=function(){
	      alert("아이디나 비밀번호가 틀립니다.다시 로그인 하세요!");
	    }
	  </script>
	</c:when>
</c:choose>  
</head>
<body>
	<h3>로그인 창</h3>
	<% String result = (String)request.getParameter("result"); %>
	<c:if test="${param.result == 'loginFailed'}">
		<h3>로그인에 실패했습니다</h3>
	</c:if>
	<%-- MemberController에서 RedirectAttributes.addFlashAttribute()를 사용했을 경우
		URL을 통해 전달하는 GET방식과는 좀 다른 GET 방식이기 때문에 아래와 같이 스크립트릿을 작성해도 괜찮다
	<% String result = request.getAttribute("result"); %>
	<%=result %>
	<c:if test="${result == 'loginFailed'}">
		<h3>로그인에 실패했습니다</h3>
	</c:if> --%>
	<form name="frmLogin" action="${contextPath}/member/login.do"
		method="post">
		<%
		Object action = request.getAttribute("action");
		if(action != null){out.print("로그인 하면 다음으로 이동 "+action);
			%> <input type="hidden" name="action" value="${action}"><%}%>
		<table>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="id"></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="pwd"></td>
			</tr>
		</table>
		<input type="submit" value="로그인" class="submitBtn">
	</form>
</body>
</html>