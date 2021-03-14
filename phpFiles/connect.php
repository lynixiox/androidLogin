<?php

	include_once 'config.php';
	
	class Connect
	{
		private $connect;
		
		public function __construct()
		{
			$this->connect = mysqli_connect(HostName, HostUser, HostPass, DatabaseName);
			
			if(mysqli_connect_errno($this->connect))
			{
				echo "Something went wrong: " . mysqli_connect_error();
			}
			
		}
		
		public function ReturnDb()
		{
			return $this->connect;
		}
		
	}

?>