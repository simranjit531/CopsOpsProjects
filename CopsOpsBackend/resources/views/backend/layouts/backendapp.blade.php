<!DOCTYPE html>
<html lang="{{ app()->getLocale() }}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>COPOPS- @yield('title')</title>
<!-- Tell the browser to be responsive to screen width -->
<meta name="description" content="@yield('description')">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="csrf-token" content="{{ csrf_token() }}">
<link rel="icon" type="image/png" sizes="32x32" href="{{ asset('img/favicon-32x32.png') }}">
<link
	href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"
	rel="stylesheet">
<link href="https://cdn.rawgit.com/sachinchoolur/lightgallery.js/master/dist/css/lightgallery.css" rel="stylesheet">
<!-- Font Awesome -->
{{ Html::style('css/plugins/font-awesome/css/font-awesome.min.css') }}
<!-- Theme style -->
{{ Html::style('css/dist/css/adminlte.min.css') }}
<!-- iCheck -->
{{ Html::style('css/plugins/iCheck/flat/blue.css') }}
<!-- Morris chart -->
{{ Html::style('css/plugins/morris/morris.css') }}
<!-- jvectormap -->
{{ Html::style('css/plugins/jvectormap/jquery-jvectormap-1.2.2.css') }}
<!-- Date Picker -->
{{ Html::style('css/plugins/datepicker/datepicker3.css') }}
<!-- Daterange picker -->
{{ Html::style('css/plugins/daterangepicker/daterangepicker-bs3.css') }}
 <!-- Bootstrap time Picker -->
 {{ Html::style('css/plugins/timepicker/bootstrap-timepicker.min.css') }}
<!-- bootstrap wysihtml5 - text editor -->
{{
Html::style('css/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css')
}}
<!-- Google Font: Source Sans Pro -->
{{ Html::style('css/custom.css') }} @yield('before-styles')
<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800"
	rel="stylesheet">
</head>
<style>
.notification-sec #menu1{position: absolute;
    left: -200px;
    height: 250px;
    overflow-y: scroll;
    min-width: 15rem;
}
.notification-sec a #noticount{
    position: absolute;
    left: 7px;
    background: red;
    top: -5px;
    border-radius: 50%;
    color: #fff;
    }
.notification-sec .fa-bell-o{position:relative;}
.notification-sec{padding-top: 7px;float:right;position:relative;
    margin: 0 10px;}
