<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Home</title>
<script src="http://code.jquery.com/jquery-3.6.0.min.js"></script>	
</head>
<body>
	<h1 align="center">Spring에서의 Ajax 사용 </h1>
	
	<button onclick="location.href='test.do'">테스트</button>
	
	<ol>
		<li>
			서버 쪽 컨트롤러로 값 보내기<button id="test1">테스트</button>
			<script>
				$('#test1').on('click', function(){	// .on('click', function(){}); or .click(function(){});
					$.ajax({
						url: 'test1.do', 
						data: {name: '강건강', age: 20}, 
						success: function(data){
							console.log(data);
							if(data.trim() == 'ok'){ // trim() : 보내는 값에 빈 공간이 들어갈 수 있어 trim을 사용하면 제대로 작동함
								alert('전송 성공');
							} else if(data.trim() == 'fail') {
								alert('전송 실패');
							}
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>	
		<li>
			컨트롤러에서 리턴하는 JSON객체 받아서 출력하기<button id="test2">테스트</button>
			<div id="d2"></div>
			<script>
				$('#test2').click(function(){
					$.ajax({
						url: 'test2.do',
						dataType: 'json',  // 객체로 가져오는 방법 1
						success: function(data){
							console.log(data);
							//data = JSON.parse(data);	// 객체로 가져오는 방법 2
							$('#d2').html('번호 : ' + data.no
										 + '<br>제목 : ' + data.title
// 										 + '<br>작성자 : ' + decodeURIComponent(data.writer)
// 										 + '<br>내용 : ' + decodeURIComponent(data.content.replace(/\+/g, ' '))); 
																					// 띄어쓰기 부분이 +로 출력됨 -> replace 작성 (// : 정규표현식, g: 전역 )
																					// 특수 기호를 맨처음 적을 때는 항상 역슬래쉬(\) 부분이 정규식 맨 앞에 존재 해야함
// 										 + '<br>내용 : ' + decodeURIComponent(data.content.replaceAll('+', ' '))); 
										 + '<br>작성자 : ' + data.writer
										 + '<br>내용 : ' + data.content);
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>	
		<li>
			컨트롤러에서 리턴하는 JSON배열을 받아서 출력하기<button id="test3">테스트</button>
			<div id="d3"></div>
			<script>
				$('#test3').on('click', function(){
					$.ajax({
						url: 'test3.do',
						success: function(data){
							console.log(data);
							
							var values = '';
							for(var i in data){
								values += data[i].userId + ", "
										+ data[i].userPwd + ", "
										+ data[i].userName + ", "
										+ data[i].age + ", "
										+ data[i].email + ", "
										+ data[i].phone + "<br>";
							}
							
							$('#d3').html(values);
						}, 
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>
	</ol>
	
	
	
	
	
</body>
</html>
