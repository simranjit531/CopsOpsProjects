@extends('backend.layouts.backendapp') @section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/usermanagement')}}" class="active">{{
				trans('pages.usermgnt.listofusers')}}</a></li>
		<li><a href="{{ url('/dailycrew')}}">{{
				trans('pages.usermgnt.dailycrew')}}</a></li>
		<li><a href="{{ url('/validationofregistrants')}}">{{
				trans('pages.usermgnt.validationofregistrants')}}</a></li>
		<li><a href="{{ url('/accountrefuses')}}">{{
				trans('pages.usermgnt.accountrefuses')}}</a></li>
	</ul>
</div>
<!-- Content Header (Page header) -->
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.userManagement') }}</h1>
			</div>
			<!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.userManagement')
						}}</li>
				</ol>
			</div>
			<!-- /.col -->
		</div>
	</div>
</div>
<div class="user-management">
	<div class="row">
		<div class="col-sm-6 pl-0">
			<h2 class="outlin-heading">{{ trans('pages.usermgnt.Operatorstools')
				}}</h2>
			<div class="outil-section">
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-search"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text"
						name="name" id="name"
						placeholder="{{trans('pages.usermgnt.enternameoperator')}}"
						aria-label="Search">
				</div>
				<div class="locations-out">
					<a href="javascript:void(0);" id="assignanintervention">{{trans('pages.usermgnt.assignanintervention')}}
						<i class="fa fa-tachometer" aria-hidden="true"></i>
					</a>
					<div class="loactions-out-inner">

						<h3>{{trans('pages.usermgnt.assignanintervention')}}</h3>

						<div class="col-sm-12 input-field mb-4">
						<label>Select Operator </label> 
							<select class="form-control js-example-basic-single" name="_operators" style="width: 100%" multiple>
								<option value="">{{trans('pages.usermgnt.operator')}}</option>
								@if($operators) @foreach($operators as $o)
								<option value="{{ $o->id }}">{{ $o->first_name.'
									'.$o->last_name }}</option> @endforeach @endif
							</select>
						</div>

						<div class="col-sm-12 input-field mb-4">
							<label>{{ trans('pages.usermgnt.object') }} </label> <select
								class="form-control" name="_object">
								<option value="">{{ trans('pages.usermgnt.object') }}</option>
								@if($incidents) @foreach($incidents as $i)
								<option value="{{ $i->id }}">{{ $i->sub_category_name .'-'.
									words($i->incident_description, 50, '...') }}</option>
								@endforeach @endif
							</select>
						</div>


						<div class="col-sm-12 input-field ">
							<label>{{ trans('pages.usermgnt.descriptionoftheintervention')}}
							</label>
							<textarea class="form-control" name="_description"></textarea>
						</div>

						<div class="col-sm-12 input-field address-div mt-3">
							<label>{{ trans('pages.usermgnt.address')}}</label> <input
								type="text" class="form-control" name="_address">
						</div>

						<button type="submit" class="assigner-btn">{{
							trans('pages.usermgnt.assign')}}</button>

					</div>

				</div>
			</div>
		</div>


		<div class="col-sm-6 pr-0">
			<h2 class="outlin-heading">{{ trans('pages.usermgnt.citizentools') }}</h2>
			<div class="outil-section">
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-search"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text"
						name="cname" id="cname"
						placeholder="{{trans('pages.usermgnt.enternamecitizen')}}"
						aria-label="Search">
				</div>
			</div>
		</div>


	</div>

	<div class="user-manage-table mt-3">
		<div class="row">
			<div class="col-sm-6 pl-0 left-side">
				<h2>{{ trans('pages.usermgnt.listofoperators')}}</h2>
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="table">
						<thead>
							<tr>
								<th></th>
								<th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.email') }}</th>
								<th>{{ trans('pages.usermgnt.tables.grade') }}</th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

			<div class="col-sm-6 pl-0 left-side">
				<h2>{{ trans('pages.usermgnt.listofcitizens')}}</h2>
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="userTable">
						<thead>
							<tr>
								<th></th>
								<th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.birthdate')}}</th>
								<th>{{ trans('pages.usermgnt.tables.email')}}</th>
								<th>{{ trans('pages.usermgnt.tables.numberofreports')}}</th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div>
	</div>

	<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>

				</div>
				<div class="modal-body">
					<div
						class="col-12 col-sm-12 col-md-12 col-lg-12 col--xl-12 user-mgm-m7">
						<h6></h6>

						<div class="row">

							<div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
								<figure>
									<img id="profile_image" src="{{asset('img/jean-img.jpg')}}" alt="jean-img">
								</figure>
								<h2>
									<span id="cop_grade">Grade III</span>
								</h2>
							</div>

							<div
								class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
								<ul>
									<li>{{ trans('pages.usermgnt.tables.firstname')}} / {{
										trans('pages.usermgnt.tables.lastname')}}<span></span>
									</li>
									<li>{{ trans('pages.usermgnt.tables.lastname')}} <span></span></li>
									<li>{{ trans('pages.usermgnt.tables.birthdate')}}<span></span></li>
									<li>{{ trans('pages.usermgnt.tables.number')}} <span></span></li>
									<li>{{ trans('pages.usermgnt.tables.email')}} <span></span></li>
								</ul>
							</div>

						</div>


						<div class="inention-div operator hide">
							<div class="row">
								<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>
											{{ trans('pages.usermgnt.intervention')}} <br> {{
											trans('pages.usermgnt.attributed')}}
										</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span id="operator_assigned_report">12</span>
									</div>
								</div>
								<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>{{ trans('pages.usermgnt.totelinterventionfenced')}}</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span  id="operator_completed_report">18</span>
									</div>

								</div>
							</div>
						</div>

						<div class="inention-div citizen hide">
							<div class="row">
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>
											{{ trans('pages.usermgnt.intervention')}} <br> {{
											trans('pages.usermgnt.attributed')}}
										</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span id="citizen_police_report">12</span>
									</div>
								</div>
								
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>
											{{ trans('pages.usermgnt.intervention')}} <br> {{
											trans('pages.usermgnt.attributed')}}
										</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span id="citizen_fire_report">12</span>
									</div>
								</div>
								
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>
											{{ trans('pages.usermgnt.intervention')}} <br> {{
											trans('pages.usermgnt.attributed')}}
										</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span id="citizen_city_report">12</span>
									</div>
								</div>
								
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3">
									<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
										<h2>
											{{ trans('pages.usermgnt.intervention')}} <br> {{
											trans('pages.usermgnt.attributed')}}
										</h2>
									</div>
									<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
										<span id="citizen_handrail_report">12</span>
									</div>
								</div>
							</div>
						</div>

						<div class="zoom-div mt-4">
							<div class="row">
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<a href="javascript:void(0);" id="business_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="business_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
								</div>
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<a href="javascript:void(0);" id="id_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="id_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
								</div>
							</div>
						</div>

						<div
							class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
							<div id="map" style="width:100%;height:200px;"></div>
						</div>

						<!--<div
							class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Address</label> <input type="text"
								class="form-control">
						</div>-->



						<div id="accordion" class="accordion">
							<div class="card mb-0">
								<div class="card-header collapsed" data-toggle="collapse"
									href="#collapseOne">
									<a class="card-title"> Option </a>
								</div>
								<div id="collapseOne" class="card-body collapse"
									data-parent="#accordion">
									<div class="form-group">
										<div class="pull-left">{{ trans('pages.usermgnt.freeze_account')}}</div>
										<div class="pull-right">
											<label class="switch">
                                            	<input type="checkbox" name="checkbox_freeze_account">
                                            	<span class="slider round"></span>
                                            </label>		
										</div>
										<div class="clearfix"></div>
									</div>
									<div class="form-group" id="access_grade_parent">
										<div class="pull-left">{{ trans('pages.usermgnt.change_access')}}</div>
										<div class="pull-right">
											<select name="access_grade">
												<option value="Grade I">Grade I</option>
												<option value="Grade II">Grade II</option>
											</select>
										</div>
										<div class="clearfix"></div>
									</div>
								</div>
							</div>
						</div>


					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>

</div>

@endsection @section('before-styles') @endsection
@section('after-scripts')
<script src="https://maps.googleapis.com/maps/api/js?key={{ env('GOOGLE_MAP_KEY') }}"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>

<script>
var oTable, oTableCitizen = "";
var bounds = new google.maps.LatLngBounds();
$(function(){


	$('.js-example-basic-single').select2({ placeholder: "Select a operator",});
	

	
	init(); 
	
	oTable = $('#table').DataTable({      
	      processing: true,
	      serverSide: true,
	      ajax: {
	          url: '{{ url("/userdata")  }}',
	          data: function (d) {
	              d.first_name = $('input[name=name]').val();
	          }
	      },
	      columns: [
	          { data: 'action', name: 'action', orderable: false, searchable: false},
	          { data: 'first_name', name: 'first_name' },
	          { data: 'last_name', name: 'last_name' },
	          { data: 'email_id', name: 'email_id' },
	          { data: 'cops_grade', name: 'cops_grade' }, 
			  { data: 'view', name : 'view', orderable: false, searchable: false},
	      ],
	        order: [[0, "asc"]]
	});

	oTableCitizen = $('#userTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
          url: '{{ url("/userdatacitizen")  }}',
          data: function (d) {
            d.first_name = $('input[name=cname]').val();
          }
        },
        columns: [
                { data: 'action', name: 'action', orderable: false, searchable: false},
                { data: 'first_name', name: 'first_name' },
                { data: 'last_name', name: 'last_name' },
                { data: 'date_of_birth', name: 'date_of_birth' },
                { data: 'email_id', name: 'email_id' }, 
                { data: 'total_reports', name: 'total_reports' }, 
				{ data: 'view', name : 'view', orderable: false, searchable: false},				
          ],
        order: [[0, "asc"]]
  });

	$('#name').on('keyup', function(e) {
    	oTable.draw();  
  	});	

	$('#cname').on('keyup', function(e) {
	      oTableCitizen.draw();	     
	});

	
	var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));

	elems.forEach(function(html) {
	  var switchery = new Switchery(html, { size: 'small' });
	});
	
});


