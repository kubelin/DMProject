window.fbAsyncInit = function() {
	FB.init({ 
		appId: '115724335261385', 	//자신의 app id로 변경, 앱 설정에서 url도 변경
		status: true,
		cookie: true,
		xfbml: true,
		oauth: true
	});
	
	FB.Event.subscribe('auth.statusChange', statusChange );
};

 (function(d){
     var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
     js = d.createElement('script'); 
	 js.id = id; 
	 js.async = true;
     js.src = "//connect.facebook.net/ko_KR/all.js";
     d.getElementsByTagName('head')[0].appendChild(js);
   }(document));



/**
 *   1. 등록된 사용자 인지 Check 
 *   2. if existed user then calling the redirect otherwise add new user.
 */
function statusChange(response){
	
	 if (response.status === 'connected') {
		    var uid = response.authResponse.userID;
		    var accessToken = response.authResponse.accessToken;
			var checkUser = false;
			
			if( checkUser == true ){
			// redirect to real service.
			}else{
			// insert DB	
				insertUserInfo();
			}
			
	  } else if (response.status === 'not_authorized') {
		  	alert('죄송합니다. 허가를 하지 않으면 이용이 불가능 합니다.');
			location.href="http://apps.facebook.com/jonghojoo";
	  } else {
			alert('로그인은 필수 입니다.');
	  }
	
};

/**
 *    get userInfo from facebook
 *    insert userInfo to DB
 */
function insertUserInfo(){
	var user = new Object();
	
	FB.api('/me', function(response) {
		user.id = response.id;
  		user.name = response.name;
		user.gender = response.gender;
		user.birthday = response.birthday;
			
			$.ajax({
				url :"checkUser",
				dataType : 'text',
				type: 'post',
				data : {
					'id' : response.id,
					'name' :  response.name,
					'gender' : response.gender,
					'birthday' : response.birthday
				},
				success: function(data){
					//console.log(data);
					alert( '인증이 성공 하였습니다.');
					location.href = "main";
				},
				error: function(xhr){
					//console.log(xhr);
					alert('인증이 실패 하였습니다.');
					location.href = "http://www.facebook.com";
					//console.log(user);
				}
			});
	});
};

