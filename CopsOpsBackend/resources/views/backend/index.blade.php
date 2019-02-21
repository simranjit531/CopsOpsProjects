@extends('backend.layouts.backendapp') @section('content')

<style>
.round {
  position: relative;
  padding-left: 10px;
}

.round label {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 50%;
  cursor: pointer;
  height: 20px;
  left: 0;
  position: absolute;
  top: 0;
  width: 20px;
}

.round label:after {
  border: 2px solid #fff;
    border-top: none;
    border-right: none;
    content: "";
    height: 5px;
    left: 5px;
    opacity: 0;
    position: absolute;
    top: 5px;
    transform: rotate(-45deg);
    width: 10px;
}

.round input[type="checkbox"] {
  visibility: hidden;
}

.round input[type="checkbox"]:checked + label {
  background-color: #66bb6a;
  border-color: #66bb6a;
}

/*
.dropdown-menu-form li:nth-child(2) .round input[type="checkbox"]:checked + label{
  background-color: #dc3545;
  border-color: #dc3545;
}
*/

.round input[type="checkbox"]:checked + label:after {
  opacity: 1;
}

.fixed_width{
	
}
</style>
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
					<li class="breadcrumb-item"><a href="#">{{ trans('pages.home')
						}}</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.controlCenter')}}</li>

					<input type="hidden" name="hidden_user_id"
						value="{{ Auth::user()->user_id }}" />
					<input type="hidden" name="hidden_user_full_name"
						value="{{ Auth::user()->first_name.' '.Auth::user()->last_name }}" />
					<input type="hidden" name="hidden_usergroup" value="copops" />
					<input type="hidden" name="hidden_userpassword" value="webAppPass" />
					<input type="hidden" name="hidden_email"
						value="{{ Auth::user()->email_id }}" />
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
							placeholder="{{ trans('pages.PORTE')}}" aria-label="Search" id="search_places">

					</div>

					<div class="col-12 col-sm-2 col-md-2 ml-3 select-box">
						<!-- 
						<select name="user_type" id="user_type">
							<option value="" selected>--Select--</option>
							<option value="4">Citizen</option>
							<option value="3">Operator</option>
							<option value="Zone-of-Interest">Zone of Interest</option>
							<option value="Point-of-Interest">Point of Interest</option>
						</select>
						 -->
						 <div class="dropdown">
                         	<button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown">{{ trans('pages.SELECT')}}
                          	<span class="caret"></span></button>
                          	<ul class="dropdown-menu dropdown-menu-form">
                            	<li>                            	
                        			<div class="round">
                            			<input type="checkbox" id="checkbox_1" value="4"/>{{ trans('pages.Citizen')}}  
                            			<label for="checkbox_1"></label>
                          			</div>                            		
                            	</li> 
                            	<li>                            	
                        			<div class="round">
                            			<input type="checkbox" id="checkbox_2" value="3"/>{{ trans('pages.usermgnt.operator')}}
                            			<label for="checkbox_2"></label>
                          			</div>                            		
                            	</li> 
                            	<li>                            	
                        			<div class="round">
                            			<input type="checkbox" id="checkbox_3" value="Zone-of-Interest"/>{{ trans('pages.ZoneofInterest')}} 
                            			<label for="checkbox_3"></label>
                          			</div>                            		
                            	</li> 
                            	<li>                            	
                        			<div class="round">
                            			<input type="checkbox" id="checkbox_4" value="Point-of-Interest"/>{{ trans('pages.PointofInterest')}} 
                            			<label for="checkbox_4"></label>
                          			</div>                            		
                            	</li>                            	
                          	</ul>
                    	</div>
                    	
					</div>

					<div class="col-12 col-sm-4 col-md-4 location-zone ml-3">
						<ul>
							<li><a href="javascript:void(0);" id="add-zone-of-interest">{{ trans('pages.ZoneofInterest')}}
									<i class="fa fa-map-marker" aria-hidden="true"></i></a></li>
							<li><a href="javascript:void(0);" id="add-point-of-interest">{{ trans('pages.PointofInterest')}} 
									<i class="fa fa-arrows-v" aria-hidden="true"></i></a></li>
						</ul>
					</div>

				</form>
				
				<div>
					<a href="javascript:void(0)"; class="btn btn-success btn-sm" id="save-map-activity">{{ trans('pages.save')}} </a>
