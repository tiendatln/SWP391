<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Checkout - Modern Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
        }

        .checkout-container {
            max-width: 1200px;
            margin: 40px auto;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .checkout-step {
            position: relative;
            padding: 25px;
            margin-bottom: 20px;
            background: #fff;
        }

        .step-icon {
            width: 50px;
            height: 50px;
            background: linear-gradient(135deg, #007bff, #0056b3);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            position: absolute;
            left: 25px;
            top: 25px;
        }

        .form-control, .form-select {
            border-radius: 8px;
            padding: 10px 15px;
            border: 1px solid #e2e8f0;
        }

        .address-card {
            background: #fff;
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding58: 20px;
            transition: all 0.3s ease;
        }

        .address-card:hover, .address-card input:checked + .address-card {
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
        }

        .payment-option {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            transition: all 0.3s ease;
        }

        .payment-option:hover, .payment-option input:checked + .payment-option {
            border-color: #007bff;
            background: rgba(0,123,255,0.05);
        }

        .order-summary {
            background: #fff;
            padding: 25px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #007bff, #0056b3);
            border: none;
            padding: 10px 25px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,123,255,0.3);
        }

        .product-img {
            border-radius: 8px;
            object-fit: cover;
        }
    </style>
</head>
<body>
    <div class="checkout-container container">
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card checkout-step">
                    <div class="step-icon">
                        <i class="bx bxs-receipt fs-4"></i>
                    </div>
                    <div class="ms-5 ps-4">
                        <h4 class="mb-3">Billing Information</h4>
                        <form>
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label">Full Name</label>
                                    <input type="text" class="form-control" placeholder="Enter your name">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control" placeholder="Enter email">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Phone</label>
                                    <input type="text" class="form-control" placeholder="Enter phone">
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Address</label>
                                    <textarea class="form-control" rows="3" placeholder="Enter full address"></textarea>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Country</label>
                                    <select class="form-select">
                                        <option>Select Country</option>
                                        <option value="US">United States</option>
                                        <option value="VN">Vietnam</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">City</label>
                                    <input type="text" class="form-control" placeholder="Enter city">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Zip Code</label>
                                    <input type="text" class="form-control" placeholder="Enter zip code">
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="card checkout-step">
                    <div class="step-icon">
                        <i class="bx bxs-truck fs-4"></i>
                    </div>
                    <div class="ms-5 ps-4">
                        <h4 class="mb-3">Shipping Address</h4>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="address-card">
                                    <input type="radio" name="address" class="d-none" checked>
                                    <div>
                                        <h6>Primary Address</h6>
                                        <p class="text-muted mb-1">Bradley McMillian</p>
                                        <p class="text-muted mb-1">109 Clarksburg Park Road</p>
                                        <p class="text-muted">Mo. 012-345-6789</p>
                                    </div>
                                </label>
                            </div>
                            <div class="col-md-6">
                                <label class="address-card">
                                    <input type="radio" name="address" class="d-none">
                                    <div>
                                        <h6>Secondary Address</h6>
                                        <p class="text-muted mb-1">Bradley McMillian</p>
                                        <p class="text-muted mb-1">109 Clarksburg Park Road</p>
                                        <p class="text-muted">Mo. 012-345-6789</p>
                                    </div>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card checkout-step">
                    <div class="step-icon">
                        <i class="bx bxs-wallet-alt fs-4"></i>
                    </div>
                    <div class="ms-5 ps-4">
                        <h4 class="mb-3">Payment Method</h4>
                        <div class="row g-3">
                            <div class="col-4">
                                <label class="payment-option">
                                    <input type="radio" name="pay-method" class="d-none">
                                    <i class="bx bx-credit-card fs-2 mb-2 d-block"></i>
                                    Credit Card
                                </label>
                            </div>
                            <div class="col-4">
                                <label class="payment-option">
                                    <input type="radio" name="pay-method" class="d-none">
                                    <i class="bx bxl-paypal fs-2 mb-2 d-block"></i>
                                    Paypal
                                </label>
                            </div>
                            <div class="col-4">
                                <label class="payment-option">
                                    <input type="radio" name="pay-method" class="d-none" checked>
                                    <i class="bx bx-money fs-2 mb-2 d-block"></i>
                                    Cash on Delivery
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="d-flex justify-content-between mt-4">
                    <a href="#" class="btn btn-outline-secondary">
                        <i class="bx bx-arrow-left me-1"></i> Continue Shopping
                    </a>
                    <a href="#" class="btn btn-primary">
                        <i class="bx bx-cart me-1"></i> Proceed to Payment
                    </a>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card order-summary sticky-top" style="top: 20px;">
                    <div class="p-4 border-bottom">
                        <h5 class="mb-0">Order Summary <span class="float-end">#MN0124</span></h5>
                    </div>
                    <div class="p-4">
                        <div class="d-flex align-items-center mb-4">
                            <img src="https://via.placeholder.com/80" class="product-img me-3" alt="product">
                            <div>
                                <h6 class="mb-1">Waterproof Mobile Phone</h6>
                                <p class="text-muted mb-0">$260 x 2 = $520</p>
                            </div>
                        </div>
                        <div class="d-flex align-items-center mb-4">
                            <img src="https://via.placeholder.com/80" class="product-img me-3" alt="product">
                            <div>
                                <h6 class="mb-1">Smartphone Dual Camera</h6>
                                <p class="text-muted mb-0">$260 x 1 = $260</p>
                            </div>
                        </div>
                        <div class="border-top pt-3">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Subtotal</span>
                                <span>$780</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Discount</span>
                                <span>- $78</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Shipping</span>
                                <span>$25</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Tax</span>
                                <span>$18.20</span>
                            </div>
                            <div class="d-flex justify-content-between pt-3 border-top">
                                <h5>Total</h5>
                                <h5>$745.20</h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>