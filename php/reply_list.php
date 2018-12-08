<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    $result = mysqli_query($con,"SELECT number,nick,text from C_comment");

    $response=array();
    while($row = mysqli_fetch_array($result)){
        array_push($response, array("number"=>$row[0] ,"nick"=>$row[1],"text"=>$row[2]));
    }
    echo json_encode(array("response"=>$response));
    mysqli_close($con);
?>
        
        