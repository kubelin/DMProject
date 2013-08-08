/**
 * @author james
 */
function changeGenre( value , cb){
		location.href = "/kw/main?genre="+$('#myTab li[class=active]').attr('id')+"&pageNo="+value+"&cB="+cb;	
}

function changeBlock( value ){
	if( value >= 0 ){
		var pageNo = value*10 + 1;
		location.href = "/kw/main?genre="+$('#myTab li[class=active]').attr('id')+"&pageNo="+pageNo+"&cB="+value;	
	}
	else
		value++;
}

function getRecommandedMp3v2(){
		  	 $.ajax({
					url :'recommandMp3v2',
					dataType : 'json',
					type: 'post',
					success: function(data){
						if( data.state == 'fail'){
							var tempDiv = '<div class="span6"><blockquote><p>해당 장르에 추천할 음악이 없습니다. </p><p>장르별로 추천을 완료했는지 확인해주세요. ^^ </p><small>자료가 없을 수도 있습니다..</small></blockquote></div>';
							if( $('#myTabContent2').children('.active').children().children('div') ){
								$('#myTabContent2').children('.active').children().children('div').remove();
							}
							$('#myTabContent2').children('.active').children().append(tempDiv);
							$('#recBtn2').one('click', getRecommandedMp3v2 );
							alert('한개 이상의 장르에 최소 5개 이상의 MP3를 추천해주세요 ^^');
							return ;
						}
						
						if(data.recBalladList.length > 0 ){
							createDynamicRecommandV2(data.recBalladList, 'Ballad', 'recV2Ballad');
						}
						if(data.recDanceList.length > 0){
							createDynamicRecommandV2(data.recDanceList, 'Dance', 'recV2Dance');
						}
						if(data.recRockList.length > 0){
							createDynamicRecommandV2(data.recRockList, 'Rock', 'recV2Rock');
						}
						if(data.recRapList.length > 0){
							createDynamicRecommandV2(data.recRapList, 'Rap', 'recV2Rap');
						}
					},
					error: function(xhr, text, error){
						//console.log(xhr);
						//console.log(text);
						alert('죄송합니다. 추천 할 데이터가 없습니다.');
					}
				}); 
}

// 추천1 
// 리팩토링 필요
function getRecommandedMp3v1(){
		  	 $.ajax({
					url :'recommandMp3v1',
					dataType : 'json',
					type: 'post',
					success: function(data){
						if( data.state == 'fail'){
							var tempDiv = '<div class="span6"><blockquote><p>해당 장르에 추천할 음악이 없습니다. </p><p>장르별로 추천을 완료했는지 확인해주세요. ^^ </p><small>자료가 없을 수도 있습니다..</small></blockquote></div>';
									if( $('#myTabContent1').children('.active').children().children('div') ){
								$('#myTabContent1').children('.active').children().children('div').remove();
							}
							$('#myTabContent1').children('.active').children().append(tempDiv);
							$('#recBtn1').one('click', getRecommandedMp3v1 );
							alert('한개 이상의 장르에 최소 5개 이상의 MP3를 추천해주세요 ^^');
							return ;
						}
						if( data.recBalladList.length == 0 && data.recDanceList.length == 0 && data.recRockList.length == 0 && data.recRapList.length == 0){
							alert('죄송합니다. 일치하는 데이터를 찾을 수 없습니다.( 추천2를 이용해 주세요) ');
						}
						
						//console.log(data);
						//console.log(data.recBalladList);
						//console.log(data.recBalladList[1]);
						
						if(data.recBalladList.length > 0 ){
							createDynamicRecommandV2(data.recBalladList, 'Ballad', 'recV1Ballad');
						}
						if(data.recDanceList.length > 0){
							createDynamicRecommandV2(data.recDanceList, 'Dance', 'recV1Dance');
						}
						if(data.recRockList.length > 0){
							createDynamicRecommandV2(data.recRockList, 'Rock', 'recV1Rock');
						}
						if(data.recRapList.length > 0){
							createDynamicRecommandV2(data.recRockList, 'Rap', 'rec1Rap');
						}
					},
					error: function(xhr, text, error){
						//console.log(xhr);
						//console.log(text);
						alert('죄송합니다. 추천 할 데이터가 없습니다.');
					}
				}); 
}

// maxim을 여기서 정해줄 수 있다.
function createDynamicRecommandV2(valueArray, sort, place){
					var maximum  = 0 ;
					var tempArray = [];
					maximum = valueArray.length ;
					if( valueArray.length > 10){
						maximum = 10;	
					}
					for (var i = 0; i < maximum ; i++) {
							/*tempArray.push('<tr><td>' + (i+1) + '</td>');
							 * tempArray.push('<td>' + valueArray[i].author + '</td>');
								tempArray.push('<td>' + valueArray[i].song_date + '</td>');
								tempArray.push('<td><button class="btn btn-large" onclick="loadMusic(this)">들어보기</button></td></tr>');
							 */
							tempArray.push('<tr><td>' + (i+1) + '</td> <td>' + valueArray[i].title + '</td> <td>' + valueArray[i].author + '</td> <td>' + valueArray[i].song_date + '</td> <td><button class="btn btn-large" onclick="loadMusic(this)">들어보기</button></td></tr>');
						}
						//console.log(tempArray);
						
						if( sort == 'Ballad'){
							$('#' + place).append(tempArray);
						}
						if( sort == 'Dance'){
							$('#' + place).append(tempArray);
						}
						if( sort == 'Rock'){
							$('#' +place).append(tempArray);
						}
						if( sort == 'Rap'){
							$('#' + place).append(tempArray);
						}
						/*= [
								'<tr>',
								'<td>' + (i+1) + '</td>',
								'<td>' + valueArray[i].title + '</td>',
								'<td>' + valueArray[i].author + '</td>',
								'<td>' + valueArray[i].song_date + '</td>',
								'<td><button class="btn btn-large" onclick="loadMusic(this)">들어보기</button></td>',
								'</tr>'
							].join('');
							*/
}
