@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
        <ul>
            <li><a href="{{ url('/usermanagement')}}" class="active">{{ trans('pages.usermgnt.listofusers')}}</a></li>
            <li><a href="{{ url('/dailycrew')}}" >{{ trans('pages.usermgnt.dailycrew')}}</a></li>
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
    <div class="row">
        <div class="col-sm-6 pl-0">
             <h2 class="outlin-heading">{{ trans('pages.usermgnt.Operatorstools') }}</h2>
            <div class="outil-section">
            <div class="input-group input-group-sm">
                  <div class="input-group-append">
                  <button class="btn btn-navbar" type="submit">
                    <i class="fa fa-search"></i>
                  </button>
                </div>
                <input class="form-control form-control-navbar" type="text" name="name" id="name" placeholder="{{trans('pages.usermgnt.enternamecitizen')}}" aria-label="Search">
              </div>
                 <div class="locations-out">
                     <a href="javascript:void();" id="assignanintervention">{{trans('pages.usermgnt.assignanintervention')}} <i class="fa fa-tachometer" aria-hidden="true"></i></a>
                     <div class="loactions-out-inner">
                         
                        <h3>{{trans('pages.usermgnt.assignanintervention')}}</h3>
                         
                         <a href="" class="drop-part"><h4>{{trans('pages.usermgnt.operator')}} <i class="fa fa-angle-down" aria-hidden="true"></i></h4></a>
                         
                         <div class="col-sm-12 input-field mb-4">
                            <label>{{ trans('pages.usermgnt.object') }} </label>
                             <input type="text" class="form-control">
                         </div>
                         
                         
                         <div class="col-sm-12 input-field ">
                            <label>{{ trans('pages.usermgnt.descriptionoftheintervention')}} </label>
                             <textarea class="form-control"></textarea>
                         </div>
                         
                         <div class="col-sm-12 input-field address-div mt-3">
                            <label>{{ trans('pages.usermgnt.address')}}</label>
                             <input type="text" class="form-control">
                         </div>
                         
                         <button type="submit" class="assigner-btn">{{ trans('pages.usermgnt.assign')}}</button>
                         
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
                <input class="form-control form-control-navbar" type="text" name="cname" id="cname" placeholder="{{trans('pages.usermgnt.enternameoperator')}}" aria-label="Search">
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
    
    <!--<div class="iv-jean">
        <h6>IVAN jean</h6>
        <div class="col-12 col-sm-3 col-sm-3 col-md-3 col-lg-3 col-xl-3">
            
        </div>
    </div>-->
	
<!--<div class="m7 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
        
        <div class="row">
            
            <div class="col-12 col-sm-6 col-md-6 col-lg-6 col--xl-6 user-mgm-m7">
                <h6>DE La PAIX Jean yves</h6>
                
                <div class="row">
                    
                    <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
                        <figure><img src="{{asset('img/jean-img.jpg')}}" alt="jean-img"></figure>
                        <h2>Mobilsable <span>Grade III</span></h2>
                    </div>
                    
                    <div class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
                        <ul>
                            <li>{{ trans('pages.usermgnt.tables.firstname')}} / {{ trans('pages.usermgnt.tables.lastname')}}<span>DE LA Paix jean</span></li>
                            <li>{{ trans('pages.usermgnt.tables.lastname')}} <span>jean</span></li>
                            <li>{{ trans('pages.usermgnt.tables.birthdate')}}<span>12/02/2018</span></li>
                            <li>{{ trans('pages.usermgnt.tables.number')}} <span>06558455</span></li>
                            <li>{{ trans('pages.usermgnt.tables.email')}} <span>DELAPXI@gmail.com</span></li>
                        </ul>
                    </div>
                    
                </div>
                
                
                <div class="inention-div">
                    <div class="row">
                        <div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
                            <h2>{{ trans('pages.usermgnt.intervention')}} <br> {{ trans('pages.usermgnt.attributed')}}<span>12</span></h2>
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
                
                <div class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d943425.2893083721!2d0.3684537420433749!3d46.407246459696296!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd54a02933785731%3A0x6bfd3f96c747d9f7!2sFrance!5e0!3m2!1sen!2sin!4v1544421187963" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
                </div>
                
                <div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
                    <label class="w-100">Address</label>
                    <input type="text" class="form-control">
                </div>
                
                
               
            <div id="accordion" class="accordion">
                <div class="card mb-0">
                    <div class="card-header collapsed" data-toggle="collapse" href="#collapseOne">
                        <a class="card-title">
                            Item 1
                        </a>
                    </div>
                    <div id="collapseOne" class="card-body collapse" data-parent="#accordion" >
                        <p>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                            aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat
                            craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                        </p>
                    </div>
                </div>
            </div>

                
            </div>
            
            <div class="col-12 col-sm-6 col-md-6 col-lg-6 col--xl-6 user-mgm-m7">
                <h6>DE La PAIX Jean yves</h6>
                
                <div class="row">
                    
                    <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
                        <figure><img src="{{asset('img/jean-img.jpg')}}" alt="jean-img"></figure>
                        <h2>Mobilsable <span>Grade III</span></h2>
                    </div>
                    
                    <div class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
                        <ul>
                           <li>{{ trans('pages.usermgnt.tables.firstname')}} / {{ trans('pages.usermgnt.tables.lastname')}}<span>DE LA Paix jean</span></li>
                            <li>{{ trans('pages.usermgnt.tables.lastname')}} <span>jean</span></li>
                            <li>{{ trans('pages.usermgnt.tables.birthdate')}}<span>12/02/2018</span></li>
                            <li>{{ trans('pages.usermgnt.tables.number')}} <span>06558455</span></li>
                            <li>{{ trans('pages.usermgnt.tables.email')}} <span>DELAPXI@gmail.com</span></li>
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
                
                <div class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d943425.2893083721!2d0.3684537420433749!3d46.407246459696296!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd54a02933785731%3A0x6bfd3f96c747d9f7!2sFrance!5e0!3m2!1sen!2sin!4v1544421187963" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
                </div>
                
                <div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
                    <label class="w-100">Address</label>
                    <input type="text" class="form-control">
                </div>
                
                
               
            <div id="accordion" class="accordion">
                <div class="card mb-0">
                    <div class="card-header collapsed" data-toggle="collapse" href="#collapseOne">
                        <a class="card-title">
                            Item 1
                        </a>
                    </div>
                    <div id="collapseOne" class="card-body collapse" data-parent="#accordion" >
                        <p>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                            aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat
                            craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                        </p>
                    </div>
                </div>
            </div>

                
            </div>
        </div>
        
        </div>-->
		
