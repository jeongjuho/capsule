<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $id=$_POST["userId"];
    
    $result = mysqli_query($con,"SELECT DISTINCT flid from Follow where myid='$id'");
    $result2 = mysqli_query($con,"SELECT C_id,C_title,C_content from C_capsule");

    $response=array();
    //$response1=array();

	while($row = mysqli_fetch_array($result)){
		$ids = $row[0];
		$result2 = mysqli_query($con,"SELECT C_id,C_title,C_content from C_capsule where C_id = '$ids'");
		while($row = mysqli_fetch_array($result2)){
		array_push($response, array("id"=>$row[0],"title"=>$row[1],"text"=>$row[2]));
	}
}
  		//array_push($response, array("fid"=>$row[0]));
	

    echo json_encode(array("response"=>$response));
   
    mysqli_close($con);
?>
        
        