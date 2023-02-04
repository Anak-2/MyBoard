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
<title>회원가입 창</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<!-- 외부 script 파일을 읽어오는게 아니라면 defer 속성은 쓸모가 없다!!!! defer 대신 aysnc=false를 이용해주자 -->
</head>
<body>
	<h3>회원가입</h3>
	<form name="frmSignUp" action="${contextPath }/member/addMember.do"
		method="post">
		<table>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="id" class="idBox" required></td>
				<td><input type="checkbox" class="idDupCheck" name="idDupCheck">
					<label for="idDupCheck">ID중복 확인</label></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="pwd" required></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" required></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><input type="text" name="email" required></td>
			</tr>
		</table>
		<input type="submit" value="가입하기">
	</form>

	<script type="text/javascript">
		var checkBox = $(".idDupCheck");
		var idCheck = false;
		/*  -------------비동기 방식으로 처리하고 싶은 경우 ResponseBody 어노테이션과 AJAX 이용!! */
		function idDupCheck() {
			console.log("Call idDupCheck ajax")
			var idBox = $(".idBox");
			var _id = idBox.val();
			console.log("_id: "+_id);
			if(_id == "" || _id == undefined){
				alert("ID를 입력해주세요!")
				/* jQeury의 checkBox 속성 접근하는 법 */
				checkBox.prop("checked",false);
				return;
			}
			var memberId = {id: _id};
			console.log(typeof(memberId));
			$.ajax({
				type : "post",
				async : "false",
				/* response의 dataType, 여기선 String */
				dataType: "text",
				contentType : "application/json; charset=UTF-8",
				url : "http://localhost:8090/pro30/member/idDupCheck.do",
				/* memberId를 JSON객체로 만들어서 서버에 전달 */
				data : JSON.stringify(memberId),
				success : function(data, textStatus) {
					if (data == "unavailable") {
						idCheck = false;
						alert("사용 불가능한 아이디입니다!");
						checkBox.prop("checked",false);
						idBox.css({
							"borderColor":"red"
						});
						console.log(typeof(data));
						console.log("사용 불가능한 아이디")
					} else {
						idCheck = true;
						checkBox.prop("disabled", true);
						idBox.prop("disabled", true)
						idBox.css({
							"color":"green",
							"borderColor":"lightGreen"
						});
						console.log(typeof(data));
						console.log("사용 가능한 아이디")
					}
				},
				error : function(data, textStatus) {
					checkBox.prop("checked",false);
					console.log("오류");
				}
			});
		}
		checkBox.on("click", idDupCheck);
		
		$("form").on('submit', function(event) {
			event.preventDefault();
			if(idCheck){
				console.log("pass");
				/* disabled인 요소는 submit할 때 전달되지 않는다 */
				$(".idBox").prop("disabled", false);
				this.submit();
			}
			else{
				console.log("fail");
				alert("ID중복을 체크해주세요!")
			}
		});
	</script>
</body>
</html>
