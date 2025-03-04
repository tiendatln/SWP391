<%-- 
    Document   : Home
    Created on : Feb 12, 2025, 2:16:35 PM
    Author     : tiend
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thế Giới Di Động Layout</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </head>
    <body>
        
        <!-- Header -->
        <header class="bg-yellow-400 py-3 px-6 shadow-md">
            <div class="container flex items-center justify-between">
                <div class="flex items-center space-x-3">
                    <img src="logo.png" alt="Logo" class="w-10 h-10">
                    <span class="text-black text-2xl font-bold italic">Device</span>
                </div>
                <div class="flex-1 mx-6">
                    <form class="d-flex search-bar"  method="GET" action="/search">
                        <input class="form-control me-md-2" type="search" name="q" placeholder="Search" aria-label="Search">
                    </form>
                </div>
                <div class="flex space-x-4">
                    <a href="#" class="text-black font-medium flex items-center no-underline"><i class="fa-solid fa-user" style="margin: 4px;"></i>Dat</a>
                    <a href="#" class="text-black font-medium flex items-center no-underline"><span><i class="fa-solid fa-cart-shopping" style="margin: 4px;"></i>Giỏ hàng</a>
                </div>
            </div>
        </header>

        <nav class="bg-yellow-400 py-2 flex justify-center space-x-6 text-black font-medium">
            <a href="#" class="flex items-center no-underline"><span class="material-icons">smartphone</span></a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">laptop</span> </a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">headphones</span></a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">watch</span> </a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">tablet</span></a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">PC, print</span></a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">sim_card</span></a>
            <a href="#" class="flex items-center no-underline"><span class="material-icons">build</span></a>
        </nav>
    </body>
</html>
