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
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js'></script>

<!-- overal -->
<style>
    #wrapper{
		border : 1px solid teal;
        width: 1200px;
        height: 1000px; 
        margin: auto;
        background-color: white;
        padding-top: 50px;
    }

    #content-area{
        border : 1px solid teal;
        height : 75%; 
        width : 100%;
    }
</style>
</head>
<body>
  
    <jsp:include page="../common/header.jsp" /> 
  
 	<div id="wrapper">

		<!-- insertModal -->
		<div class="modal fade" id="insertModal" data-backdrop="static" data-keyboard="false" tabindex="-2" aria-labelledby="staticBackdropLabel" aria-hidden="true">

				   <div class="modal-dialog">
				    <div class="modal-content">
				    
				      <div class="modal-header">
				        <h5 class="modal-title" id="staticBackdropLabel">이벤트 등록하기</h5>
				      </div>
				      
				      
				      	<div id="insert-form" class="modal-body" >

 							<input type="hidden" class="event" id="activityNo" name="activityNo" value=""  />
					       	<input type="hidden" class="event" id="eventDate" name="eventDate" value=""  />
					       	<label for="eventTitle">이벤트명 : <input id="eventTitle" name="eventTitle" class="event"  type="text" required/></label>
					        <label for="eventContent">이벤트내용 : <input id="eventContent" name="eventContent"  	class="event"  type="text" required/></label>
					        <label for="eventPlace">이벤트장소 : <input id="eventPlace" name="eventPlace"   class="event"  type="text" required/></label>
					       	<label for="upfile">첨부파일 : <input id="upfile" name="upfile" type="file"   class="event"accept=".jpg, .jpeg, .png" required/></label>
				       		<label for="categoryNo">카테고리 : <input id="categoryNo" name="categoryNo" 	 class="event"  type="number" required/></label>

					      <div class="modal-footer">
					        <button class="btn btn-primary" id="insertEvent">등록</button>
					        <button class="btn btn-secondary" data-dismiss="modal">취소</button>
					      </div>
					      
						</div><!-- body -->
					
					
				</div>   <!-- content -->
			</div><!-- dialog -->
			
		</div><!-- insertModal -->

		<!--detailModal -->
		<div class="modal fade" id="detailModal" data-backdrop="static" data-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">

				   <div class="modal-dialog">
				    <div class="modal-content">
				    
				      <div class="modal-header">
				        <h5 class="modal-title" id="staticBackdropLabel"></h5>
				      </div>

				      	<div id="modal-body" class="modal-body">
						
					       	<div id="detail-title">
							       	<div>이벤트명&nbsp;&nbsp;&nbsp;    : 	</div>
							       	<div>카테고리&nbsp;&nbsp;&nbsp;    : 	</div>
							       	<div>이벤트내용 : 	</div>
							       	<!-- <div>이벤트시간 : 	</div> -->
							       	<div>이벤트장소 : 	</div>
							       	<div>참여인원&nbsp;&nbsp;&nbsp;    : 	</div>
							       	<div>첨부파일&nbsp;&nbsp;&nbsp;    : 	</div>
					       	</div>
					       	
					       	<div id="detail-content">
							       	<div id="dEventTitle"> </div>
							       	<div id="dCategoryNo"> </div>
							       	<div id="dEventContent"> </div>
							       	<!-- <div id="mEventDate"> </div> -->							       	
 									<div id="dEventPlace"> </div>
							       	<div id="dParticipants"> </div>
							       	<div id="dUpfile"> </div>
							       	<div id="dActivityNo"></div>
					       	</div>

						</div><!-- body -->
	
							 <div class="modal-footer">
							 	<c:choose>
								 	<c:when test="${ loginUser.userNo == 1 }"> 
							        	<button id="updateForm" type="submit" class="btn btn-primary">수정</button>
							        </c:when>
							        <c:otherwise>
							        	<button id="joinEvent" type="submit" class="btn btn-primary">참가</button>
							        </c:otherwise> 
						        </c:choose>
						        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
						      </div>
	 	
				</div>   <!-- content -->
			</div><!-- dialog -->
			
		</div><!-- detailModal -->


		<div id="calendar"></div>
		
</div><!-- wrapper -->

<style>
	#modal-body{
	border : 1px solid teal;
		display : flex;
		width : 100%;/...
		justify-content;
	}

	#mEventNo{
	display : none; 
	}

</style>

