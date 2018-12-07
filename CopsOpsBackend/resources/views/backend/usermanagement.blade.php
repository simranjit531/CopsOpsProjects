@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
        <ul>
            <li><a href="#" class="active">{{ trans('pages.usermgnt.listofusers')}}</a></li>
            <li><a href="#">{{ trans('pages.usermgnt.dailycrew')}}</a></li>
            <li><a href="#">{{ trans('pages.usermgnt.validationofregistrants')}}</a></li>
            <li><a href="#">{{ trans('pages.usermgnt.accountrefuses')}}</a></li>
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
<!--<div class="panel panel-default">
    <div class="panel-body">
            <div class="form-group">
                <input type="text" class="form-control" name="name" id="name" placeholder="search name">
            </div>
    </div>
</div>
<table class="table table-bordered" id="table">
<thead>
  <tr>
    <th></th>
     <th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
     <th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
     <th>{{ trans('pages.usermgnt.tables.email') }}</th>
     <th>{{ trans('pages.usermgnt.tables.grade') }}</th>
  </tr>

</thead>
</table>-->
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
                         
                        <h3>Lorem ipsum</h3>
                         
                         <a href="" class="drop-part"><h4>Lorem ipsum <i class="fa fa-angle-down" aria-hidden="true"></i></h4></a>
                         
                         <div class="col-sm-12 input-field mb-4">
                            <label>Lorem </label>
                             <input type="text" class="form-control">
                         </div>
                         
                         
                         <div class="col-sm-12 input-field ">
                            <label>Lorem </label>
                             <textarea class="form-control"></textarea>
                         </div>
                         
                         <div class="col-sm-12 input-field address-div mt-3">
                            <label>Lorem </label>
                             <input type="text" class="form-control">
                         </div>
                         
                         <button type="submit" class="assigner-btn">Assigner</button>
                         
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
                <table class="table table-bordered table-striped" id="table">
                    <thead>
                        <tr>
                            <th></th>
                            <th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.email') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.grade') }}</th>
                        </tr>
                    </thead>
                </table>
            </div>
            
            <div class="col-sm-6 pl-0 left-side">
                <h2>{{ trans('pages.usermgnt.listofcitizens')}}</h2>
                <table class="table table-bordered table-striped" id="userTable">
                    <thead>
                        <tr>
                            <th></th>
                            <th>{{ trans('pages.usermgnt.tables.firstname') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.lastname') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.birthdate')}}</th>
                            <th>{{ trans('pages.usermgnt.tables.email')}}</th>
                            <th>{{ trans('pages.usermgnt.tables.numberofreports')}}</th>
                        </tr>
                    </thead>
                </table>
            </div>
            
        </div>
    </div>
    
    <div class="iv-jean">
        <h6>IVAN jean</h6>
        <div class="col-12 col-sm-3 col-sm-3 col-md-3 col-lg-3 col-xl-3">
            
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
          { data: 'email_id', name: 'email_id' },  
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
                { data: 'email_id', name: 'email_id' },  
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
 });
 </script>          
@endsection