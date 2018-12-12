@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/usermanagement')}}">{{ trans('pages.usermgnt.listofusers')}}</a></li>
		<li><a href="{{ url('/dailycrew')}}" >{{ trans('pages.usermgnt.dailycrew')}}</a></li>
		<li><a href="{{ url('/validationofregistrants')}}">{{ trans('pages.usermgnt.validationofregistrants')}}</a></li>
		<li><a href="{{ url('/accountrefuses')}}" class="active">{{ trans('pages.usermgnt.accountrefuses')}}</a></li>
	</ul>
</div>
<!-- Content Header (Page header) -->
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.userManagement') }}</h1>
			</div><!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.userManagement') }}</li>
				</ol>
			</div><!-- /.col -->
		</div>
	</div>
</div>
<div class="user-management">
	<div class="m7 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">

		<div class="row">

			<div class="col-12 col-sm-6 col-md-6 col-lg-6 col--xl-6 user-mgm-m7 userm-10">
				<h6>DE La PAIX Jean yves</h6>
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="reduseTable">
						<thead>
							<tr>
								<th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
								<th>{{ trans('pages.usermgnt.tables.email') }}</th>
								<th>Date</th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>



			</div>

			<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 user-mgm-m7 rightPart">
				<a href=""><i class="fa fa-angle-left" aria-hidden="true"></i></a><h6></h6>

				<div class="row">

					<div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
						<figure><img src="{{ url('img/jean-img.jpg') }}" alt="jean-img"></figure>
						<h2>Mobilsable <span>Grade III</span></h2>
					</div>

					<div class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
						<ul>
							<li>{{ trans('pages.usermgnt.tables.firstname')}} / {{ trans('pages.usermgnt.tables.lastname')}}<span></span></li>
                            <li>{{ trans('pages.usermgnt.tables.lastname')}} <span></span></li>
                            <li>{{ trans('pages.usermgnt.tables.birthdate')}}<span></span></li>
                            <li>{{ trans('pages.usermgnt.tables.number')}} <span></span></li>
                            <li>{{ trans('pages.usermgnt.tables.email')}} <span></span></li>
						</ul>
					</div>

				</div>


				<div class="inention-div">
					<div class="row">
						<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
							<h2>Inventation <br> Attribuee<span>12</span></h2>
						</div>
						<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
							<h2>Inventation <br> Attribuee<span>12</span></h2>
						</div>
					</div>
				</div>

				<div class="zoom-div mt-4">
					<div class="row">
						<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
							<h2>Carte professional</h2>
							<a href=""><i class="fa fa-search-plus" aria-hidden="true"></i></a>
							<a href=""><i class="fa fa-search-plus" aria-hidden="true"></i></a>
						</div>
						<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
							<h2>Carte professional</h2>
							<a href=""><i class="fa fa-search-plus" aria-hidden="true"></i></a>
							<a href=""><i class="fa fa-search-plus" aria-hidden="true"></i></a>
						</div>
					</div>
				</div>

				<div class="comment-box mt-3">
					<h6>Comment</h6>
					<textarea class="form-control"></textarea>
					<a href="javascript:void(0)" class="comment-validate-btn">Validate</a>
				</div>


			</div>
		</div>

	</div>

</div>
@endsection
@section('before-styles')

@endsection
@section('after-scripts')
<script>
	$(function() {
		$('#reduseTable').DataTable({
			processing: true,
			serverSide: true,
			ajax: '{{ url("/reduseTabledata")  }}',
			columns: [
			{ data: 'first_name', name: 'first_name' },
			{ data: 'last_name', name: 'last_name' },
			{ data: 'email_id', name: 'email_id' },
			{ data: 'created_at', name: 'created_at' },
			{ data: 'view', name : 'view', orderable: false, searchable: false},
			],
			order: [[0, "asc"]],
		});
		
		$('.rightPart').hide();

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
			success: function (d) {
				
				$('.loader_a').addClass('hide');
				$('.rightPart').show();
				 $('.user-mgm-m7 h6').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(1) span').html(d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(2) span').html(d[0]['date_of_birth']);
				 $('.user-mgm-m7 .right-part ul li:eq(3) span').html(d[0]['phone_number']);
				 $('.user-mgm-m7 .right-part ul li:eq(4) span').html(d[0]['email_id']);
			}
		});

	   });

	});
</script>          
@endsection