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
							<button class="btn btn-navbar" type="button">
								<i class="fa fa-search"></i>
							</button>
						</div>
						<input class="form-control form-control-navbar" type="search"
							placeholder="Porte" aria-label="Search" id="search_places">

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
							<input type="hidden" name="hidden_lat"/>
							<input type="hidden" name="hidden_lng"/>
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
<script src="https://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&key={{ env('GOOGLE_MAP_KEY') }}"></script>
<script>

var map, marker;
var bounds = new google.maps.LatLngBounds();
var markers = [];
var markerArray = [];

function init() 
{
	map= {
	  	center:new google.maps.LatLng(48.864716, 2.349014),
	  	zoom:5,
	};

	map = new google.maps.Map(document.getElementById("map"), map);
}

var content = [];
function add_markers(markerArray)
{
	clear_markers();

	for( i = 0; i < markerArray.length; i++ ) {
		var html = '';	
    	html += '<div class="info_content">';
    	html += '<h3>'+markerArray[i][2]+'</h3>';
    	html += '<p style="margin-top:10px">'+markerArray[i][3]+'</p>';
    	html += '<p>'+markerArray[i][4]+'</p>';
    	html +='</div>';

    	content.push(html);
	}

	
	// Loop through our array of markers & place each one on the map  
    for( i = 0; i < markerArray.length; i++ ) {
        var position = new google.maps.LatLng(markerArray[i][0], markerArray[i][1]);
        bounds.extend(position);
        
        marker = new google.maps.Marker({
            position: position,
            map:map,
            title: markerArray[i][2]      
        });
        markers.push(marker);
		
        var infoWindow = new google.maps.InfoWindow();
        
     // Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {            
            return function() {
                infoWindow.setContent(content[i]);
                infoWindow.open(map, marker);
            }
        })(marker, i));
        
        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);

        
    }
    console.log(markerArray);
    markerArray = [];
}

function setMapOnAll(map) 
{
	for (var i = 0; i < markers.length; i++) 
	{
  		markers[i].setMap(map);
	}
}

function clear_markers()
{
	setMapOnAll(null);
}












var oTable, lat, lng = '';
$(function(){
	init();
	
    oTable = $('#datatables').DataTable({
      processing: false,
      serverSide: true,
      parseHtml : true,
      ajax: {
          url: '{{ route("backoffice.incidents.list")  }}',	
          type : 'post',
          data: function (d) {
        	  d._token = '{{ csrf_token() }}';
              d.lat = $('input[type="hidden"][name="hidden_lat"]').val();
              d.lng = $('input[type="hidden"][name="hidden_lng"]').val();
              
          },
          dataFilter: function(response){
              // this to see what exactly is being sent back
              var result = JSON.parse(response);

              $(result.data).each(function(k,v){
              	markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address]);	
              });
              add_markers(markerArray);
              		
              return response;
          },
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

/*************************************************************************************************************************/
var input = document.getElementById('search_places');
var autocomplete = new google.maps.places.Autocomplete(input);

google.maps.event.addListener(autocomplete, 'place_changed', function () {
    var place = autocomplete.getPlace();
    lat = place.geometry.location.lat();
    lng = place.geometry.location.lng();

    $('input[type="hidden"][name="hidden_lat"]').val(lat);
    $('input[type="hidden"][name="hidden_lng"]').val(lng);

    oTable.ajax.reload();     
});
/*************************************************************************************************************************/


$(document).on('click', '.view-incident', function(){
	var $this = $(this);
	var incidentId = $this.data('incidentId');

	/* Get data based on incident id */

	



	
});




</script>

@endsection
