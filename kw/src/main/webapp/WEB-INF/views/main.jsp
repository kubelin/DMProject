<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>  
<html>
<!-- <meta http-equiv="Content-Type" content="application/json; charset=UTF-8"> -->
<%@include file="inc/css.jsp" %>
<style type="text/css">
	body{
		margin-top: 50px;
	}
	 .float{position:absolute; top:20px; left:40px;}
</style>
<head>
  <title>DM</title>
</head>
<body data-spy="scroll" data-target=".main-sidebar" data-twttr-rendered="true">
	<!--  설문 Floating Div 시작 -->
	<div class="float" align="center">
		<c:choose>
			<c:when test="${isSurvey == 0}">
		        <strong>설문 작성 전 입니다.</strong><br>
		        <button class="btn btn-inverse btn-large" id="surveyBtn">설문 작성</button>
			</c:when>
			<c:otherwise>
					<strong>감사합니다.</strong><br>
			        <button class="btn btn-inverse btn-large disabled" id="">설문 완료</button>
			</c:otherwise>
		</c:choose>
	</div>
	<!--  설문 Floating Div 끝 -->    
<!--  container Start -->
	<div class="container">
		
		<div class="main-sidebar">
			<div class="navbar navbar-fixed-top " id="navbarTop">
			   <div class="navbar-inner">
			     <div class="container">
			       <a class="brand" href="/kw/main">DM MAIN</a>
			       <ul class="nav" id="test">
			       	<li class>
			       		<a href="#first">음악추천</a>
			       	</li>
			       	<li class>
			       		<a href="#second">추천받기1</a>
			       	</li>
		       		<li class>
			       		<a href="#thrid">추천받기2</a>
			       	</li>
			       </ul>
			     </div>
			   </div>
			 </div>
		</div>
			<div class="span12" id="first">
				<h2>
					Music List
				</h2>	
				<p class="lead" style="font-family: monospace;">선호하는 곡을 추천해 주세요. 장르당 최소 <strong>5개</strong>의 추천이 필요합니다. (최대10개)	</p><br>
				<strong>발라드 :</strong> <font color="blue" id="balCount"><c:out value="${countList[0]}" default="0"></c:out></font>개/5개 || 
				<strong>댄스 :</strong> <font color="blue" id="danCount"><c:out value="${countList[1]}" default="0"/></font>개/5개 || 
				<strong>락 :</strong> <font color="blue" id="rockCount"><c:out value="${countList[2]}" default="0"/></font>개/5개 || 
				<strong>랩/힙합 :</strong> <font color="blue" id="rapCount"><c:out value="${countList[3]}" default="0"/></font>개/5개
			</div>
		<div class="bs-docs-example">
			<ul id="myTab" class="nav nav-tabs">
				<li class="active" id="Ballad"><a href="#Ballad" data-toggle="tab" class="Ballad">발라드</a></li>
				<li id="Dance"><a href="#Dance" data-toggle="tab" class="Dance">댄스</a></li>
				<!--  <li id="Soul"><a href="#soul" data-toggle="tab" class="Soul">R&B/소울</a></li>-->
				<li id="Rock"><a href="#rock" data-toggle="tab" class="Rock">락</a></li>
				<li id="Rap"><a href="#rap" data-toggle="tab" class="Rap">랩/힙합</a></li>
				<!--<li><a href="#elec" data-toggle="tab">일렉트로니카</a></li>
				<li><a href="#indi" data-toggle="tab">인디</a></li>
				<li><a href="#blues" data-toggle="tab">블루스/포크</a></li>
				<li><a href="#trot" data-toggle="tab">트로트</a></li>-->
			</ul>
			<div id="myTabContent" class="tab-content">
			<!--  장르  메뉴 시작 -->
				<div class="tab-pane fade in active" id="${genre}">
					<div class="span11">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                  <th class="mp3ListNo">번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                  <th>추천</th>
			                </tr>
			              </thead>
			              <tbody id="normalMp3">	
			                  	<c:if test="${!empty mp3List}">
									<c:forEach var="mp3List" items="${mp3List}" varStatus="count">
										<tr>
											<td><c:out value="${count.count + (pageNo-1)*10}"></c:out></td>
											<td><c:out value="${mp3List.title}"></c:out></td>
											<td><c:out value="${mp3List.author}"></c:out></td>
											<td><c:out value="${mp3List.song_date}"></c:out></td>
											<td><button class="btn btn-large" onclick="loadMusic(this)">들어보기</button></td>
											<c:choose>
												<c:when test="${mp3List.isRecommanded == 1}">
													<td><button class="btn btn-primary btn-large disabled">추천하기</button></td>
												</c:when>
												<c:when test="${mp3List.isRecommanded == 0}">
													<td><button class="btn btn-primary btn-large">추천하기</button></td>
												</c:when>
											</c:choose>
											<td><input type="hidden" id="song_number" value="${mp3List.song_number }"> </td>
											<td><input type="hidden" id="recommended" value="${mp3List.recommended }"> </td>
										</tr>			
									</c:forEach>
								</c:if>
			              </tbody>
			            </table>
			            </div>
			            		<!--  순서 버튼  -->
								<div class="span11" align="center">
									<div class="pagination">
						              <ul id="pageList">
						              			<c:if test="${!empty totalPage}">	
						              				<li><a href="#" onclick='changeBlock("${cB-1}")'>«</a></li>
														<c:forEach begin="${beginPageNo}" end="${totalPage}" step="1"  var="totalPage">
															<c:choose>
																<c:when test="${totalPage == pageNo}">
																 	<li class="active" ><a  href="#" onclick='changeGenre("${totalPage}", "${cB }")' >${totalPage}</a></li>
																 </c:when>
																 <c:otherwise>
																 	<li><a  href="#" onclick='changeGenre("${totalPage}", "${cB }")' >${totalPage}</a></li>
																 </c:otherwise>
															 </c:choose>
														</c:forEach>
													 <li><a href="#" onclick='changeBlock("${cB+1}")'>»</a></li>	
												</c:if>
						               </ul>
					            	</div>
								</div>
								<!--  순서 버튼  끝 -->
		            </div>
			</div>
			<!--  장르  메뉴 종료 -->
		</div>
		
		<!--  추천받은 노래 시작  1 -->
			<div class="span11" id="second">
				<h3 style="font-family: monospace;">
					노래 추천 받기 ( 첫번째 )-> <button class="btn btn-inverse btn-large" href="#" id="recBtn1">Recommand!</button>
				</h3>
			</div>
			<div class="bs-docs-example">
			<ul id="myRecommand" class="nav nav-tabs">
				<li class="active" id="Ballad"><a href="#recBallad1" data-toggle="tab" class="Ballad">발라드</a></li>
				<li id="Dance"><a href="#recDance1" data-toggle="tab" class="Dance">댄스</a></li>
				<li id="Rock"><a href="#recRock1" data-toggle="tab">락</a></li>
				<li id="Rap"><a href="#recRap1" data-toggle="tab">랩/힙합</a></li>
				<!-- <li><a href="#receElec" data-toggle="tab">일렉트로니카</a></li>
				<li><a href="#recIndi" data-toggle="tab">인디</a></li>
				<li><a href="#recBlues" data-toggle="tab">블루스/포크</a></li>
				<li><a href="#recTrot" data-toggle="tab">트로트</a></li> -->
			</ul>
			<div id="myTabContent1" class="tab-content">
				<div class="tab-pane fade in active" id="recBallad1">
					<div class="span10">
			            <table class="table table-hover">
			              <thead id="rec1Head">
			                <tr>
			                  <th>번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV1Ballad">	
			              </tbody>
			            </table>
			          </div>
				</div>
				
				<div class="tab-pane fade" id="recRock1">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV1Rock">	
			              </tbody>
			            </table>
			          </div>
				</div>
				<div class="tab-pane fade" id="recDance1">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV1Dance">	
			              </tbody>
			            </table>
			          </div>
				</div>
				<div class="tab-pane fade" id="recRap1">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV1Rap">	
			              </tbody>
			            </table>
			          </div>
				</div>
			</div>
		</div>
		<!--  추천받은 노래 끝 -->
		
		<!--  추천받은 노래 시작  2 -->
			<div class="span11" id="thrid">
				<h3 style="font-family: monospace;">
					노래 추천 받기 ( 두번째 )-> <button class="btn btn-inverse btn-large" href="#" id="recBtn2">Recommand!</button>
					<h5><font style="font-family: fantasy; color: black;">완료 후, 왼쪽 설문 부탁드립니다.</font></h5>
				</h3>
			</div>
			<div class="bs-docs-example">
			<ul id="myRecommand" class="nav nav-tabs">
				<li class="active" id="Ballad"><a href="#recBallad2" data-toggle="tab" class="Ballad">발라드</a></li>
				<li id="Dance"><a href="#recDance2" data-toggle="tab" class="Dance">댄스</a></li>
				<li id="Rock"><a href="#recRock2" data-toggle="tab">락</a></li>
				<li id="Rap"><a href="#recRap2" data-toggle="tab">랩/힙합</a></li>
				<!-- <li><a href="#receElec" data-toggle="tab">일렉트로니카</a></li>
				<li><a href="#recIndi" data-toggle="tab">인디</a></li>
				<li><a href="#recBlues" data-toggle="tab">블루스/포크</a></li>
				<li><a href="#recTrot" data-toggle="tab">트로트</a></li> -->
			</ul>
			<div id="myTabContent2" class="tab-content" >
				<div class="tab-pane fade in active" id="recBallad2">
					<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                  <th>번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV2Ballad">	
			              </tbody>
			            </table>
			          </div>
				</div>
				
				<div class="tab-pane fade" id="recRock2">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV2Rock">	
			              </tbody>
			            </table>
			          </div>
				</div>
				<div class="tab-pane fade" id="recDance2">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV2Dance">	
			              </tbody>
			            </table>
			          </div>
				</div>
				<div class="tab-pane fade" id="recRap2">
						<div class="span10">
			            <table class="table table-hover">
			              <thead>
			                <tr>
			                   <th >번호</th>
			                  <th>제목</th>
			                  <th>가수</th>
			                  <th>연도</th>
			                  <th>음악듣기</th>
			                </tr>
			              </thead >
			              <tbody id="recV2Rap">	
			              </tbody>
			            </table>
			          </div>
				</div>
			</div>
		</div>
		<!--  추천받은 노래 끝 -->
		
		<!--  추천한 노래 시작 
			<div class="span11">
				<h3>
					추천한 노래
				</h3>
			</div> -->
		<!--  추천한 노래 끝 -->
	</div>
