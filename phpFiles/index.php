<?php

	require_once 'user.php';
	
	//base variables
	$username = "";
	$password = "";
	$register = ""; //just pass through a register variable to register 
	if(isset($_POST['username']))
	{
		$username = $_POST['username'];
	}
	if(isset($_POST['password']))
	{
		$password = $_POST['password'];
	}
	if(isset($_POST['register']))
	{
		$register = $_POST['register'];
	}
	
	$user = new User();

	
	//register if there is a register
	if(!empty($username) && !empty($password) && !empty($register))
	{
		$json_registeration = $user->RegisterUser($username, $password);
		
		echo json_encode($json_registeration);
	}
	//else
	if(!empty($username) && !empty($password) && empty($register))
	{
		$json_array = $user->Login($username, $password);
		
		echo json_encode($json_array);
	}
	

?>
