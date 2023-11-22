<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        /* 배너 */
        .floatdiv {
            width: 50px;
            height: 100px;
            background-color: #f1f1f1;
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
            position: fixed;
            transform: translateY(100%);
        }
        .floatdiv li{
            display: none;
        }
        
        .floatdiv:hover {
            position: fixed;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            background-color: #f1f1f1;
            padding: 10px;
            width: 200px;
            height: auto;
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
        }
        
        .floatdiv:hover li{
            display: block;
        }
        .floatdiv:hover li:hover{
        	cursor: pointer;
        }

        .floatdiv ul  { list-style: none; }
        .floatdiv li  { margin-bottom: 5px; text-align: center; }
        .floatdiv a   { color: #5D5D5D; border: 0; text-decoration: none; display: block; }
        .floatdiv a:hover, .floatdiv .menu  { background-color: #5D5D5D; color: #fff; }
        .floatdiv .menu, .floatdiv .last    { margin-bottom: 0px; }
    </style>
</head>
<body>
    <div class="floatdiv">
        <ul>
            <li style="font-size: 30px; margin-bottom: 15px;">도서목록 마이페이지</li>
            <li onclick="location.href='book'">도서</li>
            <li onclick="location.href='bookreport'">독후감 게시판</li>
            <li onclick="location.href='bookcalendar'">독서캘린더</li>
        </ul>
    </div>
</body>
</html>