<!-- 					<a href="javascript:void(0)"; class="btn btn-danger btn-sm" id="remove-map-activity">Remove</a> -->
				</div>
			</div>

			<!-- /.col -->


			<div class="col-sm-12 dashboard-map mb-1">
				<div id="map" style="width: 100%; height: 400px;"></div>
			</div>


			<div class="col-sm-12 journal-table mt-2">
				<div class="row">
					<div class="table-format col-sm-12">
						<h2>
							Journal signalments &amp; intervenants <i
								class="fa fa-map-marker" aria-hidden="true"></i>
						</h2>
						<table class="table table-bordered table-striped" id="datatables">
							<input type="hidden" name="hidden_lat" />
							<input type="hidden" name="hidden_lng" />
							<thead>
								<tr>
									<th>{{ trans('pages.datatime')}}</th>
									<th>{{ trans('pages.usermgnt.address')}}</th>
									<th>{{ trans('pages.Reporter')}}</th>
									<th>{{ trans('pages.usermgnt.tables.firstname')}} / {{
										trans('pages.usermgnt.tables.lastname')}}</th>
									<th>{{ trans('pages.Subject')}}</th>
									<th>Description</th>
									<th>{{ trans('pages.other')}} Descriptions</th>
									<th class='fixed_width'>{{ trans('pages.archive.status')}}</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
					<!--  
					<div class="col-sm-3 mini-selection chat-right-admin float-right">
						<h2>Live Chat</h2>
						<div class="chat-wrapper">
							<div class="chat-header">
								<div class="row">
									<div class="col-md-2">
										<i class="fa fa-address-book-o" aria-hidden="true"></i>
									</div>
									<div class="col-md-8">
										<div class="input-group">
											<span class="input-group-addon"><i class="fa fa-search"
												aria-hidden="true"></i></span> <input type="text"
												name="search" class="form-control">
										</div>
									</div>
									<div class="col-md-2">
										<i class="fa fa-comments-o" aria-hidden="true"></i>
									</div>
								</div>
							</div>
							<div class="chat-inner-section"></div>
						</div>
					</div>
					-->
				</div>
			</div>

			<!--2Jan pp-->
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

									<div
										class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
										<figure>
											<img id="profile_image" src="{{asset('img/jean-img.jpg')}}"
												alt="jean-img">
										</figure>
										<h2>
											<span id="cop_grade"></span>
										</h2>
									</div>

									<div
										class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
										<div class="row">
											<div class="col-md-6">
												<ul>
													<li><b>{{ trans('pages.usermgnt.tables.firstname')}} / {{
															trans('pages.usermgnt.tables.lastname')}}</b><span></span>
													</li>
													<li><b>{{ trans('pages.usermgnt.tables.birthdate')}}</b><span></span></li>

													<li><b>{{ trans('pages.usermgnt.tables.email')}}</b><span></span></li>
												</ul>
											</div>

											<div class="col-md-6 text-right">
												<p>{{ trans('pages.State')}}</p>
												<div style="float: right;">
													<p class="alert-success" id="incident-state"
														style="padding: 6px 12px;">{{ trans('pages.Pending')}}</p>
												</div>
											</div>


										</div>
									</div>
								</div>

								<div class="inention-div citizen hide">
									<div class="row">
										<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3"
											id="report_police"></div>
										<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3"
											id="report_fireman_medical"></div>
										<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3"
											id="report_city"></div>
										<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3"
											id="report_handrail"></div>

									</div>
								</div>
								<div class="inention-div operator hide">
									<div class="row">
										<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
											id="assigned_incidents"></div>
										<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
											id="completed_incidents"></div>
									</div>
								</div>

								<!-- 
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
                         -->
								<p>
									<b>{{ trans('pages.inforeport')}}</b>
								</p>
								<p>
									<b>Localisation</b>
								</p>
								<div
									class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
									<div id="map1" style="width: 100%; height: 200px;"></div>
								</div>

								<div
									class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
									<label class="w-100">{{ trans('pages.usermgnt.address')}}</label>
								</div>

								<div
									class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mt-2 pl-0">
									<input type="text" class="form-control" id="incidents_address">
								</div>

								<div
									class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mt-2 mt-2 pl-0">
									<label class="">{{ trans('pages.Subjectofthereport')}}</label> <span
										id="subject_report"></span>
								</div>

								<div
									class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mt-2 pl-0">
									<label class="w-100">{{ trans('pages.description')}}</label>
									<textarea class="form-control"
										style="resize: none; width: 100%;" id="incident_description"></textarea>
								</div>

								<div
									class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mt-2 pl-0">
									<label class="w-100">{{ trans('pages.otherinfo')}}</label>
									<textarea class="form-control"
										style="resize: none; width: 100%;" id="other_description"></textarea>
								</div>
								<div
									class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
									<label class="w-100">{{ trans('pages.Attachement')}}</label>
									<p id="attachmentinc"></p>
									<div id="attachmentvideoinc"></div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<!--end 2janpp-->



		</div>
		<!-- /.row -->
	</div>
	<!-- /.container-fluid -->
