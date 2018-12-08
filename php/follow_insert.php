<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $myid=$_POST["myid"];
    $flid=$_POST["flid"];
    $profile=$_POST["profile"];

       $response=array();
	if($myid==$flid){
    $response["success"]=false;
	}
	else{
	$response["success"]=true;
    $statement = mysqli_prepare($con,"INSERT INTO Follow1(myid,flid,profile) VALUES(?,?,?)");
    mysqli_stmt_bind_param($statement,"sss",$myid,$flid,$profile);
    mysqli_stmt_execute($statement);

}
    echo json_encode($response);
	
?>