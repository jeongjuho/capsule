<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $id=$_POST["userId"];
    
    $result = mysqli_query($con,"SELECT count(flid) from Follow1 where myid='$id' ");
    $result2= mysqli_query($con,"SELECT count(myid) from Follow1 where flid='$id' ");
    $row2=mysqli_fetch_array($result2);
    $row = mysqli_fetch_array($result);
    $response=array();
    array_push($response, array("subscribers"=>$row[0],"followers"=>$row2[0]));
  /*  while($row = mysqli_fetch_array($result)){
        array_push($response, array("nick"=>$row[0],"num"=>$row2[0]));
    }*/
    echo json_encode(array("response"=>$response));
    
    mysqli_close($con);
?>
        
        