</div>
<style>
.user__item_1.disabled {
	display: none;
}

.chat-header {
	border: 1px solid #e7e7e7;
}

.chat-header i {
	font-size: 15px;
}

.chat-wrapper {
	border: 1px solid #e7e7e7;
	background: #fff;
}

.chat-wrapper .chat-inner-section {
	padding: 5px;
}

.chat-inner-section .user__item_1 .user__details p.user__name {
	border-bottom: 1px dotted grey;
	margin-bottom: .1rem;
	cursor: pointer
}

.chat-header .input-group-addon {
	border: 1px solid #ced4da;
}

.chat-header .input-group-addon i {
	margin: 5px;
	border-right: 0px;
}

.chat-header input {
	height: 30px;
}

.chat-header .col-md-2 {
	margin-top: 3px;
}

#myModal .modal-dialog {
	max-width: 700px;
}

#myModal .modal-dialog .user-mgm-m7 {
	width: 90%;
}

.user-mgm-m7 {
	border: none;
	border-radius: none;
	box-shadow: none;
}

.user-mgm-m7 .right-part ul li {
	float: none;
	clear: both;
}

.inention-div {
	width: 100%;
}
</style>
<!-- /.content-header -->
@endsection @section('before-styles') @endsection

@section('after-scripts')
<script
	src="https://maps.googleapis.com/maps/api/js?&libraries=places&key={{ env('GOOGLE_MAP_KEY') }}"></script>

<!-- 
<script src="https://unpkg.com/navigo@4.3.6/lib/navigo.min.js" defer></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore.js"
	defer></script>
<script src="{{ asset('js/plugins/quickblox/quickblox.min.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/QBconfig.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/user.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/dialog.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/message.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/listeners.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/helpers.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/app.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/login.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/route.js') }}" defer></script>
 -->

<!-- Lightbox -->
<link href="{{ asset('js/plugins/lightbox2/src/css/lightbox.css') }}" rel="stylesheet">
<script src="{{ asset('js/plugins/lightbox2/src/js/lightbox.js') }}"></script>

<script>
var oTable, map, marker, pin, zone;

var markers = [], content = [], latLng = [], pins = []; zones = [];

bounds  = new google.maps.LatLngBounds();

$(function(){
	_initialize(new google.maps.LatLng(48.864716, 2.349014), 'map', 12); 
	
	_incidents_list();
// 	_auto_refresh_incidents_lists();


	/* Prevent form submit with enter */
	$(window).keydown(function(event){
	    if(event.keyCode == 13) {
	      event.preventDefault();
	      $('#search_places').blur();
	      var address = document.getElementById( 'search_places' ).value;
		  
	      codeAddress(address);
	      return false;
	    }
	});

	render_circles();
	render_pins();
});

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
	_add_infowindow(markerArray);
	clear_markers();
	console.log(markerArray);
	for( i = 0; i < markerArray.length; i++ ) 
	{
		var position = new google.maps.LatLng(markerArray[i][0], markerArray[i][1]);
		bounds.extend(position);
		
		marker = new google.maps.Marker({
            position: position,
            map:map,
            title: markerArray[i][2]
    	});

		if(markerArray[i][5] == "3") marker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
		else if(markerArray[i][5] == "4")  marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
		
		var infoWindow = new google.maps.InfoWindow();
		        
	    // Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {  
            return function() {
                infoWindow.setContent(content[i]);
                infoWindow.open(map, marker);
            }
        })(marker, i));

		markers.push(marker);		
	}

	if(lat !="" && lng !=""){
        initialLocation = new google.maps.LatLng(lat, lng);
        map.setCenter(initialLocation);
        map.setZoom(12);
	}
	
	map.fitBounds(bounds);       
    map.panToBounds(bounds);
