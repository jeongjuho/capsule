<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $id=$_POST["userId"];
    
    $result = mysqli_query($con,"SELECT M_nick from member where M_id='$id' ");
    $result2= mysqli_query($con,"SELECT max(num) from C_capsule");
    $row2=mysqli_fetch_array($result2);
    $row = mysqli_fetch_array($result);
    $response=array();
    array_push($response, array("nick"=>$row[0],"num"=>$row2[0]));
  /*  while($row = mysqli_fetch_array($result)){
        array_push($response, array("nick"=>$row[0],"num"=>$row2[0]));
    }*/
    echo json_encode(array("response"=>$response));
    
    mysqli_close($con);
?>
        
        