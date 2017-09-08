<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/user.css" rel="stylesheet" type="text/css">
<script src="${pageContext.servletContext.contextPath }/assets/js/jquery/jquery-1.9.0.js" type="text/javascript"></script>
<script>
$( function(){
	$( "#email" ).change( function(){
		$( "#img-check" ).hide();
		$( "#btn-checkemail").show();		
	});
	
	$( "#btn-checkemail" ).click( function(){
		var email = $( "#email" ).val();
		if( email == "" ) {
			return;
		}
		
		//ajax 통신
		$.ajax( {
			url: "${pageContext.servletContext.contextPath }/user?a=checkemail&email=" + email,
			type: "get",
			dataType: "json",
			data:"",
			success: function( response ) {
				if(response.result != "success"){
					console.log( response.message );
					return;
				}
				
				if( response.data == false ) {
					alert( "이미 사용하고 있는 email입니다." );
					$( "#email" ).
					val( "" ).
					focus();
					return;
				}
				
				$( "#img-check" ).show();
				$( "#btn-checkemail").hide();
				
			},
			error: function( xhr, status, e ) {
				console.error( status + ":" + e );
			}
		} );
	});
});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="user">

				<form id="join-form" name="joinForm" method="post" action="${pageContext.servletContext.contextPath }/user/join">
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="">

					<label class="block-label" for="email">이메일</label>
					<input id="email" name="email" type="text" value="">
					<img id="img-check" src="${pageContext.servletContext.contextPath }/assets/images/check-ok.png" style="display:none; width:24px"/>
					<input id="btn-checkemail" type="button" value="중복체크">
					
					<label class="block-label">패스워드</label>
					<input name="password" type="password" value="">
					
					<fieldset>
						<legend>성별</legend>
						<label>여</label> <input type="radio" name="gender" value="female" checked="checked">
						<label>남</label> <input type="radio" name="gender" value="male">
					</fieldset>
					
					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관에 동의합니다.</label>
					</fieldset>
					
					<input type="submit" value="가입하기">
					
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>