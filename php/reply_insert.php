<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $nick=$_POST["nick"];
    $text=$_POST["text"];
    $num=$_POST["num"];

    $statement = mysqli_prepare($con,"INSERT INTO C_comment(number,nick,text) VALUES(?,?,?)");
    mysqli_stmt_bind_param($statement,"sss",$num,$nick,$text);
    mysqli_stmt_execute($statement);
 
    $response=array();
    $response["success"]=true;

    echo json_encode($response);
	
?>