<!--  container End -->	
	
	<!--  소개 팝업 설정  시작 -->
	
		<div id="introModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="myModalLabel">테스트 방법 설명</h3>
			  </div>
			  <div class="modal-body">
				  	<p><strong>본 웹페이지는 맞춤형 음악 추천을 목표로 하고 있습니다.</strong><p>
				  	<p><strong>그럼, 간단하게 음악 추천 및 추천을 받는 방법을 설명해 드리겠습니다 ^-^</strong></p>
				  	<p><span class="label label-info">첫째</span>&nbsp;자신이 좋아하는 음악을 '추천하기' 버튼을 눌러 추천합니다. ( 최소 10개 )
				  	<br><span class="label label-info">둘째</span>&nbsp;첫번째와 두번째 추천(Recommand) 버튼을 클릭합니다.
				  	<br><span class="label label-info">셋째</span>&nbsp;추천된 결과를 확인합니다.
				  	<br><span class="label label-info">넷째</span>&nbsp;너그러운 마음으로 간단한 설문을 작성합니다.  
				  	</p>
				  	<p>※ 음악듣기가 지원이 안되는 노래가 있을 수도 있습니다. <p>
				  	<p>※ 정확도를 높히기 위해 현재 네개의 장르만을 지원합니다. <p>
				  	<p><strong>감사합니다. 좋은 하루 되세요 ^^</strong><p>
			  </div>
			  <div class="modal-footer">
			    <button class="btn" data-dismiss="modal" aria-hidden="true">닫기</button>
			  </div>
			</div>
	
	<!--  소개 팝업 설정 끝 -->
	<!--  설문 조사 팝업 시작  -->
		  <div id="surveyModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="myModalLabel">평가 설문지</h3>
			  </div>
			  <form action="addSurvey" method="post" onsubmit="return valForm()">
			  <div class="modal-body">
				  	<p><strong>첫번째 추천은 어떤가요?</strong><p>
				    <label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="7">매우 만족</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="6">만족</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="5">조금 만족</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="4" checked="checked">보통</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="3">조금 불만족</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="2">불만족</label>
					<label class="radio1"><input type="radio" name="recommand_v1" id="optionsRadios1" value="1">매우 불만족</label>
				  	<p><strong>두번째 추천은 어떤가요?</strong><p>
				    <label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="7" >매우 만족</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="6" >만족</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="5" >조금 만족</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="4" checked="checked">보통</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="3" >조금 불만족</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="2" >불만족</label>
					<label class="radio"><input type="radio" name="recommand_v2" id="optionsRadios2" value="1" >매우 불만족</label>
					<p><strong>하고 싶은 말이 있으신가요?</strong><p>
					<textarea rows="5" cols="5" class="input-xlarge" name="etc" id="surveyText"></textarea>
			  </div>
			  <div class="modal-footer">
			    <button class="btn" data-dismiss="modal" aria-hidden="true">닫기</button>
			    <input type="submit" class="btn btn-primary" value="설문 완료" />
			  </div>
			  </form>
			</div>
	<!--  설문 조사 팝업 끝  -->
	<%@include file="inc/js.jsp" %>
	<script type="text/javascript" src="resources/js/main.js"></script>
	<script type="text/javascript">
		/* 설문조사 validation form
		function valForm(){
			console.log($('.radio1'));
			console.log($('.radio1 :radio'));
			console.log($('.radio1 :radio')[6]);
			console.log($('label').find('.radio1'));
			console.log($('label .radio1').attr('checked'));
			return false;
		}
		*/
	
		// carete Query using yql
		function createQuery(author, title){
			var tempQuery = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from json where url="http:\/\/tinysong.com\/b\/' + author + '+' + title +'?format=json&key=' ) + '5e773ab2f8102c04d59044fb31362441"&format=json&callback=?';
			//var tempQuery = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from json where url="http://tinysong.com/b/' + author + '+' + title +'?format=json') +'&' + encodeURIComponent('key=5e773ab2f8102c04d59044fb31362441"') + '&format=json&callback=?';
			return tempQuery;
		}
		
		// tap's active change
		$('#myTab li').click(function(){
			var tempGenre = $(this).find('a').attr('class');
			window.location = "main?genre="+ tempGenre;
		});
		
		// load dynamic embeddedMp3
		function loadMusic(data){
			
			/*console.log($(this).parents().parent());
			console.log(data);
			console.log($(data));
			console.log($(this).parents(':eq(1)'));
			console.log($(data).parents(':eq(1)').children()[2]);
			*/
			var tempLoc = $(data).parent();
			var tempLocation = $(data); 
			
			//공백 유니코드 대체
			//console.log($(data).parents(':eq(1)').children()[1].innerHTML.replace(/\s/g,"%20"));
			// select td's data			
			var test = createQuery( $(data).parents(':eq(1)').children()[1].innerHTML.replace(/\s/g,"%20") , $(data).parents(':eq(1)').children()[2].innerHTML.replace(/\s/g,"%20") );
	 		//console.log(test);
	 		// 더블 클릭 방지
	 		tempLocation.remove();
	 		// get SongId
		 	  $.getJSON( test, function(data) {
		 			  //console.log(data);
		 		  	  if( data.query.results == 'undefined' || data.query.results == null){
		 		  		  alert('죄송합니다. 해당곡은 아직 지원하지 않습니다.');
		 		  		  return;
		 		  	  };
		 		  	  //console.log(data.query.results.json.SongID);
		 			  	
			          createEmbeddedMp3(data.query.results.json.SongID, tempLocation,tempLoc);
			          return false;
			    } 
			); 
		}
		
		// dynamic embeddedMp3
		function createEmbeddedMp3(songId, tempLocation, tempLoc){
			var tempDiv = [];
			tempDiv.push('<embed src="http://grooveshark.com/songWidget.swf" type="application/x-shockwave-flash" width="200" height="40" flashvars="hostname=cowbell.grooveshark.com&amp;songIDs='+songId+'&amp;style=metal&amp;p=0" allowscriptaccess="always" wmode="window"></object>');
			
			//tempLocation.remove();
			$(tempLoc).append(tempDiv);
		}
		
		// recommand Music
		function recommandMusic(data){
			
			if( $(this).attr('class') == 'btn btn-primary btn-large' ){
				$(this).attr('class','btn btn-primary btn-large disabled');
				
				var songNumber =$(this).parents(':eq(1)').find('#song_number').val();
				var tabGenre = $(this).parents(':eq(5)').attr('id');
				// Dynamic add to DB
				// final 로 switch 문 제작 필요
			  	 $.ajax({
					url :"recommandMp3",
					dataType : 'json',
					type: 'post',
					data : {
						'songNumber' : songNumber,
						'tabGenre' : tabGenre
					},
					beforeSend : function(){
						//alert('before');
					},
					success: function(data){
						
						if(data.state=='fail'){
							alert( '세션이 끊겼습니다');
							location.href = "apps.facebook.com/jonghojoo";
						}
						if( data.state == 'over'){
							alert(data.genre+'에서 10개 이상 추천하셨습니다.');
							return ;
						}
							//console.log(data);
							// 색 변경. 노가다 작업이므로..
							//if( data.countList[0] >= 5 ){};
							if( data.genre == 'Ballad' ){
								$('#balCount').text(data.count);	
							}else if( data.genre == 'Dance' ){
								$('#danCount').text(data.count);
							}else if( data.genre == 'Rock' ){
								$('#rockCount').text(data.count);
							}else if( data.genre == 'Rap' ){
								$('#rapCount').text(data.count);
							}
							// 버튼 변경
							alert('추천하였습니다');
						
					},
					error: function(xhr, text, error){
						//console.log(text);
						alert('인증이 실패 하였습니다.');
						location.href = "http://www.facebook.com";
					}
				}); 
			}
		}
		
		
		
		// HTML onload 
		$(function(){
			
			// these event will occur only one time
			$('#normalMp3 tr td:nth-child(6) button').one('click', recommandMusic);
			$('#surveyBtn').on('click', excuteSurvey);
			//$('#normalMp3 tr td:nth-child(5) button').one('click', loadMusic() );
			// Dynamic call in order to recommand Mp3 to user
			$('#recBtn1').one('click', getRecommandedMp3v1 );
			$('#recBtn2').one('click', getRecommandedMp3v2 );
			/**
					scrollspy start
			**/
			$('#navbarTop').scrollspy();
				/*$('[data-spy="scroll"]').each(function(){
				var $spy = $(this).scrollspy('refresh');
				alert('test');
				});  */
			$('[data-spy="scroll"]').each(function(){
				var $spy = $(this).scrollspy('process');
			});
			
			/*
					show surveyModal
			*/
			function excuteSurvey(){
				$('#surveyModal').modal({
					backdrop : false,
					show : true
				});
				
			}
			
			// URU Parsing function
			function getQueryString() {
			    var o = {};

			    var q = location.search.substring(1);
			    if (q) {
			        // 실제 그룹화 정규식.
			        var vg = /([^&=]+)=?([^&]*)/g;
			        // 인코딩된 공백문자열을 다시 공백으로
			        var sp = /\+/g;
			        // 정규식을 사용하여 값을 추출
			        var decode = function(s) {
			            if (!s) {
			                return '';
			            }
			            return decodeURIComponent(s.replace(sp, " "));
			        };
			       // 한번씩 exec를 실행하여 값을 받아온다.
			        var tmp; 
			        while (tmp = vg.exec(q)) {
			            (function() {
			                var k = decode(tmp[1]);
			                var v = decode(tmp[2]);
			                var c;
			                if (k) {
			                    o[k] = v;
			                    // getXXX 형식의 자바빈 타입으로 사용하고 싶다면
			                    // 윗 라인을 지우고, 아래와 같이 하면 됩니다.
			                    //c = k.charAt(0).toUpperCase() + k.slice(1);
			                    //o["get" + c] = function() { return v; }
			                    //o["set" + c] = function(val) { v = val; }
			                }
			            })();
			        }
			    }
			    return o;
			};
			
			// 쿼리 파싱
			var tempQueryString = getQueryString();
			// modal setting.
			if( tempQueryString.genre == null){
				$('#introModal').modal().one();
			}
			// 탭 CSS 변경 
			if( tempQueryString.genre != null){
				$('#myTab li').removeClass('active');
			  	$('#myTab li[id='+tempQueryString.genre+']').addClass('active');	
			}
		
		});
		  
		// 왼쪽에 띄워줄 absolute floating div
		
		var currentPosition = parseInt( $(".float").css("top"));
             $(window).scroll(function()
		 {
			var position = $(window).scrollTop() + 100;
			$(".float").stop().animate
				({"top":position+currentPosition+"px"},1000);
		  });
	</script>
</body>
</html>

<!-- 
	Ajax Cross-Domain Practice
		/* 	 $.ajaxPrefilter('json', function(options, orig, jqXHR) {
				 	alert('여길?')
			        return 'json';
			    });   
			
	 		  $.ajax({	
			  headers: {
					        "Content-Type": "text/html",
					        "Charset" : "utf-8",
			 			  	 }, 
				url :"http://tinysong.com/b/소녀시대+Gee",
				data: {
							'format' : 'json',
							'key' : '5e773ab2f8102c04d59044fb31362441',
							},
				async: false,
		        cache: false,
		        contentType: "application/json;",
		        dataType: "json",
		        crossDomain: true,
				jsonp: false,
				type: 'GET',
				success: function(data){
					console.log(data);
					alert( '인증이 성공 하였습니다.');
				},
				error: function(jqXHR, textStatus, errorThrown){
					console.log(jqXHR);
					console.log(jqXHR.success);
					console.log(jqXHR.url);
					console.log(jqXHR.statusCode.cacheURL);
					alert('인증이 실패 하였습니다.' + errorThrown + 'dddd' + textStatus);
				},
				getResponseHeader: function(){
					alert(' ddd');
				}
			});  */

 -->
 