// 	map.setZoom(5);
}

/* Add markers to the map */


/* Add point of interest */

function add_point_of_interest_markers(markerArray)
{
	for( var i = 0; i < markerArray.length; i++ ) 
	{
		var position = new google.maps.LatLng(markerArray[i][0], markerArray[i][1]);
		
		pin = new google.maps.Marker({
            position: position,
            map:map,
            icon : "{{ asset('img/pin.png') }}",
            draggable: (markerArray[i][2] == 'history') ? false : true,
    	});			

    	google.maps.event.addListener(pin, 'rightclick', (function(pin, i) {  		
      		return function() {
    			if(pins.length >=1)
    			{
    				r = confirm("{{ trans('pages.areusureremovepoi')}}");
    				if(r==true)
    				{
    		            pin.setMap(null);  
						pins.pop(pin);
    		            if(markerArray[i][2] == 'history') { refresh_pins(); }  		            
    				}
    			}	
    
    		}
        })(pin, i));

        pins.push(pin);
	}
}


/* Add point of interest */

/* Add zone of interest */

function add_zone_of_interest_circles(circleArray)
{
// 	console.log(circleArray);
// 	console.log(circleArray.length);
	for( var i = 0; i < circleArray.length; i++ ) 
	{
		var position = new google.maps.LatLng(circleArray[i][0], circleArray[i][1]);
		console.log(i);
		zone = new google.maps.Circle({
	        strokeColor: '#FF0000',
	        strokeOpacity: 0.4,
	        strokeWeight: 2,
	        fillColor: '#FF0000',
	        fillOpacity: 0.35,
	        map: map,
	        center: position,
	        radius: circleArray[i][2],
	        draggable: (circleArray[i][3] == 'history') ? false : true,
	        clickable : true , 
	        editable : true,
       });	

    	google.maps.event.addListener(zone, 'rightclick', (function(zone, i) {  		
      		return function() {
    			if(zones.length >=1)
    			{
    				r = confirm("{{ trans('pages.areusureremovepoi')}}");
    				if(r==true)
    				{
						localStorage.setItem('pins', '');	
						localStorage.setItem('zoom', '');
    					zone.setMap(null);    
    					zones.pop(zone);	
    					if(circleArray[i][3] == 'history') { refresh_zones(); }           
    				}
    			}	
    
    		}
        })(zone, i));

    	zones.push(zone);
    	console.log(zones);
	}
}

/* Add zone of interest */

/* Info window */

function _add_infowindow(markerArray)
{
	if(markerArray.length > 0)
	{
		for( i = 0; i < markerArray.length; i++ ) 
		{
    		var html = '';	
        	html += '<div class="info_content">';
        	html += '<h3>'+markerArray[i][2]+'</h3>';
        	html += '<p style="margin-top:10px">'+markerArray[i][3]+'</p>';
        	html += '<p>'+markerArray[i][4]+'</p>';
        	html +='</div>';

    		content.push(html);
		}
	}
}

/* Info window */


/* Auto refresh incidents list */

function _auto_refresh_incidents_lists()
{
	setInterval( function () {
	    oTable.ajax.reload( null, false );
	}, 10000 );
}

/* Auto refresh incidents list */ 


