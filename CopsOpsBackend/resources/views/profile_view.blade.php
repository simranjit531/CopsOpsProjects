<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>User Profile</title>
	<style>
		.wrapper{width: 400px;background: #fff;border:1px solid #D1D1D1; margin: 0 auto;}
		.topbar{width: 100%;margin: 10px 0;}
		.top_img{padding:10px;}
		.secondbar{width: 100%;margin: 10px 0;}
		.second_img{padding:5px;overflow: hidden;}
		.img-list{padding: 0;list-style-type: none;float: right;}
		.img-list li{float: left;padding-right: 8px;}
		.center_img{width: 100%;}
		.big_img{overflow:hidden;background: #fff;width: 150px;height: 150px;border-radius: 50%;line-height: 100px; text-align: center;margin: auto;box-shadow: -1px 2px 24px -7px rgba(0,0,0,0.75);}
		.center-main-img{width: 100%;}
		.tabbar{background: #166de3;width: 100%;overflow: hidden;margin-top: 30px;}
		.right,.left{width: 50%;height: 100%;float: left;text-align: center;}
		.right .man,.left .girl{font-size: 19px; color: #fff;padding: 15px;}
		.right .man,.left  .girl{position:relative;}
		.big_img{background:url('{{ asset('uploads/profile/'.$userData->profile_image) }}') no-repeat;background-size:cover;}
/*      
		.right .man:before{content:""; position:absolute;  left:35px;top: 3px; background: url("1.png"); background-repeat: no-repeat;width:17px; height:13px; }
		.left  .girl:before{content:""; position:absolute;  left:35px;top: 3px; background: url("1.png"); background-repeat: no-repeat;width:17px; height:13px; }
*/
		.main_form{width: 100%;margin-top: 20px;}
		.first-form,.second-form{width: 47%;  margin-left: 8px;float: left;}
		input[type=text], select {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border:none;
  border-bottom: 1px solid #8bbbfc;
  box-sizing: border-box;
}

a.nextbtn {
  width: 60%;
  background-color: #075ccd;
  color: white;
  padding: 14px 50px;
  margin: 8px 0;
  border: none;
  border-radius: 25px;
  cursor: pointer;
	text-align: center;
	text-decoration: none;
}
.btn{width: 60%;text-align: center;margin: 0 auto;padding-bottom: 40px; padding-top: 30px;}.center-form{width: 100%;overflow: hidden;}
		.icon{padding-right: 13px;    vertical-align: middle;}		
	</style>
</head>

<body>	
	
	<div class="wrapper">
	    {{--
		<div class="topbar">
		  <div class="top_img"> <img src="top1.png" width="7px;" alt="arrow"></div>
		</div>
		--}}
		<div class="secondbar">
		  <div class="second_img">
			  {{--
			  <ul class="img-list" >
			    <li><a href=""><img src="image1.png" width="25px" alt="photo"></a></li>
			     <li><a href=""><img src="image2.png" width="25px" alt="photo"></a></li>
			  </ul>
			  --}}
			</div>
			<div class="center_img">
			  <div class="big_img"><!----<img src="{{ asset('uploads/profile/'.$userData->profile_image) }}" alt="main" class="center-main-img">---></div>
			</div>
		</div>
		
		<div class="tabbar">
		<div class="right">
		    <div class="man">
			   <input type="radio" name="gender" value="male" @if($userData->gender == "Male") checked @endif disabled>Man<br>
			</div>	
		  </div>
		<div class="left">
		    <div class="girl">
			    <input type="radio" name="gender" value="female"  @if($userData->gender == "Female") checked @endif disabled>Women<br>
			</div>	
		  </div>
		</div>
		<div class="main_form">
		   <form action="/action_page.php">
			      <div class="center-form">
			     <div class="first-form">
			        <input type="text" id="fname" name="lastname" placeholder="Last name" value="{{ $userData->last_name}}" disabled>
			      </div>
			      <div class="second-form">
			        <input type="text" id="lname" name="firstname" placeholder="First name" value="{{ $userData->first_name}}" disabled>
			     </div>
			   <div class="first-form">
			        <input type="text" id="fname" name="date" placeholder="dd/mm/yy" value="{{ \Carbon\Carbon::parse($userData->date_of_birth)->format('d-m-Y') }}" disabled>
			      </div>
			      <div class="second-form">
				  <input type="text" id="fname" name="Phonenumber" placeholder="Phone Number" value="{{ $userData->phone_number}}" disabled>
			     </div>
			   <div class="first-form">
			   	<input type="text" id="lname" name="Email" placeholder="Email" value="{{ $userData->email_id}}" disabled>
			      </div>
			      
			    
			   </div>

  </form>
		</div>
	</div>
</body>
</html>
