@extends('backend.layouts.backendapp') @section('content')
<style>
.image-attachments{
    width: 100px;
    height: 100px;
    margin-bottom: 10px;
}
</style>
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
					<li class="breadcrumb-item"><a href="#">{{ trans('pages.home')
						}}</a></li>
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
						<a id="remove-parent" style="position: absolute; top: 5px; right: 5px;"><i class="fa fa-times"></i></a>
						
						<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true" style="margin-bottom: 15px;">
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="headingOne">
                                    <h4 class="panel-title">
                                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne" class="">                                            
                                            Operator
                                        </a>
                                        <i class="less-more fa fa-plus"></i>
                                        <div class="clearfix"></div>
                                    </h4>
                                </div>
                                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                    <div class="panel-body">
                                        <div class="col-sm-12 input-field mb-4">
                    						<label>First/Last Name </label> 
                    						<select class="form-control js-example-basic-single" name="_operators[]" style="width: 100%">
                    							<option value="">{{trans('pages.usermgnt.operator')}}</option>
                    							@if($operators) @foreach($operators as $o)
                    							<option value="{{ $o->id }}">{{ $o->first_name.'
                    							'.$o->last_name }}</option> @endforeach @endif
                    						</select>
                    					</div>
                    					
                    					<div class="col-sm-12 input-field mb-4">
                    						<label>First/Last Name </label> 
                    						<select class="form-control js-example-basic-single" name="_operators[]" style="width: 100%">
                    							<option value="">{{trans('pages.usermgnt.operator')}}</option>
                    							@if($operators) @foreach($operators as $o)
                    							<option value="{{ $o->id }}">{{ $o->first_name.'
                    							'.$o->last_name }}</option> @endforeach @endif
                    						</select>
                    					</div>
                    					
                    					<div class="col-sm-12 input-field mb-4" id="cloneDiv">
                    						<label>First/Last Name </label> 
                    						<select class="form-control js-example-basic-single" name="_operators[]" style="width: 100%">
                    							<option value="">{{trans('pages.usermgnt.operator')}}</option>
                    							@if($operators) @foreach($operators as $o)
                    							<option value="{{ $o->id }}">{{ $o->first_name.'
                    							'.$o->last_name }}</option> @endforeach @endif
                    						</select>
                    					</div>
                    					
                    					<div id="cloned-divs"></div>
                    					
                    					<div class="col-sm-12 input-field mb-4">
                    						<label><a href="javascript:void(0);">add more <i class="fa fa-plus add-more-divs"></i></a> </label>
                    					</div>
                    					
                    					
                                    </div>
                                </div>
                            </div>
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
				
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-calendar"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text" name="registration_start_date" id="registration_start_date" placeholder="{{ trans('pages.selectregdate') }}" aria-label="Search">
				</div>
				
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-calendar"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text" name="registration_end_date" id="registration_end_date" placeholder="{{ trans('pages.selectregdate') }}" aria-label="Search">
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
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-calendar"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text" name="citizen_registration_start_date" id="citizen_registration_start_date" placeholder="{{ trans('pages.selectregdate') }}" aria-label="Search">
				</div>
				
				<div class="input-group input-group-sm">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="submit">
							<i class="fa fa-calendar"></i>
						</button>
					</div>
					<input class="form-control form-control-navbar" type="text" name="citizen_registration_end_date" id="citizen_registration_end_date" placeholder="{{ trans('pages.selectregdate') }}" aria-label="Search">
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
								<th>Date</th>
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
								<th>Date</th>
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
					<a class="closeinterval close" data-dismiss="modal">&times;</a>

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
								<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 zoom-left">
									<ul id="lightgallery" class="list-unstyled row">
									
						                <li class="col-xs-6 col-sm-6 col-md-6" id="bus1" data-src="">
						                    <a href="javascript:void(0);"  id="business_card1_a">
						                        <img class="img-responsive image-attachments" id="business_card1">
						                    </a>
						                </li>
						                <li class="col-xs-6 col-sm-6 col-md-6" id="bus2" data-src="">
						                    <a href="javascript:void(0);" id="business_card2_a">
						                        <img class="img-responsive image-attachments" id="business_card2">
						                    </a>
						                </li>
						               
									<li class="col-xs-6 col-sm-6 col-md-6" id="bus3" data-src="">
					                    <a href="javascript:void(0);"  id="id_card1_a">
					                        <img class="img-responsive image-attachments" id="id_card1"/>
					                    </a>
					                </li>
					                <li class="col-xs-6 col-sm-6 col-md-6"  id="bus4" data-src="">
					                    <a href="javascript:void(0);" id="id_card2_a">
					                        <img class="img-responsive image-attachments" id="id_card2"/>
					                    </a>
					                </li>
						            </ul>
								</div>
							</div>
						</div>

						<!--<div class="zoom-div mt-4">
							<div class="row">
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding-left: 45px;">
										<a href="javascript:void(0);" id="business_card1_a">
											<img class="img-responsive image-attachments" id="business_card1"/>
										</a>
									</div>
									<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding-left: 45px;">
										<a href="javascript:void(0);" id="business_card2_a">
											<img class="img-responsive image-attachments" id="business_card2"/>
										</a>
									</div>
									<!-- 
									<a href="javascript:void(0);" id="business_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="business_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									 --
								</div>
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding-left: 45px;">
										<a href="javascript:void(0);" id="id_card1_a">
											<img class="img-responsive image-attachments" id="id_card1"/>
										</a>
									</div>
									<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding-left: 45px;">
										<a href="javascript:void(0);" id="id_card2_a">
											<img class="img-responsive image-attachments" id="id_card2"/>
										</a>
									</div>
									<!-- 
									<a href="javascript:void(0);" id="id_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="id_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									 --
								</div>
							</div>
						</div>-->

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
									href="#collapseOne9">
									<a class="card-title"> Option </a>
								</div>
								<div id="collapseOne9" class="card-body collapse"
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
					<a class="closeinterval btn btn-default" data-dismiss="modal">Close</a>
				</div>
			</div>

		</div>
	</div>
<input type="hidden" name="hidden_user_id">
</div>

@endsection @section('before-styles') @endsection
@section('after-scripts')
<script src="https://maps.googleapis.com/maps/api/js?key={{ env('GOOGLE_MAP_KEY') }}"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>

<link href="{{ asset('js/plugins/lightbox2/src/css/lightbox.css') }}" rel="stylesheet">
<script src="{{ asset('js/plugins/lightbox2/src/js/lightbox.js') }}"></script>

<script>
var oTable, oTableCitizen = "";
var intervals = [];
var bounds = new google.maps.LatLngBounds();
$(function(){

	$("#registration_start_date").datepicker().on('changeDate', function (ev) {
		$(this).datepicker('hide');
		$("#registration_end_date").val($(this).val());
		oTable.ajax.reload(); 
	});

	$("#registration_end_date").datepicker().on('changeDate', function (ev) {
		$(this).datepicker('hide');
		oTable.ajax.reload(); 
	});

	$("#citizen_registration_start_date").datepicker().on('changeDate', function (ev) {
		$(this).datepicker('hide');
		$("#citizen_registration_end_date").val($(this).val());
		oTableCitizen.ajax.reload(); 
	});

	$("#citizen_registration_end_date").datepicker().on('changeDate', function (ev) {
		$(this).datepicker('hide');
		oTableCitizen.ajax.reload(); 
	});

	$('.js-example-basic-single').select2({ placeholder: "Select a operator",});
	

	
	init(); 
	
	oTable = $('#table').DataTable({      
	      processing: true,
	      serverSide: true,
	      ajax: {
	          url: '{{ url("/userdata")  }}',
	          data: function (d) {
	              d.first_name = $('input[name=name]').val();
	              d.registration_start_date = $('#registration_start_date').val();
	              d.registration_end_date = $('#registration_end_date').val();	              
	          }
	      },
	      columns: [
	          { data: 'date', name: 'date'},
	          { data: 'first_name', name: 'first_name' },
	          { data: 'last_name', name: 'last_name' },
	          { data: 'email_id', name: 'email_id' },
	          { data: 'cops_grade', name: 'cops_grade' }, 
			  { data: 'view', name : 'view', orderable: false, searchable: false},
	      ],
	        //order: [[0, "desc"]]
	});

	oTableCitizen = $('#userTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
          url: '{{ url("/userdatacitizen")  }}',
          data: function (d) {
            d.first_name = $('input[name=cname]').val();
            d.registration_start_date = $('#citizen_registration_start_date').val();
            d.registration_end_date = $('#citizen_registration_end_date').val();
          }
        },
        columns: [
                { data: 'date', name: 'date'},
                { data: 'first_name', name: 'first_name' },
                { data: 'last_name', name: 'last_name' },
                { data: 'date_of_birth', name: 'date_of_birth' },
                { data: 'email_id', name: 'email_id' }, 
                { data: 'total_reports', name: 'total_reports' }, 
				{ data: 'view', name : 'view', orderable: false, searchable: false},				
          ],
        //order: [[0, "desc"]]
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
   $('input[type="hidden"][name="hidden_user_id"]').val(userid);
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
		
			$('.loader_a').addClass('hide');
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 h6').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(1) span').html(d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(2) span').html(d[0]['date_of_birth']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(3) span').html(d[0]['phone_number']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(4) span').html(d[0]['email_id']);
			if(d[0]['profile_image'] !="") $('.modal .modal-dialog .modal-content .modal-body').find('img#profile_image').attr('src', d[0]['profile_image']);

			$('.modal .modal-dialog .modal-content .modal-body').find('#cop_grade').html(d[0]['cops_grade']);
						
			if(d[0]['business_card1'] !=""){
				$('.modal .modal-dialog .modal-content .modal-body').find('#business_card1').attr('src', d[0]['business_card1']).attr('target','_blank');
				//$('.modal .modal-dialog .modal-content .modal-body').find('#business_card1_a').attr('href', d[0]['business_card1']).attr('data-lightbox','image-1');
				$('#bus1').attr('data-src',d[0]['business_card1']);
			}
			if(d[0]['business_card2'] !=""){
				$('.modal .modal-dialog .modal-content .modal-body').find('#business_card2').attr('src', d[0]['business_card2']).attr('target','_blank');
				//$('.modal .modal-dialog .modal-content .modal-body').find('#business_card2_a').attr('href', d[0]['business_card2']).attr('data-lightbox','image-1');
				$('#bus2').attr('data-src',d[0]['business_card2']);
			}

			if(d[0]['id_card1'] !=""){
				$('.modal .modal-dialog .modal-content .modal-body').find('#id_card1').attr('src', d[0]['id_card1']).attr('target','_blank');
				//$('.modal .modal-dialog .modal-content .modal-body').find('#id_card1_a').attr('href', d[0]['id_card1']).attr('data-lightbox','image-1');
				$('#bus3').attr('data-src',d[0]['id_card1']);
			}
			if(d[0]['id_card2'] !=""){
				$('.modal .modal-dialog .modal-content .modal-body').find('#id_card2').attr('src', d[0]['id_card2']).attr('target','_blank');
				//$('.modal .modal-dialog .modal-content .modal-body').find('#id_card2_a').attr('href', d[0]['id_card2']).attr('data-lightbox','image-1');
				$('#bus4').attr('data-src',d[0]['id_card2']);
			}
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
// 				  	lat = d[0]['latitude'];
// 					lng = d[0]['longitude'];
// 					var position = new google.maps.LatLng(lat, lng);
// 		        	bounds.extend(position);

// 					map1= {
// 					  	center:new google.maps.LatLng(lat, lng),
// 					  	zoom:15,
// 					};

// 					map1 = new google.maps.Map(document.getElementById("map"), map1);
// 					marker = new google.maps.Marker({
// 		            position: position,
// 		            map:map1,
// 		            icon:'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
// 		        	});
			}
			else if(d[0]['ref_user_type_id'] == '{{ App\UserType::_TYPE_OPERATOR }}') {
				$('.modal .modal-dialog .modal-content .modal-body .operator').find('span#operator_assigned_report').text(d[0]['assigned_incidents']);						
				$('.modal .modal-dialog .modal-content .modal-body .operator').find('span#operator_completed_report').text(d[0]['completed_incidents']);	
// 			    lat = d[0]['latitude'];
// 				lng = d[0]['longitude'];
// 				console.log(d[0]['latitude']+""+d[0]['longitude'])
// 				var position = new google.maps.LatLng(lat, lng);
// 	        	bounds.extend(position);

// 				map1= {
// 				  	center:new google.maps.LatLng(lat, lng),
// 				  	zoom:15,
// 				};

// 				map1 = new google.maps.Map(document.getElementById("map"), map1);
// 				marker = new google.maps.Marker({
// 	            position: position,
// 	            map:map1,
// 	            icon:'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
// 	        	});
			}
			//lat = d[0]['latitude'];
			//lng = d[0]['longitude'];			

			//var center = new google.maps.LatLng(lat, lng);
			//_initialize(center, 'map', 15);
			
			 //var markerArray = [];
			// markerArray.push([lat, lng, d[0].ref_user_type_id]);

 			//add_markers(markerArray, true);
					
			$('#myModal').modal('show');
			user_live_location(userid);

			/*$("#business_card1_a").click(function(){
			    lightbox.start($(this));
			    return false;
			});

			$("business_card2_a").click(function(){
			    lightbox.start($(this));
			    return false;
			});

			$("#id_card1_a").click(function(){
			    lightbox.start($(this));
			    return false;
			});

			$("#id_card2_a").click(function(){
			    lightbox.start($(this));
			    return false;
			});*/
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

	var operators = [];
	$("select[name='_operators[]']").each(function(){
		if($(this).val() !="") operators.push($(this).val());
	});
	var operatorId = operators;
	console.log(operatorId);
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
				$(".loactions-out-inner").toggle();
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

$("#remove-parent").on("click", function(){
	$(this).parents('.loactions-out-inner').hide();
});

$(".less-more").on("click", function(){
	$(this).toggleClass("fa-minus");
	$("#collapseOne").toggle();
});

$(".add-more-divs").on("click", function(e){
	var clonedDiv = $("#cloneDiv").clone();
	clonedDiv.removeAttr("id");
	clonedDiv.find('select[name="_operators[]"]').val('Operator');

	$("#cloned-divs").append(clonedDiv);
});


$(function(){
	/* Function to do live user tracking, the script will run every 30 seconds and will update map marker position*/
	
	var userId = $('input[type="hidden"][name="hidden_user_id"]').val();
	console.log(userId);
	user_live_location(userId);
});
function clearMyInterval()
{
	var lat = 48.864716;
	var lng = 2.349014;		
	var center = new google.maps.LatLng(lat, lng);
	_initialize(center, 'map', 5);
	if(intervals.length > 0)
	{
		for(var i=0; i<intervals.length; i++)
		{
			clearInterval(intervals[i]); 
		}
	}
}
$(document).on('click','.closeinterval',function(){
	
	clearMyInterval();
});
function user_live_location(userId){	
	if(userId !=""){
	if(intervals.length > 0)
	{
		clearMyInterval();
	}
	// var interva = setInterval( function () {
    	    $.ajax({
    			'url':"{{ route('backoffice.live.location') }}",
    			'data':{"_token": "{{ csrf_token() }}", "user_id":userId},
    			success : function(response){
    				if(response.status == true){
						var lat = response.data[0].latitude;
						var lng = response.data[0].longitude;		
						var center = new google.maps.LatLng(lat, lng);
						_initialize(center, 'map', 15);
						
    					var markerArray = [];
    					var flightPlanCoordinates =[];
    					$(response.data).each(function(k,v){
    						t = {};
    						t['lat'] = parseFloat(v.latitude);
    						t['lng'] = parseFloat(v.longitude);
    						// t['ref_user_type_id'] = v.ref_user_type_id;    						
    						markerArray.push([v.latitude, v.longitude, response.ref_user_type_id]);

    						flightPlanCoordinates.push(t);
    					});

    		 			add_markers(markerArray, true);
    		 			draw_route(flightPlanCoordinates);
    				}
    			}
    		});
    	// }, 5000 );
		intervals.push(interva);
	}
}



var markers = [];
/* Initalise map */
function _initialize(center, mapId, zoom) {
	
    var mapOptions = {
        center: center,
        zoom : zoom
    };
    map = new google.maps.Map(document.getElementById(mapId), mapOptions);	 
} 
/* Initalise map */



/* Add markers to the map */

function add_markers(markerArray, lat, lng)
{		
	// console.log(markerArray);
	// clear_markers();	
	for( i = 0; i < markerArray.length; i++ ) 
	{
		console.log(markerArray[i][0], markerArray[i][1]);
		var position = new google.maps.LatLng(markerArray[i][0], markerArray[i][1]);
		
		marker = new google.maps.Marker({
            position: position,
            map:map,
//             title: markerArray[i][2]
    	});

		markers.push(marker);
		
	}

	
	// map.fitBounds(bounds);       
	// map.panToBounds(bounds);
	// map.setZoom(5);
}


function clear_markers() 
{	
	for (var i = 0; i < markers.length; i++) 
	{
  		markers[i].setMap(null);
	}
	
	markers = [];	
}


function draw_route(flightPlanCoordinates)
{	        
    var flightPath = new google.maps.Polyline({
	    path: flightPlanCoordinates,
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2
    });

    flightPath.setMap(map);
}

</script>
@endsection
