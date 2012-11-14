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
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/bootstrap-responsive.css" rel="stylesheet">
<link href="../css/kuji.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>

<body>
<div id="container">

<h1>くじびき結果</h1>

<table class="table table-striped">
  <tr>
    <th>名前</th>
    <th></th>
    <th>プレゼント先</th>
  </tr>
<c:forEach var="m" items="${members}">
  <tr>
    <td>${f:h(m.name)}</td>
    <td>→</td>
    <td><c:if test="${m.recipient != null}">${f:h(m.recipient.name)}</c:if></td>
  </tr>
</c:forEach>
</table>

</div>
</body>
</html>
