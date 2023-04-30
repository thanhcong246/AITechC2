package com.vn.tcshop.aitechc.Api;

public class Api {
    public static final String KEY = "Bearer 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";
    private static final String ROOT_URL = "http://192.168.1.12:86/AITechC2/API/ApiUsers.php?apicall=";
    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_READ_USERS = ROOT_URL + "gethusers";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_RESET_PASS_USER = ROOT_URL + "resetPassword";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&id=";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_USER_BY_EMAIL = ROOT_URL + "getUsersByEmail";
    public static final String URL_USER_BY_NAME = ROOT_URL + "getUsersByName";
    public static final String URL_USER_BY_ID = ROOT_URL + "getUsersById";
    public static final String UPLOAD_URL_IMG_PROFILE = "http://192.168.1.12:86/AITechC2/includes/AvatarUser.php";
    public static final String URL_IMG = "http://192.168.1.12:86/AITechC2/includes/avtImages/";
    //
    public static final String URL_CATEGORY = ROOT_URL + "getCategory";
    public static final String URL_PRODUCT = ROOT_URL + "getProduct";
    public static final String URL_SEARCH_PRODUCT = ROOT_URL + "getProductByName";
    public static final String URL_CATEGORY_PRODUCT = ROOT_URL + "getProductByCategory";
    public static final String URL_DETAIL_PRODUCT = ROOT_URL + "getDetailProduct";
    public static final String URL_COMMENT_PRODUCT = ROOT_URL + "getComment";
    public static final String URL_SET_COMMENT = ROOT_URL + "setCommentAndGetRate";
    public static final String URL_SET_PRODUCT_TO_CART = ROOT_URL + "setProductToCart";
    public static final String URL_CART_DETAIL = ROOT_URL + "getCartDetail";
    public static final String URL_REMOVE_CART = ROOT_URL + "removeAllCartItems";
    public static final String URL_REMOVE_CART_BY_ID = ROOT_URL + "removeCartItemsById";
    public static final String URL_SET_ADDRESS = ROOT_URL + "setShippingAddress";
    public static final String URL_GET_ADDRESS = ROOT_URL + "getShippingAddress";
    public static final String URL_SET_DIRECT_PAYMENT = ROOT_URL + "setDirectPayment";
    public static final String URL_GET_BILL_PAYMENT = ROOT_URL + "getBillPayment";
    public static final String URL_GET_PRODUCT_PAYMENT = ROOT_URL + "getProductPayment";
    public static final String URL_GET_CART_HISTORY = ROOT_URL + "getCartHistory";
    public static final String URL_PRODUCT_IMAGE = "http://192.168.1.12:86/AITechC2/quanlysanpham/uploads/";
}
