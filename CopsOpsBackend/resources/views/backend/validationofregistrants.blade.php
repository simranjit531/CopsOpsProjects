@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/usermanagement')}}">{{ trans('pages.usermgnt.listofusers')}}</a></li>
		<li><a href="{{ url('/dailycrew')}}">{{ trans('pages.usermgnt.dailycrew')}}</a></li>
		<li><a href="{{ url('/validationofregistrants') }}" class="active">{{ trans('pages.usermgnt.validationofregistrants')}}</a></li>
		<li><a href="{{ url('/accountrefuses')}}">{{ trans('pages.usermgnt.accountrefuses')}}</a></li>
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
                <table class="table table-bordered table-striped" id="validationReg">
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
            
            <div class="col-12 col-sm-6 col-md-6 col-lg-6 col--xl-6 user-mgm-m7 rightPart">
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
                            <a href="javascript:void(0)" data-toggle="modal" data-target="#favoritesModal" class="idCardOne" rel="{{ url('img/jean-img.jpg') }}"><i class="fa fa-search-plus" aria-hidden="true"></i></a>
                            <a href="javascript:void(0)"><i class="fa fa-search-plus" aria-hidden="true"></i></a>
                        </div>
                        <div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
                            <h2>Carte professional</h2>
                            <a href="javascript:void(0)"><i class="fa fa-search-plus" aria-hidden="true"></i></a>
                            <a href="javascript:void(0)"><i class="fa fa-search-plus" aria-hidden="true"></i></a>
                        </div>
                    </div>
                </div>
                
     
                <div class="two-btn mt-5">
                    <a href="javascript:void(0)" class="refuser-btn">Refuser</a>
                    <a href="javascript:void(0)" class="validate-btn">Validator</a>
                </div>
                
                

                
            </div>
        </div>
		
<div class="modal fade" id="favoritesModal" 
     tabindex="-1" role="dialog" 
     aria-labelledby="favoritesModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" 
          data-dismiss="modal" 
          aria-label="Close">
          <span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
                <h6>DE La PAIX Jean yves</h6>
                
                <div class="image-clear">
                   <img class="img-responsive" src="" />  
                </div>
      </div>
      <div class="modal-footer">
        <button type="button" 
           class="btn btn-default" 
           data-dismiss="modal">Close</button>
      </div>
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
		$('#validationReg').DataTable({
            processing: true,
            serverSide: true,
            ajax: '{{ url("/validationRegTabledata")  }}',
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
				console.log(d[0]['id_card1']);
				$('.loader_a').addClass('hide');
				$('.rightPart').show();
				 $('.user-mgm-m7 h6').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(1) span').html(d[0]['last_name']);
				 $('.user-mgm-m7 .right-part ul li:eq(2) span').html(d[0]['date_of_birth']);
				 $('.user-mgm-m7 .right-part ul li:eq(3) span').html(d[0]['phone_number']);
				 $('.user-mgm-m7 .right-part ul li:eq(4) span').html(d[0]['email_id']);
				 $('.rightPart .zoom-div .zoom-left a:eq(0)').attr('rel', d[0]['id_card1']);
			}
		});

	   });
	   
	 
    $('.idCardOne').on('click', function () {
        var image = $(this).attr('rel');
		image= '{{ asset("/uploads/id_cards")}}/'+ image;
        $('#favoritesModal').on('show.bs.modal', function () {
            $(".img-responsive").attr("src", image);
        });
    });

	});
</script>          
@endsection