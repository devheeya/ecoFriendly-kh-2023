<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang='en'>
  <head>
    <meta charset='utf-8' />
     <!-- css -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

<!-- javascript -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.9/index.global.min.js'></script>

<!-- overal -->
<style>
    #wrapper{
		border : 1px solid teal;
        width: 1200px;
        height: 1000px; 
       margin: auto;
        background-color: white;
    }


    #content-area{

       border : 1px solid teal;
        height : 75%; 
        width : 100%;
    }

</style>


 	
 	
    

    <script>

      document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
	
	          initialView: 'dayGridMonth',
	          
	          //expandRows: true, // 화면에 맞게 높이 재설정
	          //selectable: true,    // 영역 선택
	          editable: true,      // event(일정) 
	          //dayMaxEventRows: true,  // Row 높이보다 많으면 +숫자 more 링크 보임!
          
          
          
			  events: [
			 		<c:if test="${ not empty list}">
						<c:forEach var="e" items="${list}">
							{ 
								id : ${e.eventNo},
								title: '${e.eventTitle}', // text는 ''로 감싸줌, 아니면 변수로 인식함
								start: '${e.uploadDate}',// Date는 Date(sql)인데 왜 ''로 감싸야할까
								imageurl : '${e.changeName}',
								 extendedProps: {
									 //content : ${e.eventContent},
									 place : '${e.eventPlace}',
									 //participant : ${e.participants},
									 categoryNo : ${e.categoryNo},
								 },
	
							},
						</c:forEach>
					</c:if> 
				],
				 eventDidMount: function(info) {
					    console.log(info.event.extendedProps.imageurl);
					  
				 },
				 eventContent: function (arg) {

			            var event = arg.event;
			            
			            var customHtml = '';
			            
			            customHtml += "<div class='r10 font-xxs font-bold' style='overflow: hidden;'>" + event.title + "</div>";
			            
			            customHtml += "<div class='r10 highlighted-badge font-xxs font-bold'>" + event.extendedProps.place +  "</div>";
			                        
			            customHtml += "<div class='r10 highlighted-badge font-xxs font-bold'>" + event.extendedProps.categoryNo +  "</div>";
			            
			            customHtml += "<img  style='width:100px; height : 100px;' src=" + event.extendedProps.imageurl +  "/>";
			            
			            return { html: customHtml }
			        }
			
				/* ver5부터 안 쓰임 
					eventRender: function(event, eventElement) {
					    if (event.imageurl) {
					        eventElement.find("div.fc-content").prepend("<img src='" + event.imageurl +"' width='10' height='10'>");
						}
					}*/

		  
		})// var calendar
		calendar.render();
     })//DOMContentLoaded
    </script>
  </head>
  <body>
  
  <jsp:include page="../common/header.jsp" />
  
 	<div id="wrapper">
   		<div id='calendar'></div>
    </div>
  </body>
</html>

		