<script>
      document.addEventListener('DOMContentLoaded', function() {
	        var calendarEl = document.getElementById('calendar');
	        var calendar = new FullCalendar.Calendar(calendarEl, {
		          initialView: 'dayGridMonth',
		          editable: true,   
		          events: [
				 		<c:if test="${ not empty list}">
							<c:forEach var="e" items="${list}">
								{ 
									id : '${e.activityNo}',

									title: '${e.eventTitle}', 

									start: '${e.eventDate}',
									 extendedProps: {
										 place : '${e.eventPlace}',
										 participants : ${e.participants},
										 categoryNo : ${e.categoryNo},
										 content : '${e.eventContent}'
									 },
									 imageurl :'${e.changeName}'
								}, 
							</c:forEach>
						</c:if> 
					],	

					 eventContent: function (arg) {	
				            var event = arg.event;            
				            var customHtml = '';
				            customHtml += "<div class='r10 font-xxs font-bold' style='overflow: hidden;'>" + event.title + "</div>";
				            customHtml += "<div class='r10 font-xxs font-bold' style='overflow: hidden;'>" + event.start + "</div>";		            
				            customHtml += "<div class='r10 highlighted-badge font-xxs font-bold'>" + event.extendedProps.place +  "</div>";     
				            switch(event.extendedProps.categoryNo){
					            case 1 :   customHtml += "<img  style='width:100%; height : 100px;' src='"+ 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR19-1BXMU8CHpgW_o4ef_cMsKFAubWdsfsCyzeUgwcqD7CBv0WwUT64Y72HnUgoNhtLeE&usqp=CAU'   +  "'/>"; break;
					            case 2 :   customHtml += "<img  style='width:100%; height : 100px;' src='"+ 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPJBfEwiOl6PHNVLBpR2ooLZf1gx5fYI9d6bn6TZYfGRnImcjHHII11hSwlsibXcf_tBA&usqp=CAU'   +  "'/>"; break;
					            case 3 :   customHtml += "<img  style='width:100%; height : 100px;' src='"+ 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6lm87onPTFXI7buPjllu_ocW0774X3gVW4mONbR6QmmEvnVM03KpEmSGibNCONwJ9kEA&usqp=CAU'   +  "'/>"; break;
					            case 4 :   customHtml += "<img  style='width:100%; height : 100px;' src='"+ 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0iX_NJSlpi54a2A2_yvf4wNzmIUPTmM9OHw&usqp=CAU'   +  "'/>"; break;
					            default :   customHtml += "<img  style='width:100%; height : 100px;' src='"+ 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfXljjKETOFvt1Uq8HnOM2b1ngsAP3kLEttg&usqp=CAU'   +  "'/>";
				            }
					   	 	return { html: customHtml }
				        }

			});// var calendar
			calendar.render();
			
	        calendar.on('dateClick', function(e){
	        		$('#eventDate').val(e.dateStr);
	        		$('#insertModal').modal('show');
	        });
	        calendar.on('eventClick', function(e){

	

				 event = e.event._def;

				 $('#detail-content').children().eq(0).html(event.title);
				 $('#detail-content').children().eq(1).html(event.extendedProps.categoryNo);
				 $('#detail-content').children().eq(2).html(event.extendedProps.content);
				 $('#detail-content').children().eq(3).html(event.extendedProps.place);
				 $('#detail-content').children().eq(4).html(event.extendedProps.participants);
				 $('#detail-content').children().eq(5).html(event.extendedProps.imageurl);
				 $('#detail-content').children().eq(6).html(event.publicId);

		         $('#detailModal').modal('show');  	
	        });
	        
	        $('#insertEvent').on('click', () => {	


	    		const formData = new FormData();
	    		formData.append('eventTitle', $('#eventTitle').val());
	    		formData.append('eventContent', $('#eventContent').val());
	    		formData.append('eventPlace', $('#eventPlace').val());
	    		formData.append('eventDate', $('#eventDate').val());
	    		formData.append('upfile', $('#upfile')[0].files[0]);
	    		formData.append('categoryNo', $('#categoryNo').val());

	    		$.ajax({
	    			url : 'insert.ev',
	    			type : 'POST',
	    			processData: false,
	    		    contentType: false,
	    		    enctype: 'multipart/form-data',
	    		    data: formData,

	    			success : data => {	    	
	    				if(data === 'success'){
	    					$('#insertModal').modal('hide');		
	    					calendar.refetchEvents();
    						alert('이벤트 등록 성공!');	

    				
    					} else{
    						alert('이벤트 등록 실패!');
    					}

	    			},
	    			error : () => {
	    				alert('이벤트를 등록할 수 없습니다.');
	    				$('#insertModal').modal('hide');
	    			}
	    		})
	    	});
	    	
	    	$('#updateForm').on('click', function(){
	    	
	    	// 각 div.html()을 변수에 저장하고
	    		let eventTitle = $('#dEventTitle').html();
	    		let categoryNo = $('#dCategoryNo').html();
	    		let eventContent = $('#dEventContent').html();
	    		//let eventDate = $('#mEventDate').html();
	    		let eventPlace = $('#dEventPlace').html();
	    		let participants = $('#dParticipants').html();
	    		let upfile = $('#dUpfile').html();
	    		let eventNo = $('#dActivityNo').html();
	    		
	    	// #detail-content에  input(value가 변수인) 삽입
	    		$('#detail-title').html('');
	    		$('.modal-footer').html('');
	    	 	$('#detail-content').html(
	    	 			'<label for="uEventTitle">이벤트명 : <input id="uEventTitle" class="event" name="eventTitle" type="text" required  value="' +eventTitle+ '"/></label>'
	    		   		+'<label for="uCategoryNo">카테고리 : <input id="uCategoryNo" class="event" name="categoryNo" type="number" required  value="' +categoryNo+ '"/></label>'
	    			 	+'<label for="uEventContent">이벤트내용 : <input id="uEventContent" class="event" name="eventContent" type="text" required  value="' +eventContent+ '"/></label>'
	    			 	//+'<label for="eventDate">이벤트날짜 : <input id="eventDate" class="event" name="eventDate" type="text" required  value="' +eventDate+ '"/></label>'
	    		        +'<label for="uEventPlace">이벤트장소 : <input id="uEventPlace" class="event" name="eventPlace" type="text" required  value="' +eventPlace+ '"/></label>'
	    		        +'<label for="uParticipants">참여인원 : <input id="uParticipants" class="event" name="participants" type="number" readonly value="' +participants+ '"/></label>'
	    		       	+'<label for="uUpfile">첨부파일 : <input id="uUpfile" class="event" name="upfile" type="file" accept=".jpg, .jpeg, .png" required  value="' +upfile+ '"/></label>'
	    		       	+'<input class="uActivityNo" id="uActivityNo" name="activityNo" type="hidden"  value="' +activityNo+ '"/>'
	    		       	+'<div class="modal-footer">'
	    		       	+'<button type="submit" class="tn btn-primary" id="updateEvent">수정</button>'
	    		        +'</div>');
	    		});//updateF
	    		
	    		$('#detail-content').on('click', '#updateEvent', function(){
	    			
	    			const formData = new FormData();
	    			
	    			formData.append('eventTitle', $('#uEventTitle').val());
	    			formData.append('categoryNo', Number($('#uCategoryNo').val()));
	    			formData.append('eventContent', $('#uEventContent').val());
	    			formData.append('eventPlace', $('#uEventPlace').val());
	    			formData.append('upfile', $('#uUpfile')[0].files[0]);
	    			formData.append('activityNo', $('#uActivityNo').val());
	    			    
	    			$.ajax({
	    				url : 'update.ev',
	    				type : 'POST',
	    				processData: false,
	    			    contentType: false,
	    			    enctype: 'multipart/form-data',
	    			    data: formData,
	    				success : result => {
	    					if(result === 'success'){
	    						alert('이벤트 수정 성공!');
	    					} else{
	    						alert('이벤트 수정 실패!');
	    					}
	    					$('#detailModal').modal('hide');
	    				},
	    				error : function(){
	    					alert('수정할 수 없습니다. 잠시 후 다시 시도해주세요');
	    				}
	    			});
	    		});
	    		
	    		$('#joinEvent').on('click', function(){
	    			
	    			$.ajax({
	    				url : 'join.ev',
	    				/* type : 'POST', */
	    				data : { 
	    					// 둘 다 null이 들어가는 이유?
	    					userNo : ${ sessionScope.loginUser.userNo },
	    					activityNo : $('#mActivityNo').html(),
	    				},
	    				success : function(data){
	    					alert('참가신청 성공!');
	    					$('#detailModal').modal('hide');
	    				},
	    				error : function(){
	    					alert('참가할 수 없습니다. 잠시 후 다시 시도해주세요');
	    				}
	    			});
	    		});//joinE

     })//DOMContentLoaded

</script>

<style>
		#insertModal{
			width: 50%;
			height :  50%;
			margin : auto;
			background-color : white;
		}
		#eventEnrollForm{
			
		}
		#modal-body{
			display : flex;
			/* flex-flow : column nowrap; */
			/*align-items : center;*/
			justify-content : center;
		}
</style>

</body>
</html>