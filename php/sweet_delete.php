<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");

    $num=$_POST["num"];
    $swid=$_POST["swid"];

       $response=array();

        $result = mysqli_query($con,"DELETE FROM Sweet where num='$num' and swid='$swid' ");
        // mysqli_stmt_execute($result);
	$response["success"]=true;
    echo json_encode($response);
	
?>