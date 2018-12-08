<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    $result = mysqli_query($con,"SELECT * from Sweet");

    $response=array();
    while($row = mysqli_fetch_array($result)){
        array_push($response, array("num"=>$row[0],"swid"=>$row[1]));
    }
    echo json_encode(array("response"=>$response));
    mysqli_close($con);
?>
        
        