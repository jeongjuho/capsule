<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $num=$_POST["num"];
    $swid=$_POST["swid"];

       $response=array();

	$response["success"]=true;
    $statement = mysqli_prepare($con,"INSERT INTO Sweet(num,swid) VALUES(?,?)");
    mysqli_stmt_bind_param($statement,"ss",$num,$swid);
    mysqli_stmt_execute($statement);

    echo json_encode($response);
	
?>