/* Incident list table generate */
function _incidents_list()
{
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
	              console.log(response);
	              var result = JSON.parse(response);

				  var markerArray = [], latLngArray = [];		
	              $(result.data).each(function(k,v){
	              		markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);
	              		latLngArray.push([v.latitude, v.longitude]);	
	              });

	              lat = $('input[type="hidden"][name="hidden_lat"]').val();
	              lng = $('input[type="hidden"][name="hidden_lng"]').val();

	              var checkboxes = [], userType = [];	
	          	
	          	  $('input[type="checkbox"]').each(function(){
    	          	if($(this).is(":checked")){
    	          		checkboxes.push($(this).val());	
    	          	}
    	          });	

				  if(checkboxes.length == 0) add_markers(markerArray, lat, lng);
	              		
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
	          { data: 'other_description', name: 'other_description' },
	          { data: 'status', name: 'status' },
	      ],
     	order: [[0, "desc"]]
	});	
}
/* Incident list table generate */


/* Dropdown functionality */

$('input[type="checkbox"]').on("click", function(){
	var checkboxes = [], userType = [];	

	clear_pins();
	clear_zones();
	
	$('input[type="checkbox"]').each(function(){
		if($(this).is(":checked")){
			checkboxes.push($(this).val());	
		}
	});
	
	if($.inArray("Zone-of-Interest", checkboxes) != -1){		
		render_circles();		
	}
	if($.inArray( "Point-of-Interest", checkboxes) != -1){
		render_pins();
	}

	if($.inArray( "3", checkboxes) != -1){
		userType.push(3);
	}

	if($.inArray( "4", checkboxes) != -1){
		userType.push(4);
	}
	
	$.ajax ({
        url: '{{ route("backoffice.incidents.citizencops")  }}',	
        type : 'post',
        dataType:'json',
        data: {
      	  "_token" : '{{ csrf_token() }}',
            "lat": $('input[type="hidden"][name="hidden_lat"]').val(),
            "lng": $('input[type="hidden"][name="hidden_lng"]').val(),
            "usertype": userType
            
        },
        success: function(response){
            // this to see what exactly is being sent back
            
            if(response.flag == 0){
            	clear_markers();
            }
            else{            	
				var result = response;
				
                var markerArray = [], latLngArray = [];		
                $(result.data).each(function(k,v){
                		markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);
                		latLngArray.push([v.latitude, v.longitude]);	
                });

                lat = $('input[type="hidden"][name="hidden_lat"]').val();
                lng = $('input[type="hidden"][name="hidden_lng"]').val();
                
                add_markers(markerArray, lat, lng);
            }            
            
            		
            return response;
        },
    });  
});

/* Dropdown functionality */

/* Get address */
geocoder = new google.maps.Geocoder();

function codeAddress(address) 
{
    //In this case it gets the address from an element on the page, but obviously you  could just pass it to the method instead
//     var address = document.getElementById( 'search_places' ).value;
	console.log(address);
    geocoder.geocode( { 'address' : address }, function( results, status ) {
        if( status == google.maps.GeocoderStatus.OK ) {

            //In this case it creates a marker, but you can get the lat and lng from the location.LatLng
            var lat = results[0].geometry.location.lat();
            var lng = results[0].geometry.location.lng();
			
            $('input[type="hidden"][name="hidden_lat"]').val(lat);
            $('input[type="hidden"][name="hidden_lng"]').val(lng);
            
            map.setCenter( results[0].geometry.location );
            map.setZoom(12);
			
            
        } else {
            alert( "{{trans('pages.giocodewasnotsuccess')}} " + status );
        }
    } );
}


var input = document.getElementById('search_places');
var autocomplete = new google.maps.places.Autocomplete(input);