$("#assignanintervention").click(function(){
      $(".loactions-out-inner").toggle();
	}); 


$(document).on('click','#viewCops',function(e){   
   var userid = $(this).attr('rel');
   $.ajaxSetup({
		headers: {
			'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
		}
	});
   $.ajax({
		url: "{{ url('/viewuser') }}",
		data: { 'userid': userid},
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function(data)
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (response) {
			var d = response.data;
			console.log(d);
			$('.loader_a').addClass('hide');
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 h6').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(1) span').html(d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(2) span').html(d[0]['date_of_birth']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(3) span').html(d[0]['phone_number']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(4) span').html(d[0]['email_id']);
			if(d[0]['profile_image'] !="") $('.modal .modal-dialog .modal-content .modal-body').find('img#profile_image').attr('src', d[0]['profile_image']);

			$('.modal .modal-dialog .modal-content .modal-body').find('#cop_grade').html(d[0]['cops_grade']);
						
			if(d[0]['business_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card1').attr('href', d[0]['business_card1']).attr('target','_blank');
			if(d[0]['business_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card2').attr('href', d[0]['business_card2']).attr('target','_blank');

			if(d[0]['id_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card1').attr('href', d[0]['id_card1']).attr('target','_blank');
			if(d[0]['id_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card2').attr('href', d[0]['id_card2']).attr('target','_blank');
			$('.modal .modal-dialog .modal-content .modal-body').find('select[name="access_grade"]').val(d[0]['cops_grade']);
			$('.modal .modal-dialog .modal-content .modal-body').find('select[name="access_grade"]').attr('data-user', d[0]['id']);
			$('.modal .modal-dialog .modal-content .modal-body').find('input[type="checkbox"][name="checkbox_freeze_account"]').attr('data-user', d[0]['id']);

			if(d[0]['status'] == 0) $('.modal .modal-dialog .modal-content .modal-body').find('input[type="checkbox"][name="checkbox_freeze_account"]').prop('checked', true);
			else $('.modal .modal-dialog .modal-content .modal-body').find('input[type="checkbox"][name="checkbox_freeze_account"]').prop('checked', false);

			$('.operator').removeClass('hide');
			$('.citizen ').addClass('hide');
			
			if(d[0]['ref_user_type_id'] != '{{ App\UserType::_TYPE_OPERATOR }}'){
				$('#access_grade_parent').hide();
				$('.operator').addClass('hide');
				$('.citizen ').removeClass('hide');	

				
				$('.modal .modal-dialog .modal-content .modal-body .citizen').find('span#citizen_police_report').text(d[0]['report_police']);						
				$('.modal .modal-dialog .modal-content .modal-body .citizen').find('span#citizen_fire_report').text(d[0]['report_fire']);
				$('.modal .modal-dialog .modal-content .modal-body .citizen').find('span#citizen_city_report').text(d[0]['report_city']);
				$('.modal .modal-dialog .modal-content .modal-body .citizen').find('span#citizen_handrail_report').text(d[0]['report_handrail']);
				  	lat = d[0]['latitude'];
					lng = d[0]['longitude'];
					var position = new google.maps.LatLng(lat, lng);
		        	bounds.extend(position);

					map1= {
					  	center:new google.maps.LatLng(lat, lng),
					  	zoom:10,
					};

					map1 = new google.maps.Map(document.getElementById("map"), map1);
					marker = new google.maps.Marker({
		            position: position,
		            map:map1,
		            icon:'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
		        	});
			}
			else if(d[0]['ref_user_type_id'] == '{{ App\UserType::_TYPE_OPERATOR }}') {
				$('.modal .modal-dialog .modal-content .modal-body .operator').find('span#operator_assigned_report').text(d[0]['assigned_incidents']);						
				$('.modal .modal-dialog .modal-content .modal-body .operator').find('span#operator_completed_report').text(d[0]['completed_incidents']);	
			    lat = d[0]['latitude'];
				lng = d[0]['longitude'];
				console.log(d[0]['latitude']+""+d[0]['longitude'])
				var position = new google.maps.LatLng(lat, lng);
	        	bounds.extend(position);

				map1= {
				  	center:new google.maps.LatLng(lat, lng),
				  	zoom:18,
				};

				map1 = new google.maps.Map(document.getElementById("map"), map1);
				marker = new google.maps.Marker({
	            position: position,
	            map:map1,
	            icon:'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
	        	});
			}

					
			$('#myModal').modal('show');
		}
	});
});


$('.modal').on('hidden.bs.modal', function () {
	$('.modal .modal-dialog .modal-content .modal-body').find('img#profile_image').attr('src', '{{asset('img/jean-img.jpg')}}');
})

$(document).on('click', '.showimage', 'click', function(){
	var $this = $(this);
	var src = $this.attr('src');	
});
		

$(document).on('change', 'select[name="_object"]', function(){
	var incidentId = $(this).val();

	$.ajaxSetup({
		headers: {
			'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
		}
	});
    $.ajax({
		url: "{{ route('backoffice.incident.detail') }}",
		data : { 'incident-id': incidentId },
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function()
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (response) 
		{

			d = response.data
			$('textarea[name="_description"]').val(d[0]['incident_description']);
			$('input[name="_address"]').val(d[0]['address']);
						
			$('.loader_a').addClass('hide');
			 
		}
	});	
});


$('.assigner-btn').on('click', function(){
	console.log("Test");
	var objectId = $('select[name="_object"]').val();
	var operatorId = $('select[name="_operators"]').val();
	//console.log(objectId+'----'+operatorId);
// 	if(operatorId ==" ") alert("Please select some operators"); return false;
// 	if(objectId ==" ") alert("Please select Intervention"); return false;

	console.log("Test");
	$.ajax({
		url: "{{ route('backoffice.intervetnion.assign') }}",
		data : { 'objectId': objectId, 'operators-id': operatorId },
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function(data)
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (d) 
		{			
			$('.loader_a').addClass('hide');
			if(d.status){
				oTable.draw();   
				oTableCitizen.draw();
				toastr.success(d.message, {timeOut: 10000});
				$('#myModal').modal('hide');
			}
			else{
				toastr.error("Invalid request", {timeOut: 10000});
			}
			 
		}
	});	
	
});


/* Change grade */
$(document).on('change', 'select[name="access_grade"]', function(){
	userId = $(this).data('user');
	
	$.ajax({
		url: "{{ route('backoffice.grade.update') }}",
		data : { 'grade': $(this).val(), 'user-id': userId },
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function(data)
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (d) 
		{			
			$('.loader_a').addClass('hide');
			if(d.status){
				oTable.draw();   
				oTableCitizen.draw();
				toastr.success(d.message, {timeOut: 10000});
				$('#myModal').modal('hide');
			}
			else{
				toastr.error("Invalid request", {timeOut: 10000});
			}
			 
		}
	});	
});

/* Change grade */

function init() 
{
	var map= {
	  	center:new google.maps.LatLng(48.864716, 2.349014),
	  	zoom:5,
	};

	var map = new google.maps.Map(document.getElementById("map"), map);
}

/* Freeze account */
$(document).on('change', 'input[type="checkbox"][name="checkbox_freeze_account"]', function(){
	var userId = $(this).data('user');	
	var type = 1;
	
	if($(this).is(':checked')) type = 0;
	
	$.ajax({
		url: "{{ route('backoffice.account.update') }}",
		data : { 'type': type, 'user-id': userId },
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function(data)
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (d) 
		{			
			$('.loader_a').addClass('hide');
			if(d.status){
				oTable.draw();   
				oTableCitizen.draw();
				toastr.success(d.message, {timeOut: 10000});
				$('#myModal').modal('hide');
			}
			else{
				toastr.error("Invalid request", {timeOut: 10000});
			}
			 
		}
	});	
	
});
/* Freeze account */

</script>
@endsection
