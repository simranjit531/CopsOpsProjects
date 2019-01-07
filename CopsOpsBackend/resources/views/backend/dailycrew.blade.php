@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/usermanagement')}}">{{ trans('pages.usermgnt.listofusers')}}</a></li>
		<li><a href="{{ url('/dailycrew')}}" class="active">{{ trans('pages.usermgnt.dailycrew')}}</a></li>
		<li><a href="{{ url('/validationofregistrants')}}">{{ trans('pages.usermgnt.validationofregistrants')}}</a></li>
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
	 <div class="caree-form col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
            <div class="row">
                <div class="caree-left-form col-12 col-sm-5 col-md-5 col-lg-5 col-xl-5">
                    <h2>Create A Team</h2>
                    <form method="post" name="form_create_team" action="{{ route('dailycrewcreate') }}">
                    	@csrf
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Team Name</label>
                            <input type="text" class="form-control" name="team_name">
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Operator 1</label>
                            <select class="form-control  operator_name" name="operator_name[]">
                            	@if($operators)
                            		<option value="">Select</option>
                            		@foreach($operators as $o)
                            		<option value="{{ $o->id }}">{{ $o->first_name.' '.$o->last_name }}</option>
                            		@endforeach
                            	@endif
                            </select>                            
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Operator 2</label>
                            <select class="form-control  operator_name" name="operator_name[]">
                            	@if($operators)
                            		<option value="">Select</option>
                            		@foreach($operators as $o)
                            		<option value="{{ $o->id }}">{{ $o->first_name.' '.$o->last_name }}</option>
                            		@endforeach
                            	@endif
                            </select>  
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3 operator-div">
                            <label>Operator 3</label>
                            <select class="form-control operator_name" name=operator_name[]">
                            	@if($operators)
                            		<option value="">Select</option>
                            		@foreach($operators as $o)
                            		<option value="{{ $o->id }}">{{ $o->first_name.' '.$o->last_name }}</option>
                            		@endforeach
                            	@endif
                            </select>  
                        </div> 
                        <div id="cloned-div-area"></div>
                        <a href="javascript:void(0);" class="new-add mt-2"><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New</a>
                        <div class="clearfix"></div>
                        <button type="submit" name="btn_create_team" class="submit-btn">Create</button>
                    </form>
                </div>
                
                <div class="caree-left-form col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7" id="crew_data">
                    <ul class="list-heading">
                        <li>Name of</li>
                        <li>Effective</li>
                    </ul>
                    @if($teams)
                    	@foreach($teams as $k=>$t)
                    		<h3>{{ $k }}</h3>
                                <ul class=inner-listing>
                                	@foreach($t as $tm)
                                		<li class="view-team-data" data-id="{{ $tm->id }}"><a href="javascript:void(0);">{{ $tm->crew_name}}<span>{{ count($tm->members) }}</span></a></li>                                	
                                	@endforeach                                    
                                </ul>	
                    	@endforeach
                    @endif                    
                </div>
                
                <div class="caree-left-form col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 hide" id="crew_detail">
                    <div class="back-button">
                    	<a href="javascript:void(0);" id="anchor_back"><i class="fa fa-chevron-left"></i></a>
                    </div>
                    <div class="text-center">
                    	<p><b>Details</b></p>
                    	<p><b>Name of the crew</b></p>
                    	<p id="crew_name"></p>
                    	<p><b>Name of the crew Members</b></p>
                    	<div id="crew_member"></div>
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
		@if(Session::has('type') && Session::get('type') == 'success')
			toastr.success("{{ Session::get('message') }}", {timeOut: 10000});
		@elseif(Session::has('type') && Session::get('type') == 'error')
			toastr.error(" {{ Session::get('message') }} ", {timeOut: 10000});
		@else
			console.log("Test");	
		@endif	
		
	});

	count = 4;
	$('.new-add').on('click', function(){
		var clonedDiv = $('.operator-div').clone();
		clonedDiv.find('label').html('Operator '+ count);		
		clonedDiv.find('.operator_name').val('');
		clonedDiv.removeClass('operator-div');
		count ++;

		$('#cloned-div-area').append(clonedDiv);
	});
	
	$('button[type="submit"][name="btn_create_team"]').on('click', function(e){
		operators = [];
		e.preventDefault();

		$('.operator_name').each(function(){
			if($(this).val() !=""){
				operators.push($(this).val());
			}
		});

		if(operators.length <=1)
		{
			alert("Please select more than one crew memeber to create crew");
			return false;
		}
		var team = $('input[type="text"][name="team_name"]').val();

		if(team ==""){
			alert("Please enter team name");
			return false;
		}
			
		$('form[name="form_create_team"]').appendTo('body').submit();
		 
	});

	$('.view-team-data').on('click', function(e){
		e.preventDefault();
		var teamId = $(this).data('id');

		
		$.ajax({
			'url':"{{ route('crewget') }}",
			'data':{'_token':'{{ csrf_token() }}', 'crew_id':teamId },
			'type':'post',
			success:function(response)
			{
				if(response.flag == false)
				{
					toastr.success(response.message, {timeOut: 10000});
				}
				else
				{
					
					$('#crew_detail').find("#crew_name").html("<b>"+response.data.crew.crew_name+"</b>");

					var html = '';
					$(response.data.members).each(function(k, v){						
						html +='<p><b>'+v.member_name+'<b></p>';
					});
					
					$('#crew_member').html(html);
					
					$('#crew_detail').removeClass('hide');
					$('#crew_data').addClass('hide');
				}	
			}
		});
	});

	$('#anchor_back').on('click', function(){
		$('#crew_detail').addClass('hide');
		$('#crew_data').removeClass('hide');
	});
</script>          
@endsection