google.maps.event.addListener(autocomplete, 'place_changed', function () {
    var place = autocomplete.getPlace();
    if(place !="")
    {
    	lat = place.geometry.location.lat();
        lng = place.geometry.location.lng();
		console.log("start");
		console.log(lat);
		console.log(lng);
		console.log("end");
        $('input[type="hidden"][name="hidden_lat"]').val(lat);
        $('input[type="hidden"][name="hidden_lng"]').val(lng);

        //oTable.ajax.reload();   
        $.ajax ({
              url: '{{ route("backoffice.incidents.list")  }}',	
              type : 'post',
              dataType:'json',
              data: {
            	  "_token" : '{{ csrf_token() }}',
                  "lat": $('input[type="hidden"][name="hidden_lat"]').val(),
                  "lng": $('input[type="hidden"][name="hidden_lng"]').val()
                  
              },
              success: function(response){
         
                  // this to see what exactly is being sent back
                  var markerArray = [];
                  var result = response;
                  var type = '';

                  if(result.recordsTotal > 0)
                  {
                  	console.log("Test");
                  	 var type = result.data[0]['ref_user_type_id'];
                     $(result.data).each(function(k,v){
                     		markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);	
                     });	

    	              	
                  }	

                 // lat = $('input[type="hidden"][name="hidden_lat"]').val();
                 // lng = $('input[type="hidden"][name="hidden_lng"]').val();
    	              
                  //add_markers(markerArray, lat, lng);		  
              },
          });  
    }    
});

/* Get address */


function render_circles()
{
	if (localStorage.getItem("circle") != '' && localStorage.getItem("circle") != null) {		
		 var circlesArrray = JSON.parse(localStorage.getItem('circle'));
		 console.log(circlesArrray);
		 if(circlesArrray.length >= 1)
		 {	
			 var markerArray =  [];
			 $(circlesArrray).each(function(k,v){				
				 markerArray.push([v.lat, v.lng, v.radius, 'history']);				 				
			});

			add_zone_of_interest_circles(markerArray);
			 
		 	var zoom = localStorage.getItem('zoom');
			var lat = localStorage.getItem('lat');
			var lng = localStorage.getItem('lng');
			
			map.setCenter(new google.maps.LatLng(lat, lng));	 		 
			map.setZoom(parseInt(zoom));				 
		 }
	 }
}

function render_pins()
{
	if (localStorage.getItem("pins") != '' && localStorage.getItem("pins") != null) {
		 var pinsArrray = JSON.parse(localStorage.getItem('pins'));
		 if(pinsArrray.length >= 1)
		 {
			 var markerArray =  [];
			 $(pinsArrray).each(function(k,v){
				 				 
				 markerArray.push([v.lat, v.lng, 'history']);
			});

		 	add_point_of_interest_markers(markerArray);	

			var zoom = localStorage.getItem('zoom');
			var lat = localStorage.getItem('lat');
			var lng = localStorage.getItem('lng');
				
			map.setCenter(new google.maps.LatLng(lat, lng));	 		 
			map.setZoom(parseInt(zoom));				 
		 }	
	 }
}







$("#add-point-of-interest").on("click", function(){

	var markerArray = [];
	var lat = $('input[type="hidden"][name="hidden_lat"]').val();
    var lng = $('input[type="hidden"][name="hidden_lng"]').val();
    
    if(lat == "" || lng == "")
	{
    	lat = "48.864716"; lng = "2.349014";
    	markerArray.push([lat, lng]);	
	}
    else if(lat !="" && lng !=""){
		markerArray.push([lat, lng]);
	}

	add_point_of_interest_markers(markerArray);

});

$("#add-zone-of-interest").on("click", function(){

	var circleArray = [];
	var lat = $('input[type="hidden"][name="hidden_lat"]').val();
    var lng = $('input[type="hidden"][name="hidden_lng"]').val();
    
    if(lat == "" || lng == "")
	{
    	lat = "48.864716"; lng = "2.349014";
    	circleArray.push([lat, lng, 3000]);	
	}
    else if(lat !="" && lng !=""){
    	circleArray.push([lat, lng, 3000]);
	}

    add_zone_of_interest_circles(circleArray);

});

$('#save-map-activity').on("click", function(){

	r = confirm("{{ trans('pages.dousaveinfo')}}");
	if(r == true)
	{
		var pinsArray = []; circlesArray = [];
		console.log(zones);
		$(pins).each(function(k,v){
			pin = {};
			pin['icon'] = v.icon;
			pin['lat'] = v.position.lat();
			pin['lng'] = v.position.lng();
			
			pinsArray.push(pin);
		});

		$(zones).each(function(k,v){
			console.log(v);
			c = {};
			c['lat'] = v.center.lat();
			c['lng'] = v.center.lng();
			c['radius'] = v.radius;

			circlesArray.push(c);
		});
		
		if(zones.length >= 1 )circlesArray = JSON.stringify(circlesArray);
		localStorage.setItem('circle', circlesArray);
		
		if(pins.length >= 1) pinsArray = JSON.stringify(pinsArray)
		localStorage.setItem('pins', pinsArray);
		
		localStorage.setItem('zoom', map.getZoom());
		localStorage.setItem('lat', map.getCenter().lat());
		localStorage.setItem('lng', map.getCenter().lng());		
		
	}	
});