</style>
<body class="hold-transition sidebar-mini">
	<audio id="myAudio">      
      <source src="{{ asset('glass_ping.mp3') }}" type="audio/mp3">
		  {{ trans('pages.browsernotsuppory')}}
    </audio>
	<div class="wrapper">

		<!-- Navbar -->
		<nav
			class="main-header navbar navbar-expand bg-white navbar-light border-bottom header-right-navbar">
			<!-- Left navbar links -->
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" data-widget="pushmenu"
					href="#"><i class="fa fa-bars"></i></a></li>
				<li class="pull-right"><a href="{{ route('logout') }}" class="nav-link"><i class="fa fa-sign-out"></i>{{ trans('pages.logout') }} </a></li>
				
				<li role="presentation" class="dropdown notification-sec">
                  <a href="javascript:void(0);" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="true">
                    <i class="fa fa-bell-o"></i>
                    <span class="badge bg-green" id="noticount">0</span>
                  </a>
                  <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
                                   
                  </ul>
                </li>
				
				<li class="language-drop">
				<select id="language">
						<option value=''>{{ trans('pages.language')}}</option>
						<option value="fr">FR</option>
						<option value="en">ANG</option>
				</select></li>

			</ul>

		</nav>
		<!-- /.navbar -->

		<!-- Main Sidebar Container -->
		<aside class="main-sidebar sidebar-dark-primary elevation-4">
			<!-- Brand Logo -->
			<div class="left-logo">
				<div class="top-part">
					<a href="javscript:void(0)" class="brand-link"> <!--<img src="{{asset('img/AdminLTELogo.png')}}" alt="COPOPS Logo" class="brand-image img-circle elevation-3"
				   style="opacity: .8">--> <span class="brand-text font-weight-bold">COP
							OPS</span>
					</a>
				</div>

				<div class="user-panel d-flex">
					<div class="info">
						<span>{{ trans('pages.controlCenter') }} <br>{{
							trans('pages.fromParis') }}
						</span>
						<div class="image">
							<img src="{{asset('img/user2-160x160.jpg')}}"
								class="img-circle elevation-2" alt="User Image"> <a href="#"
								class="image-name">{{ Auth::user()->first_name.' '. Auth::user()->last_name }}</a> <span class="online-sign">{{
								trans('pages.administrator') }}</span>
						</div>

					</div>


				</div>

			</div>

			<!-- Sidebar -->
			<div class="sidebar">
				<!-- Sidebar user panel (optional) -->


				<!-- Sidebar Menu -->
				<nav class="mt-2">
					<ul class="nav nav-pills nav-sidebar flex-column"
						data-widget="treeview" role="menu" data-accordion="false">
						<!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
						<li class="nav-item"><a href="{{ url('/dashboard') }}"
							class="nav-link {{ (\Request::route()->getName() == 'dashboard') ? 'active' : '' }}"> <i class="nav-icon fa fa-home"></i>
								<p>{{ trans('pages.controlCenter') }}</p> <!--Centre de controle-->
						</a></li>

						<li class="nav-item"><a href="{{ url('/usermanagement') }}"
							class="nav-link {{ in_array(\Request::route()->getName(), array('usermanagement', 'dailycrew', 'validationofregistrants', 'accountrefuses')) ? 'active' : '' }}"> <i class="nav-icon fa fa-user-o"></i>
								<p>{{ trans('pages.userManagement') }}</p> <!--Gestion des utilisateurs-->
						</a></li>

						<li class="nav-item"><a href="{{ url('/chat') }}" class="nav-link {{ (\Request::route()->getName() == 'chat') ? 'active' : '' }}">
								<i class="nav-icon fa fa-comments-o"></i>
								<p>{{ trans('pages.discussion') }}</p> <!--Discussion-->
						</a></li>


						<li class="nav-item"><a href="{{ url('/archivecenter') }}"
							class="nav-link {{ (\Request::route()->getName() == 'archivecenter') ? 'active' : '' }}"> <i class="nav-icon fa fa-archive"></i>
								<p>{{ trans('pages.archiveCenter') }}</p> <!--Center des archives-->
						</a></li>

						

					</ul>
				</nav>
				<!-- /.sidebar-menu -->
			</div>
			<!-- /.sidebar -->
		</aside>
		<!-- Content of main page-->
		<div class="content-wrapper">@yield('content')</div>
		<!-- Content of main page -->

		<footer class="main-footer">
			<strong>{{ trans('pages.copyright') }}
			<div class="float-right d-none d-sm-inline-block"></div>
		</footer>

		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Control sidebar content goes here -->
		</aside>
		<!-- /.control-sidebar -->
	</div>
	<!-- ./wrapper -->

	<!-- jQuery -->
	{!! Html::script('js/plugins/jquery/jquery.min.js') !!}
	<!-- jQuery UI 1.11.4 -->
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
	<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
	<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>
	<!-- Bootstrap 4 -->
	{!! Html::script('js/plugins/bootstrap/js/bootstrap.bundle.min.js') !!}	
	<!-- Sparkline -->
	{!! Html::script('js/plugins/sparkline/jquery.sparkline.min.js') !!}	
	<!-- daterangepicker -->
	{!! Html::script('js/plugins/daterangepicker/daterangepicker.js') !!}
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js"></script>
	<!-- datepicker -->
	{!! Html::script('js/plugins/datepicker/bootstrap-datepicker.js') !!}
	<!-- Time Picker -->
	{!! Html::script('js/plugins/timepicker/bootstrap-timepicker.min.js') !!}
	<!-- Bootstrap WYSIHTML5 -->
	{!!
	Html::script('js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js')
	!!}
	<!-- Slimscroll -->
	{!! Html::script('js/plugins/slimScroll/jquery.slimscroll.min.js') !!}
	<!-- FastClick -->
	{!! Html::script('js/plugins/fastclick/fastclick.js') !!} 
	{!! Html::script('js/dist/js/adminlte.js') !!} 
	
	{!! Html::script('js/lightgallery.js') !!} 
    {!! Html::script('js/lg-pager.js') !!} 
    {!! Html::script('js/lg-autoplay.js') !!} 
    {!! Html::script('js/lg-fullscreen.js') !!} 
    {!! Html::script('js/lg-zoom.js') !!} 
    {!! Html::script('js/lg-hash.js') !!} 
    
    <script>
        lightGallery(document.getElementById('lightgallery'));
    </script>
	
	
	
	<link href="{{ asset('js/dist/js/plugins/switchery/dist/switchery.min.css') }}" rel="stylesheet">
	<script src="{{ asset('js/dist/js/plugins/switchery/dist/switchery.min.js') }}"></script>
	
	<link href="{{ asset('css/toastr.min.css') }}" rel="stylesheet">
	<script src="{{ asset('js/toastr.min.js') }}"></script>
	
	
	
	<script
		src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	@yield('after-scripts')

