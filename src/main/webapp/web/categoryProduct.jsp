<%-- 
    Document   : categoryProduct
    Created on : Dec 2, 2024, 4:51:01 PM
    Author     : HAO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <c:forEach var="pd" items="${sessionScope.categoryProducts}">
            <div class="product-card">
                <div class="product-image">
                    <img src="./resource/images/${pd.category.name}/${pd.imageURL}" alt="${pd.name}">
                    <c:if test="${pd.discount > 0}">
                        <div class="discount-badge">-${pd.discount}%</div>
                    </c:if>
                </div>
                <div class="product-details">
                    <div class="product-name">${pd.name}</div>
                    <c:if test="${pd.discount > 0}">
                        <div class="product-price">
                            <span class="original-price">$${pd.price}</span>
                            <span class="discounted-price">$${String.format('%.2f', pd.price * (1 - pd.discount / 100))}</span>
                        </div>
                    </c:if>
                    <c:if test="${pd.discount == 0}">
                        <div class="product-price">$${pd.price}</div>
                    </c:if>
                    <button class="product-button" onclick="window.location.href = 'productsDetail?id=${pd.id}&category=${pd.category.id}'">Show Detail</button>
                </div>
            </div>
        </c:forEach>
    </body>
</html>