function refresh_zones()
{
	var circlesArray = []; 
	$(zones).each(function(k,v){
		console.log(v);
		c = {};
		c['lat'] = v.center.lat();
		c['lng'] = v.center.lng();
		c['radius'] = v.radius;

		circlesArray.push(c);
	});
	
	if(zones.length >= 1 )circlesArray = JSON.stringify(circlesArray);
	localStorage.setItem('circle', circlesArray);
	localStorage.setItem('zoom', map.getZoom());
	localStorage.setItem('lat', map.getCenter().lat());
	localStorage.setItem('lng', map.getCenter().lng());	
}

function refresh_pins()
{
	var pinsArray = []; 
	$(pins).each(function(k,v){
		pin = {};
		pin['icon'] = v.icon;
		pin['lat'] = v.position.lat();
		pin['lng'] = v.position.lng();
		
		pinsArray.push(pin);
	});

	if(pins.length >= 1) pinsArray = JSON.stringify(pinsArray)
	localStorage.setItem('pins', pinsArray);	
	localStorage.setItem('zoom', map.getZoom());
	localStorage.setItem('lat', map.getCenter().lat());
	localStorage.setItem('lng', map.getCenter().lng());	
}

$("#remove-map-activity").on("click", function(){
	r = confirm("{{ trans('pages.doudiscard')}}");
	if(r == true)
	{		
		localStorage.setItem('circle','');
		localStorage.setItem('pins','');

		clear_zones();
		render_pins();			
	}	
});


