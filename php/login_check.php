<?
session_start();

?>

<meta charset="uft-8">

<?

$db_host = "localhost"; 

$db_user = "ggcapsule"; 

$db_passwd = "rhdrkazoqtbf12";

$db_name = "ggcapsule"; 

$conn = mysqli_connect($db_host,$db_user,$db_passwd,$db_name);


$sql ="select* from member where M_id='$id'";

$result=mysql_query($sql, $conn);

$num_match=mysql_num_rows($result);

     if(!$num_match)
       {

      echo("
	<script>
	  window.alert('���� ���̵� �Դϴ�.')
	  history.go(-1)
	  </script>
	");

	}

      else
        {
	$row=mysql_fetch_array($result);
	$db_pass = $row[M_pass];

      if($pass !=$db_pass)
        {
	echo("
	        <script>
		window.alert('��й�ȣ�� Ʋ���ϴ�.')
		history.go(-1)
	         </script>
	");
	
	exit;
           }

	else
      	{
    	         $userid=$row[M_id];
	         $usernick=$row[M_nick];
	         
	    $_SESSION['userid']=$userid;
	    $_SESSION['usernick']=$usernick;

	echo("
	 
 		<script>
		   location.href='http://ggcapsule.dothome.co.kr/home.php';
		</script>

		");
	}
	   }
	?>
                   
