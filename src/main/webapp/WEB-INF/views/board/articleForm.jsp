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
<title>글쓰기 창</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	var cnt = 1;
	function fn_addFile() {
		/* onclick이 아닌 onchange를 써야 file을 첨부한 뒤에 함수 실행 */
		$("#d_file").append("<input type='file' name=file"+cnt+" onchange='readURL(this)'/>");
		cnt++;
	}
	console.log("${memberInfo.id}");
	/* session에서 isLogOn은 존재하는데 memberInfo만 사라지는 문제 (해결 못했음) */
	<c:if test="${empty isLogOn or empty memberInfo}">
		const loginForm = "${contextPath}/member/loginForm.do";
		alert("로그인이 필요합니다");
		location.href = loginForm +'?action=/board/articleForm.do';
	</c:if>
</script>
</head>
<body>
	<div>작성자: ${memberInfo.name}님</div>
	<form name="articleForm"
		action="${contextPath }/board/addNewArticle.do" method="post"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td>글제목:</td>
				<td><input type="text" maxlength="500" name="title" /></td>
			</tr>
			<tr>
				<td>글내용:</td>
				<td><textarea name="content" rows="10" cols="65"
						maxlength="4000"></textarea></td>
			</tr>
			<tr>
				<td>이미지 파일 첨부</td>
				<td><img id="preview" src="#" width="200" heigth="200" />
			</tr>
			<tr>
				<td><input type="button" value="파일 추가" onClick="fn_addFile()"
					multiple /></td>
				<td><div id="d_file"></div></td>
			</tr>
			<tr>
				<td><input type="submit" value="제출"/></td>
			</tr>
		</table>
	</form>
</body>
</html>