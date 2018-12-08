<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $userID=$_POST["userID"];
    $userPass=$_POST["userPass"];
    $userName=$_POST["userName"];
    $userNick=$_POST["userNick"];
    $userPnum1=$_POST["userPnum1"];
    $userPnum2=$_POST["userPnum2"];
    $userPnum3=$_POST["userPnum3"];
    $userEmail1=$_POST["userEmail1"];
    $userEmail2=$_POST["userEmail2"];

    $userPnum=$userPnum1."-".$userPnum2."-".$userPnum3;
    $userEmail=$userEmail1."@".$userEmail2;

    $statement = mysqli_prepare($con,"INSERT INTO member VALUES(?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement,"ssssss",$userID,$userPass,$userName,$userNick,$userPnum,$userEmail);
    mysqli_stmt_execute($statement);
    
    $response=array();
    $response["success"]=true;

    echo json_encode($response);
?>