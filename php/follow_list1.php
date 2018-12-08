<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $id=$_POST["userId"];
    
    $result = mysqli_query($con,"SELECT DISTINCT flid,profile from Follow1 where myid='$id'");
    $response=array();

	while($row = mysqli_fetch_array($result)){
  		array_push($response, array("fid"=>$row[0],"pimage"=>$row[1]));
	}

    echo json_encode(array("response"=>$response));
   
    mysqli_close($con);
?>
        
        