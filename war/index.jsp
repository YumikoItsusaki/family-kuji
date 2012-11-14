<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
<title>Index</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-responsive.css" rel="stylesheet">
<link href="css/kuji.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>

<body>
<div id="container">

<h1>Itsusaki Family Kuji</h1>

<ul>
<c:forEach var="e" items="${f:errors()}">
  <li class="text-error">${f:h(e)}</li>
</c:forEach>
</ul>

<form action="kuji" method="post">
  <select name="id">
    <option value="">▼あなたの名前を選んでください</option>
<c:forEach var="member" items="${members}">
    <option value="${member.id}">${f:h(member.name)}</option>
</c:forEach>
  </select>
  <p>パスワードを入力してください。あなたが初めてくじを引く場合、今後ここで設定したパスワードを使って結果を再確認することができます。すでにくじを引いている場合は初回に設定したパスワードを入力してください。</p>
  <p>パスワード: <input type="text" name="pass"></p>
  <p><input class="btn" type="submit" value="くじ"></p>
</form>

</div>
</body>
</html>
