<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<style>
		#tb{
			margin: auto;
			width: 700px;
		}
	</style>
</head>
<body>
	<c:import url="common/menubar.jsp"/>
	
	<script>
	$(function() {
		var msg = '${msg}';
		
		if(msg != ''){
			alert(msg);
		}
	});
	</script>
	
	<h1 align="center">게시글 TOP5 목록</h1>
	<table id="tb" border="1">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>날짜</th>
				<th>조회수</th>	
				<th>첨부파일</th>
			</tr>
		</thead>
		<tbody></tbody> 
	</table>
	<script>
		function topList(){ // 조회수 기준 top5만 조회
			$.ajax({
				url: 'topList.bo', // 보낼 데이터가 없기때문에 바로 success 작성
				success: function(data){
					console.log(data);
					$tableBody = $('#tb tbody');
					$tableBody.html(''); // 겹칠 수 있기 때문에 작성
					
					for(var i in data){
						var $tr = $('<tr>');
						var $bId = $('<td>').text(data[i].boardId);
						var $title = $('<td>').text(data[i].boardTitle);
						var $writer = $('<td>').text(data[i].nickName);
						var $date = $('<td>').text(data[i].boardCreateDate);
						var $count = $('<td>').text(data[i].boardCount);
						
						if(data[i].originalFileName != null){ // 파일이 존재하면 O 없다면 비워둠 
							$file = $('<td>').text('O');
						} else {
							$file = $('<td>').text('');
						}
						
						$tr.append($bId);
						$tr.append($title);
						$tr.append($writer);
						$tr.append($date);
						$tr.append($count);
						$tr.append($file);
						$tableBody.append($tr);
					}
				},
				error: function(data){
					console.log(data);
				}
			});
		}
		
		$(function(){
			topList();
			
			setInterval(function(){ // 실시간
				topList();
			}, 5000);
		})
	</script>
</body>
</html>