//pp 2jan
$(document).on('click','.view-incident',function(e){  
   var incidentId = $(this).attr('data-incident-id');
   var incidentState = $(this).attr('data-state');
   $.ajaxSetup({
		headers: {
			'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
		}
	});
   $.ajax({
		url: '{{ route("backoffice.incidents.view") }}',
		data: { 'incidentid': incidentId},
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
			
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(1) span').html(d[0]['date_of_birth']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(2) span').html(d[0]['email_id']);

			if(d[0]['profile_image'] !="") $('.modal .modal-dialog .modal-content .modal-body').find('img#profile_image').attr('src', d[0]['profile_image']);

			$('.operator').removeClass('hide');
			$('.citizen ').addClass('hide');
			
			if(d[0]['ref_user_type_id'] != '{{ App\UserType::_TYPE_OPERATOR }}'){
				$('.operator').addClass('hide');
				$('.citizen ').removeClass('hide');	
			if(typeof d[0]['report_police'] !== 'undefined')
			{
				$('#report_police').html("<h2>{{ trans('pages.archive.report')}} <br>Police<span >"+d[0]['report_police']+"</span></h2>");
			}
			
			if(typeof d[0]['report_fire'] !== 'undefined')
			{
			$('#report_fireman_medical').html("<h2>{{ trans('pages.archive.report')}} <br> {{ trans('pages.Fireman')}}/medical<span >"+d[0]['report_fire']+"</span></h2>");
			}

			if(typeof d[0]['report_city'] !== 'undefined')
			{
			$('#report_city').html("<h2>{{ trans('pages.archive.report')}} <br>{{ trans('pages.City')}}<span >"+d[0]['report_city']+"</span></h2>");
			}
			
			if(typeof d[0]['report_handrail'] !== 'undefined')
			{
			$('#report_handrail').html("<h2>{{ trans('pages.handrail')}}<span >"+d[0]['report_handrail']+"</span></h2>");
			}
		}
		else if(d[0]['ref_user_type_id'] == '{{ App\UserType::_TYPE_OPERATOR }}') {
				$('.operator').removeClass('hide');
				$('.citizen ').addClass('hide');	
			if(typeof d[0]['assigned_incidents'] !== 'undefined')
			{
			$('#assigned_incidents').html("<h2>{{ trans('pages.Assigned')}}<br><span >"+d[0]['assigned_incidents']+"</span></h2>");
			}

			if(typeof d[0]['completed_incidents'] !== 'undefined')
			{
			$('#completed_incidents').html("<h2>{{ trans('pages.Completed')}}<br><span >"+d[0]['completed_incidents']+"</span></h2>");
			}
		}
			$('#incidents_address').val(d[0]['address']);
			$('#subject_report').html(d[0]['sub_category_name']);
			$('#incident_description').html(d[0]['incident_description']);
			$('#other_description').html(d[0]['other_description']);
			if(d[0]['photo'] != null)
			{
				var photo= "{{ url('/uploads/incident_image') }}/"+d[0]['photo'];
				IMG = $('<a href="'+photo+'" data-lightbox="image-1">')		    	
		    	.append('<img src="'+photo+'" style="width:200px;"></a>')			
			
    			$('#attachmentinc').html(IMG);
    
    			//lightbox open:
    			IMG.click(function(){
    			    lightbox.start($(this));
    			    return false;
    			})
			}
			if(d[0]['video'] != null)
			{
			var video= "{{ url('/uploads/incident_video') }}/"+d[0]['video'];
			$('#attachmentvideoinc').html('<a href="'+video+'" traget="_blank">View</a>');
			}

			if(d[0]['business_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card1').attr('href', d[0]['business_card1']).attr('target','_blank');
			if(d[0]['business_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card2').attr('href', d[0]['business_card2']).attr('target','_blank');

			if(d[0]['id_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card1').attr('href', d[0]['id_card1']).attr('target','_blank');
			if(d[0]['id_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card2').attr('href', d[0]['id_card2']).attr('target','_blank');

			$('#incident-state').removeClass('alert-danger');
			$('#incident-state').removeClass('alert-primary');
			$('#incident-state').removeClass('alert-success');
			if((incidentState == "On-Wait") || (incidentState == "En attente"))
			{
				$('#incident-state').addClass('alert-danger');
			}
			else if((incidentState == "Pending") || (incidentState == "En cours"))
			{
				$('#incident-state').addClass('alert-primary');
			}
			else if((incidentState == "Finished") || (incidentState == "Terminé"))
			{
				$('#incident-state').addClass('alert-success');
			}

			$('#incident-state').html(incidentState);
			
			$('#myModal').modal('show');
			lat = d[0]['latitude'];
			lng = d[0]['longitude'];
			var position = new google.maps.LatLng(lat, lng);
        	bounds.extend(position);

			map1= {
			  	center:new google.maps.LatLng(lat, lng),
			  	zoom:15,
			  	/*zoomControl: false,
				   zoomControl: false,
				  scaleControl: false,
				  scrollwheel: false,*/
				  //disableDoubleClickZoom: true,
				  //draggable:false
			};
			if(d[0]['ref_user_type_id'] == "3") icon='http://maps.google.com/mapfiles/ms/icons/red-dot.png';
	        else if(d[0]['ref_user_type_id'] == "4") icon = 'http://maps.google.com/mapfiles/ms/icons/green-dot.png';
	        
			map1 = new google.maps.Map(document.getElementById("map1"), map1);
			marker = new google.maps.Marker({
            position: position,
            map:map1,
            icon: icon,
        	});
			
		}
	});
});






 if("{{ app()->getLocale() }}" == 'fr')
	 {
	  $.extend(true, $.fn.dataTable.defaults, {
			language: {
				url : '//cdn.datatables.net/plug-ins/1.10.10/i18n/French.json'
			}
		});
	 }










function clear_zones()
{
	for(var i in zones)
	{
		zones[i].setMap(null);
	}
	zones = [];
}

function clear_pins()
{
	for(var i in pins)
	{
		pins[i].setMap(null);
	}
	pins = [];
}

function clear_markers() 
{	
	for (var i = 0; i < markers.length; i++) 
	{
  		markers[i].setMap(null);
	}
	
	markers = [];	
}


</script>



@endsection
