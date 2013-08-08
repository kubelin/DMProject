<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML> 
<html>
<%@include file="inc/css.jsp" %>

<script src="resources/js/fs_init.js"></script>
 <div id="fb-root"></div> <!---->
<head>
	<title>Home</title>
</head>
<body >
<!--  container Start -->
	<div class="container">
			<div class="span11 offset1">
				<h2> Welcome ! :) </h2><button class="btn btn-large"><i class="icon-spinner icon-4x icon-spin"></i> 인증 중...</button> 
				<br>
			</div>
				<div class="span11 offset1"> 
					  <h4>본 웹페이지는 졸업 작품을 위한 데이터 수집에 목표가 있으며,</h5>
				      <h5>FACEBOOK LOGIN 및 허가와 동시에 사용자의 기본정보가 저장됨을 알려드립니다.</h5>
				      <address>
				        <strong>KwangWoon Uiv.</strong><br>
				        <abbr title="Phone">N:</abbr> 주종호<br>
				        <abbr title="Phone">P:</abbr> (010) 6799-8765
				      </address>
				      <address>
				        <strong>Contact me</strong><br>
				        <a href="mailto:#">jongho8765@gmail.com</a>
				      </address>
				      <form class="bs-docs-example form-horizontal" action="/kw/main">
		                <button type="submit" class="btn">Skip</button>
         		 	  </form>
			    </div>
				
	</div>
<!--  container End -->	
	
	
	
	<%@include file="inc/js.jsp" %>
</body>
</html>
