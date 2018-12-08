<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $myid=$_POST["myid"];
    $flid=$_POST["flid"];

       $response=array();
	
   if($myid==$flid){
	$response["success"]=false;
    }

   else{

        $result = mysqli_query($con,"DELETE FROM Follow1 where myid='$myid' and flid='$flid' ");
        // mysqli_stmt_execute($result);
	$response["success"]=true;
}
    echo json_encode($response);
	
?>