@extends('backend.layouts.backendapp') @section('content')



<!-- Content Header (Page header) -->
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.controlCenter') }}</h1>
			</div>
			<!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.controlCenter')
						}}</li>
				</ol>
			</div>


			<div class="round-right-part mb-3">
				<!-- SEARCH FORM -->
				<form class="form-inline col-md-8 ml-3 mt-1 float-left">
					<div class="input-group input-group-sm">
						<div class="input-group-append">
							<button class="btn btn-navbar" type="submit">
								<i class="fa fa-search"></i>
							</button>
						</div>
						<input class="form-control form-control-navbar" type="search"
							placeholder="Porte" aria-label="Search">

					</div>

					<div class="col-12 col-sm-2 col-md-2 ml-3 select-box">
						<select>
							<option value="" selected>Afficher</option>
						</select>
					</div>

					<div class="col-12 col-sm-4 col-md-4 location-zone ml-3">
						<ul>
							<li><a href="">Zone <br>d'internet <i class="fa fa-map-marker"
									aria-hidden="true"></i></a></li>
							<li><a href="">Point <br>d'internet <i class="fa fa-arrows-v"
									aria-hidden="true"></i></a></li>
						</ul>
					</div>

				</form>
			</div>

			<!-- /.col -->


			<div class="col-sm-12 dashboard-map mb-1">			
				<div id="map" style="width:100%;height:400px;"></div>
			</div>


			<div class="col-sm-12 journal-table mt-2">
				<div class="row">
					<div class="table-format col-sm-9">
						<h2>
							Journal signalments &amp; intervenants <i
								class="fa fa-map-marker" aria-hidden="true"></i>
						</h2>
						<table class="table table-bordered table-striped" id="datatables">
							<thead>
								<tr>
									<th>Date</th>
									<th>Address</th>
									<th>Reporter</th>
									<th>First/Last Name</th>
									<th>Subject</th>
									<th>Description</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
					<div class="col-sm-3 mini-selection float-right">
						<h2>Chat en Direct</h2>
						<div class="inner-section"></div>
					</div>
				</div>
			</div>



		</div>
		<!-- /.row -->
	</div>
	<!-- /.container-fluid -->
</div>
<!-- /.content-header -->
@endsection @section('before-styles') @endsection 

@section('after-scripts') 
<script src="https://maps.googleapis.com/maps/api/js?key={{ env('GOOGLE_MAP_KEY') }}"></script>
<script>

function init() 
{
	var map= {
	  	center:new google.maps.LatLng(48.864716, 2.349014),
	  	zoom:5,
	};

	var map = new google.maps.Map(document.getElementById("map"), map);
}
	
$(function(){
	init();
	
    var oTable = $('#datatables').DataTable({
      processing: false,
      serverSide: true,
      parseHtml : true,
      ajax: {
          url: '{{ route("backoffice.incidents.list")  }}',	
          type : 'post',          
          data: {'_token': '{{ csrf_token() }}' }
      },      
      columns: [
          { data: 'date', name: 'date'},
          { data: 'address', name: 'address' },
          { data: 'reporter', name: 'reporter' },
          { data: 'first_name', name: 'first_name' },
          { data: 'sub_category_name', name: 'sub_category_name' }, 
          { data: 'incident_description', name: 'incident_description' },
          { data: 'status', name: 'status' },
      ],
      order: [[0, "desc"]]
	});	  
});


$(document).on('click', '.view-incident', function(){
	var $this = $(this);
	var incidentId = $this.data('incidentId');

	/* Get data based on incident id */

	



	
});




</script>

@endsection