<script>
	$(function(){
// 		playAudio();
	});
var x = document.getElementById("myAudio"); 

function playAudio() { 
  x.play(); 
} 

$(document).ready(function(){
    $('#language').change(function(){
		var lang= $(this).val();
		if(lang=='en')
		{
			document.location.href="{{ route('lang', ['en']) }}";
		}
		else if(lang == 'fr')
		{
			document.location.href="{{ route('lang', ['fr']) }}";
		}
	});

	
    var source = new EventSource("{{ route('backoffice.notifications') }}");
    
    source.onmessage = function(event) {        
        res = JSON.parse(event.data)
        
        /* Step 1 get notification count*/
        var notiCount = $("#noticount").html();
        var notiCount = notiCount == "" ? 0 : parseInt(notiCount);

		console.log($("#noticount").html());
		if($("#noticount").html() !=0){
			console.log($("#noticount").html());
			console.log(res.count);
    		if(res.count != notiCount){
    			/* Play Sound */
    			playAudio();
    // 			var audio = new Audio('https://interactive-examples.mdn.mozilla.net/media/examples/t-rex-roar.mp3');
    // 			audio.play();
    		}        
		}
    	$("#noticount").html(res.count);		
        var li = '';
    	$(res.data).each(function(k,v){
			if("{{ app()->getLocale() }}" == 'fr')
			{
			if(v.message == 'intervention assigned'){ v.message = 'Interventions Assignées'}
			if(v.message.match(/New Report by.*/)){ 
			var newdate= v.message;
			newdate= newdate.split('by');
			v.message = 'Nouveau rapport de'+newdate[1];
			}
			if(v.message.match(/registered to copops*/))
			{
			var date= v.message;
			date= date.split('registered');
			v.message = date[0]+' enregistré à copops';
			}
			}
			li +='<li style="padding:6px 12px;"><a class="on-click-change-stat" href="javascript:void(0);" data-id="'+v.id+'" data-table="'+v.table+'" data-table-id="'+v.table_id+'"><span class="message" style="font-size:12px;">'+v.message+'</span></a><a class="on-click-change-stat" href="javascript:void(0);" style="margin-left: 10px; color: #05adfd;" data-id="'+v.id+'" data-table="'+v.table+'" data-table-id="'+v.table_id+'"><i class="fa fa-times"></i></a></li>';     
        });

        $('#menu1').html(li);
    };
	
});

$(document).on('click', '.on-click-change-stat', function(){
	var $this = $(this);
	var table = $(this).data('table');
	var tableId = $(this).data('table_id');
	var id = $(this).data('id');

	var count = (parseInt($('#noticount').html()) - 1);
	$('#noticount').html(count);
	
	$.ajax({
		'url': "{{ route('backoffice.update.notifications') }}",
		'data': {'id':id, '_token': '{{ csrf_token() }}'},
		'type':'post',
		success : function(response){
			$this.parent('li').remove();
		}
	});
});
</script>
</body>
</html>