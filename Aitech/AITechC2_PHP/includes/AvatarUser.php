<?php
session_start();
$conn = mysqli_connect("localhost", "root", "");
mysqli_select_db($conn, "AITechC2");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $qry = "SELECT * FROM `users` WHERE `id`='$id'";
    $res = mysqli_query($conn, $qry);
    $row = mysqli_fetch_assoc($res);

    if ($_FILES['upload']) {
        $on = $_FILES['upload']['name'];
        $sn = $_FILES['upload']['tmp_name'];
        $dn = "avtImages/" . $on;
        move_uploaded_file($sn, $dn);

        $qry = "UPDATE `users` SET `image`='$on' WHERE `id`='$id'";
        $res = mysqli_query($conn, $qry);

        if ($res == true)
            $_SESSION['msg'] = "File Uploaded Successfully";
        else
            $_SESSION['msg'] = "Could not upload File";

        header("location:" . $_SERVER['PHP_SELF']);
        exit();

    }
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>File Upload</title>
</head>
<body>
<center>
    <table border="1">
        <form name="frm" action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post"
              enctype="multipart/form-data">
            <tr>
                <td>Enter User ID</td>
                <td><input type="text" name="id"/></td>
            </tr>
            <tr>
                <td>Select Image</td>
                <td><input type="file" name="upload"/></td>
            </tr>

            <tr>
                <td colspan="2" align="center">
                    <input type="submit" name="submit" value="Upload"/>
                </td>
            </tr>
        </form>
    </table>
    <?php
    if (isset($_SESSION['msg'])) {
        echo $_SESSION['msg'];
        unset($_SESSION['msg']);
    }
    ?>
</center>
</body>
</html>