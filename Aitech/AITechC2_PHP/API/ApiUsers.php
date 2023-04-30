<?php

//getting the dboperation class
require_once '../includes/user.php';
require_once '../includes/category.php';
require_once '../includes/Product.php';
require_once '../includes/Comment.php';
require_once '../includes/Cart.php';
require_once '../includes/Shipping.php';
require_once '../includes/Payment.php';
require_once '../includes/CartHistory.php';

//function validating all the paramters are available
//we will pass the required parameters to this function
function isTheseParametersAvailable($params){
    //assuming all parameters are available
    $available = true;
    $missingparams = "";

    foreach($params as $param){
        if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
            $available = false;
            $missingparams = $missingparams . ", " . $param;
        }
    }

    //if parameters are missing
    if(!$available){
        $response = array();
        $response['error'] = true;
        $response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';

        //displaying error
        echo json_encode($response);

        //stopping further execution
        die();
    }
}

//an array to display response
$response = array();

//if it is an api call
//that means a get parameter named api call is set in the URL
//and with this parameter we are concluding that it is an api call
if(isset($_GET['apicall'])){

    switch($_GET['apicall']){

        //the CREATE operation
        //if the api call value is 'createuser'
        //we will create a record in the database
        case 'createuser':
            //first check the parameters required for this request are available or not
            isTheseParametersAvailable(array('name','email','phone','address','password','gender'));

            //creating a new dboperation object
            $db = new user();

            //creating a new record in the database
            $result = $db->createUsers(
                $_POST['name'],
                $_POST['email'],
                $_POST['phone'],
                $_POST['address'],
                $_POST['password'],
                $_POST['gender']
            );


            //if the record is created adding success to response
            if($result === true){
                //record is created means there is no error
                $response['error'] = false;

                //in message we have a success message
                $response['message'] = 'Đăng kí thành công';

                //and we are getting all the heroes from the database in the response
                $response['users'] = $db->getUsers();
            }else{

                //if record is not added that means there is an error
                $response['error'] = true;

                //and we have the error message
                $response['message'] = $result;
            }
            break;

        //the READ operation
        //if the call is getheroes
        case 'gethusers':
            $db = new user();
            $response['error'] = false;
            $response['message'] = '';
            $response['users'] = $db->getUsers();
            break;


        //the UPDATE operation
        //the UPDATE operation

        case 'updateuser':
            isTheseParametersAvailable(array('id','name','email','phone','address','gender'));
            $db = new user();
            if (isset($_POST['password'])) {
                $result = $db->updateUsers(
                    $_POST['id'],
                    $_POST['name'],
                    $_POST['email'],
                    $_POST['phone'],
                    $_POST['address'],
                    $_POST['password'],
                    $_POST['gender']
                );
            } else {
                $result = $db->updateUsers(
                    $_POST['id'],
                    $_POST['name'],
                    $_POST['email'],
                    $_POST['phone'],
                    $_POST['address'],
                    null,
                    $_POST['gender']
                );
            }
            if($result === true){
                $response['error'] = false;
                $response['message'] = 'Cập nhật thành công';
                $response['users'] = $db->getUsers();
            }else{
                $response['error'] = true;
                $response['message'] = $result;
            }
            break;

        case 'resetPassword':
            isTheseParametersAvailable(array('email','password'));
            $db = new user();
            $result = $db->resetPassword(
                $_POST['email'],
                $_POST['password']
            );

            if($result === true){
                $response['error'] = false;
                $response['message'] = 'Thay đổi mật khẩu thành công';
                $response['users'] = $db->getUsers();
            }else{
                $response['error'] = true;
                $response['message'] = $result;
            }
            break;

        //the delete operation
        case 'deleteuser':

            //for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
            if(isset($_GET['id'])){
                $db = new user();
                if($db->deleteUsers($_GET['id'])){
                    $response['error'] = false;
                    $response['message'] = 'Bạn đã xóa tài khoản';
                    $response['users'] = $db->getUsers();
                }else{
                    $response['error'] = true;
                    $response['message'] = 'Đã xảy ra lỗi';
                }
            }else{
                $response['error'] = true;
                $response['message'] = 'Không thể xóa, vui lòng thử lại';
            }
            break;

        case 'login':
            //checking the parameters required are available or not
            isTheseParametersAvailable(array('email','password'));

            //creating a new instance of the user class
            $user = new user();

            //getting the user's login credentials
            $email = $_POST['email'];
            $password = $_POST['password'];

            //calling the login method of the user class
            $result = $user->login($email, $password);

            //if the login is successful
            if($result === true){
                //success response
                $response['error'] = false;
                $response['message'] = 'Đăng nhập thành công';
            }else{
                //unsuccessful response
                $response['error'] = true;
                $response['message'] = $result;
            }
            break;

        case 'getUsersByEmail':
            $db = new user();
            $email = $_POST['email'];
            $response['error'] = false;
            $response['message'] = '';
            $response['users'] = $db->getUsersByEmail($email);
            break;

        case 'getUsersByName':
            $db = new user();
            $name = $_POST['name'];
            $response['error'] = false;
            $response['message'] = '';
            $response['users'] = $db->getUsersByName($name);
            break;

        case 'getUsersById':
            $db = new user();
            $id = $_POST['id'];
            $response['error'] = false;
            $response['message'] = '';
            $response['users'] = $db->getUsersById($id);
            break;

        case 'getCategory';
            $db = new category();
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['categories'] = $db->getCategory();
            break;

        case 'getCategoryByName';
            $db = new category();
            $category = $_POST['category'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['categories'] = $db->getCategoryByName($category);
            break;

        case 'getProduct';
            $db = new product();
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['product'] = $db->getProduct();
            break;

        case 'getProductByCategory';
            $db = new product();
            $category = $_POST['category'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['category'] = $db->getProductByCategory($category);
            break;

        case 'getProductByName';
            $db = new product();
            $search = $_POST['search'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['search'] = $db->getProductByName($search);
            break;

        case 'getDetailProduct';
            $db = new product();
            $name_product = $_POST['detail'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['detail'] = $db->getDetailProduct($name_product);
            break;

        case 'setCommentAndGetRate':
            // First, check if the required parameters are available
            isTheseParametersAvailable(array('name_user', 'name_product', 'comment_product', 'rate', 'date_comment'));

            // Create a new Comment object
            $db = new Comment();

            // Call the setCommentAndGetRate function
            $result = $db->setCommentAndGetRate(
                $_POST['name_user'],
                $_POST['name_product'],
                $_POST['comment_product'],
                $_POST['rate'],
                $_POST['date_comment']
            );

            // If the result is an array (i.e., no error occurred)
            if (is_array($result)) {
                $response['error'] = false;
                $response['message'] = 'Bình luận đã được lưu';
                $response['rates'] = $result; // use "rates" instead of "users"
            } else {
                $response['error'] = true;
                $response['message'] = $result;
            }
            break;

        case 'getComment':
            $db = new Comment();
            $name_product = $_POST['comment'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['comment'] = $db->getComment($name_product);
            break;

        //case 'setProductToCart'
        case 'setProductToCart':
            // First, check if the required parameters are available
            isTheseParametersAvailable(array('name_product', 'name_user', 'cost_product'));

            // Create a new Cart object
            $cart = new Cart();

            // Call the setProductToCart function
            $result = $cart->setProductToCart(
                $_POST['name_product'],
                $_POST['cost_product'],
                $_POST['name_user']
            );

            // If the result is true, return success message
            if ($result) {
                $response['error'] = false;
                $response['message'] = 'Product added to cart successfully';
            } else {
                $response['error'] = true;
                $response['message'] = 'Error occurred while adding product to cart';
            }
            break;

        case 'getCartDetail':
            $db = new Cart();
            $name_user = $_POST['cart_detail'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['cart_detail'] = $db->getCart($name_user);
            break;

        case 'removeAllCartItems':
            $db = new Cart();
            $name_user = $_POST['remove_cart_detail'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['remove_cart_detail'] = $db->removeAllCartItems($name_user);
            break;

        case 'removeCartItemsById':
            $db = new Cart();
            $id_cart_details = $_POST['remove_cart_detail_by_id'];
            $response['error'] = false;
            $response['message'] = 'Bạn đã xóa hết';
            $response['remove_cart_detail_by_id'] = $db->removeCartItemById($id_cart_details);
            break;

        case 'setShippingAddress':
            isTheseParametersAvailable(array('name', 'name_user', 'phone_user', 'address_user', 'note_user'));

            $db = new Shipping();

            $id_shipping = $db->setShippingAddress(
                $_POST['name'],
                $_POST['name_user'],
                $_POST['phone_user'],
                $_POST['address_user'],
                $_POST['note_user']
            );

            // If the result is true, return success message with id_shipping
            if ($id_shipping > 0) {
                $response['error'] = false;
                $response['message'] = 'Address added to cart successfully';
                $response['id_shipping'] = $id_shipping;
            } else {
                $response['error'] = true;
                $response['message'] = 'Error occurred while adding address to Shipping';
            }
            break;

        case 'getShippingAddress':
            $db = new Shipping();
            $name = $_POST['cart_shipping'];
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['cart_shipping'] = $db->getShippingAddress($name);
            break;

        case 'setDirectPayment':
            isTheseParametersAvailable(array('name_user', 'code_cart', 'cart_date', 'id_shipping'));

            $db = new Payment();

            $payment = $db->setDirectPayment(
                $_POST['name_user'],
                $_POST['code_cart'],
                $_POST['cart_date'],
                $_POST['cart_number'],
                $_POST['cart_cost'],
                $_POST['id_shipping']
            );
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';

            break;

        case 'getBillPayment':

            $db = new Payment();
            $code_cart = $_POST['code_cart_payment'];

            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['cart_shipping_payment'] = $db->getBillPayment($code_cart);
            break;

        case 'getProductPayment':

            $db = new Payment();
            $code_cart = $_POST['code_cart_payment'];

            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['cart_product_payment'] = $db->getProductPayment($code_cart);
            break;

        case 'getCartHistory':

            $db = new CartHistory();
            $id_user = $_POST['id_user'];

            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['cart_history'] = $db->getCartHistory($id_user);
            break;
    }

}else{
    //if it is not api call
    //pushing appropriate values to response array
    $response['error'] = true;
    $response['message'] = 'Invalid API Call';
}

//displaying the response in json structure
echo json_encode($response);