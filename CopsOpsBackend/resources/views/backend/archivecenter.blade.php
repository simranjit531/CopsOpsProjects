@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/archivecenter')}}" class="active">{{ trans('pages.archive.reportlog')}}</a></li>
		<li><a href="{{ url('/currenthand')}}" >{{ trans('pages.archive.currenthand')}}</a></li>
	</ul>
</div>
<!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">{{ trans('pages.archiveCenter') }}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">{{ trans('pages.archiveCenter') }}</li>
            </ol>
          </div><!-- /.col -->
      </div>
  </div>
</div>
<div class='archivecenter'>
        <div class="caree-form col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 main-archive">
            <div class="row">
                <div class="caree-left-form archive-center-left col-12 col-sm-5 col-md-5 col-lg-5 col-xl-5">
                    <h2>Cree Une Equipe</h2>
					<div class="table-responsive">
                     <table class="table table-bordered table-striped" id="archivedata">
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
                
                <div class="caree-left-form m9-screen col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7">
                    <h6>Details</h6>
                    
                    <h4><span>Nom de lorem</span> Nom delorem</h4>
                    
                    <ul>
                        <li><span>Lorem Ipsum</span> <input type="text" class="form-control"></li>
                        <li><span>Lorem Ipsum</span><input type="text" class="form-control"></li>
                        <li><span>Lorem Ipsum</span> <textarea class="form-control"></textarea></li>
                        <li><span>Lorem Ipsum</span> <textarea class="form-control"></textarea></li>
                        <li><span>Lorem Ipsum</span><input type="text" class="form-control refrence-input"></li>
                    </ul>
                    
                </div>
                
                
                
            </div>
        </div>
    
</div>


  
@endsection
@section('after-scripts')
      
	  <script>
	$(function() {
		$('#archivedata').DataTable({
            processing: true,
            serverSide: true,
            ajax: '{{ url("/archivedata")  }}',
            columns: [
            { data: 'first_name', name: 'first_name' },
          { data: 'last_name', name: 'last_name' },
          { data: 'email_id', name: 'email_id' },
          { data: 'created_at', name: 'created_at' },
          { data: 'view', name : 'view', orderable: false, searchable: false},
        ],
        order: [[0, "asc"]],
        });
	});
</script>   
	  
@endsection