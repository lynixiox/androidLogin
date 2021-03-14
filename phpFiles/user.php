<?php
	
	include_once 'connect.php';
	
	class User
	{
		
		private $dataBase;
		private $table = "users";
		
		//basically start function to connect to db
		public function __construct()
		{
			$this->dataBase = new Connect();
		}
		
		//function to register users
		public function RegisterUser($username, $password)
		{
			//first check if username exits --- ugg
			$exists = $this->CheckUsername($username);
			
			//username exists 
			if($exists)
			{
				//throw error
				$json['success'] = 0;
				$json['message'] = "Username already exists";
			}
			else
			{
				//query user 
				$query = "insert into ".$this->table." (username, password) values ('$username', '$password')";
				
				//test query
				$inserted = mysqli_query($this->dataBase->ReturnDb(), $query);
				
				if($inserted == 1) //user created fine
				{
					$json['success'] = 1;
					$json['message'] = "woo registered";
				}
				else //fuck
				{
					$json['success'] = 0;
					$json['message'] = "rip failed - error";
				}
				
				//close db
				mysqli_close($this->dataBase->ReturnDb());
			}
			return $json;
		}
		
		//function to check the username
		public function CheckUsername($username)
		{
			$query = "select * from ".$this->table." where username = '$username'";
			
			$result = mysqli_query($this->dataBase->ReturnDb(), $query);
			
			if(mysqli_num_rows($result) > 0)
			{
				//if there is any matching users
				//close database and return true
				mysqli_close($this->dataBase->ReturnDb());
				
				return true;
			}
			//if not return false
			return false;
		}
		
		//function to check if login works
		public function TestLogin($username, $password)
		{
			//test for 1 username with matching password
			$query = "select * from ".$this->table." where username = '$username' AND password = '$password' Limit 1";
			
			$result = mysqli_query($this->dataBase->ReturnDb(), $query);
			
			if(mysqli_num_rows($result) > 0)
			{
				//close db and return true
				mysqli_close($this->dataBase->ReturnDb());
				
				return true;
			}
			//close and return false
			mysqli_close($this->dataBase->ReturnDb());
			return false;

		}
		//function to login
		public function Login($username, $password)
		{
			$json = array();
			
			//rest login
			$canLogin = $this->TestLogin($username, $password);
			
			if($canLogin)
			{
				$json['success'] = 1;
				$json['message'] = "Logged in yat";
			}
			else
			{
				$json['success'] = 0;
				$json['message'] = "nope";
			}
			//return results
			return $json;
			
		}
		
	}
?>