<!-- Modal -->
  <div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
       
      </div>
      <div class="modal-body">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col--xl-12 user-mgm-m7">
                <h6></h6>
                
                <div class="row">
                    
                    <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
                        <figure><img src="{{asset('img/jean-img.jpg')}}" alt="jean-img"></figure>
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
						<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
                            <h2>{{ trans('pages.usermgnt.intervention')}} <br> {{ trans('pages.usermgnt.attributed')}}</h2>
						</div>
						<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
						<span>12</span>
						</div>
                        </div>
                        <div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
						<div class="col-11 col-sm-11 col-md-11 col-lg-11 col-xl-11">
                            <h2>{{ trans('pages.usermgnt.totelinterventionfenced')}}</h2>
						</div>
						<div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1">
						<span>18</span>
						</div>
                           
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
                
                <div class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d943425.2893083721!2d0.3684537420433749!3d46.407246459696296!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd54a02933785731%3A0x6bfd3f96c747d9f7!2sFrance!5e0!3m2!1sen!2sin!4v1544421187963" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
                </div>
                
                <div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
                    <label class="w-100">Address</label>
                    <input type="text" class="form-control">
                </div>
                
                
               
            <div id="accordion" class="accordion">
                <div class="card mb-0">
                    <div class="card-header collapsed" data-toggle="collapse" href="#collapseOne">
                        <a class="card-title">
                            Item 1
                        </a>
                    </div>
                    <div id="collapseOne" class="card-body collapse" data-parent="#accordion" >
                        <p>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                            aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat
                            craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                        </p>
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
  
@endsection
@section('before-styles')

@endsection
@section('after-scripts')
<script>
 $(function() {
     var oTable = $('#table').DataTable({
      dom: "<'row'<'col-xs-12'<'col-xs-6'l><'col-xs-6'>>r>"+
            "<'row'<'col-xs-12't>>"+
            "<'row'<'col-xs-12'<'col-xs-6'i><'col-xs-6'p>>>",
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


      var oTableCitizen = $('#userTable').DataTable({
        dom: "<'row'<'col-xs-12'<'col-xs-6'l><'col-xs-6'>>r>"+
        "<'row'<'col-xs-12't>>"+
        "<'row'<'col-xs-12'<'col-xs-6'i><'col-xs-6'p>>>",
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
                { data: 'cops_grade', name: 'cops_grade' }, 
				{ data: 'view', name : 'view', orderable: false, searchable: false},				
          ],
        order: [[0, "asc"]]
      });

  $('#name').on('keyup', function(e) {
      oTable.draw();
     // e.preventDefault();
  });

    $('#cname').on('keyup', function(e) {
      oTableCitizen.draw();
     // e.preventDefault();
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
			success: function (d) {
				
				$('.loader_a').addClass('hide');
				 $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 h6').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
				 $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(1) span').html(d[0]['last_name']);
				 $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(2) span').html(d[0]['date_of_birth']);
				 $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(3) span').html(d[0]['phone_number']);
				  $('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(4) span').html(d[0]['email_id']);
				  $('#myModal').modal('show');
			}
		});

	   });
 });
 </script>          
@endsection