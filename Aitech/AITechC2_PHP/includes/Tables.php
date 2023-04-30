<?php
// Include the necessary files
require_once 'DbConnect.php';
require_once 'TableCreator.php';

// Create a new instance of DbConnect
$db = new DbConnect();

// Connect to the database
$con = $db->connect();

/*
 * Trước tiên vào mysql php tạo cở sở dữ liệu có tên là AITechC2
 * http://localhost:86/AITechC2/includes/Tables.php
 * */


// Create a new instance of TableCreator, passing in the database connection

$tableCreator = new TableCreator($con);

// Define the fields for the new table
// user
$user = array(
    'name' => 'TEXT',
    'email' => 'VARCHAR(255)',
    'phone' => 'VARCHAR(255)',
    'address' => 'VARCHAR(255)',
    'password' => 'VARCHAR(255)',
    'gender' => 'CHAR(9)',
    'image' => 'TEXT'
);

//cart_payments
$cart_payments = array(
    'name_user' => 'VARCHAR(100)',
    'code_cart' => 'VARCHAR(10)',
    'cart_status' => 'INT(11)',
    'cart_date' => 'VARCHAR(50)',
    'cart_number' => 'INT(11)',
    'cart_cost'=>'INT(50)',
    'cart_payment' => 'VARCHAR(50)',
    'id_shipping' => 'INT(11)'
);

//cart_details
$cart_details = array(
    'code_cart' => 'INT(10)',
    'name_product' => 'VARCHAR(100)',
    'cost_product' => 'VARCHAR(100)',
    'name_user' => 'VARCHAR(20)',
    'status' => 'INT(11)'
);

//categories
$categories = array(
    'category' => 'VARCHAR(200)'
);

//comments
$comments = array(
    'name_user' => 'VARCHAR(100)',
    'name_product' => 'VARCHAR(100)',
    'comment_product' => 'TEXT',
    'rate' => 'FLOAT',
    'date_comment' => 'DATE'
);

//products
$products = array(
    'name_product' => 'VARCHAR(100)',
    'cost_product' => 'VARCHAR(200)',
    'image_product' => 'TEXT',
    'detail_product' => 'VARCHAR(200)',
    'category' => 'VARCHAR(200)',
    'rate' => 'FLOAT',
    'status' => 'INT(11)'
);

//shipping
$shipping = array(
    'name' => 'VARCHAR(100)',
    'phone' => 'VARCHAR(50)',
    'address' => 'VARCHAR(200)',
    'note' => 'VARCHAR(255)',
    'id_user' => 'INT(11)'
);


// Call the createTable method of TableCreator, passing in the table name and fields
$tableCreator->createTable('users', $user);
$tableCreator->createTable('cart_payments', $cart_payments);
$tableCreator->createTable('cart_details', $cart_details);
$tableCreator->createTable('categories', $categories);
$tableCreator->createTable('comments', $comments);
$tableCreator->createTable('products', $products);
$tableCreator->createTable('shipping', $shipping);